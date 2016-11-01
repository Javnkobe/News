package com.feicui.edu.highpart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.edu.highpart.Common.OkHttpUtil;
import com.feicui.edu.highpart.bean.BaseEntity;
import com.feicui.edu.highpart.bean.Comment;
import com.feicui.edu.highpart.bean.News;
import com.feicui.edu.highpart.biz.LocalCommentDBManager;
import com.feicui.edu.highpart.biz.LocalNewsDBManager;
import com.feicui.edu.highpart.biz.LocalNewsSQLiteOP;
import com.feicui.edu.highpart.util.CommonUtil;
import com.feicui.edu.highpart.util.Const;
import com.feicui.edu.highpart.util.SharedPreferenceUtil;
import com.feicui.edu.highpart.util.SystemUtils;
import com.feicui.edu.highpart.util.UrlParameterUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class WebViewActivity extends AppCompatActivity {

    private WebView wbv;
    private Toolbar toolbar;
    private EditText et_comment;
    private MenuItem item;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //初始化ShareSDK
        ShareSDK.initSDK(this,"17b10f0ebee35");

        wbv = (WebView) findViewById(R.id.wbv);
        toolbar = (Toolbar) findViewById(R.id.tb);
        et_comment = (EditText) findViewById(R.id.et_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置返回箭头
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置一些webview的属性
        wbv.getSettings().setJavaScriptEnabled(true);

        final Intent intent = getIntent();
        if (intent != null) {
            news = (News) intent.getSerializableExtra("news");
            String url = news.getLink();

            //设置webview的客户端,在Activity中打开，
            wbv.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false/*!url.equals(url1)*/;//不通过浏览器打开网页
                }
            });
            wbv.loadUrl(url);
        }
        final int nid = news.getNid();
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(nid);
            }
        });
        //加载当前新闻评论数量
        loadCommentCount(nid);
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("新闻");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(/*"http://sharesdk.cn"*/news.getLink());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(/*"我是分享文本"*/news.getTitle());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(news.getLink());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(/*"http://sharesdk.cn"*/news.getLink());

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_comment_count,menu);
        item = menu.findItem(R.id.menu_comment);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_comment){
            //显示评论内容，跳转到品论界面
            Intent intent = new Intent(WebViewActivity.this,ShowCommentActivity.class);
            intent.putExtra("news",news);
            startActivity(intent);
        }else if (itemId == R.id.menu_favorite) {
            //本地收藏，存到sqlite
            MyApplication application = (MyApplication) getApplication();
            LocalNewsSQLiteOP localNewsSQLiteOP = application.localNewsSQLiteOP;
            String msg;
            if (LocalNewsDBManager.isExistNews(localNewsSQLiteOP, news.getNid())) {
                msg = "已收藏";
                Toast.makeText(WebViewActivity.this, "已收藏成功", Toast.LENGTH_SHORT).show();
            } else {
                long insert = LocalNewsDBManager.insert(localNewsSQLiteOP, news);
                if (insert > 0) {
                    msg = "收藏成功";
                    Toast.makeText(WebViewActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    msg = "收藏失败";
                    Toast.makeText(WebViewActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (itemId == R.id.menu_share){
            showShare();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCommentCount(int nid) {
        //cmt_num?ver=版本号& nid=新闻编号
        Map<String,String> map = new HashMap<>();
        map.put("ver",CommonUtil.getVersionCode(this)+"");
        map.put("nid",nid+"");
        String urlPath = UrlParameterUtil.parameter(Const.URL_USER_COMMENT_COUNT, map);
        new loadCommentCounttask().execute(urlPath);
    }
    private class loadCommentCounttask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return OkHttpUtil.getString(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析json字符串
            Gson g = new Gson();
            BaseEntity entity = g.fromJson(s, BaseEntity.class);
            if (entity == null){
                Toast.makeText(WebViewActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }else {
                double count = (double) entity.getData();//评论数量
                item.setTitle("评论数量"+(int)count);
            }
        }
    }

    private void sendComment(int nid) {
        EditText et_comment = (EditText) findViewById(R.id.et_comment);
        String content = et_comment.getText().toString().trim();
        //cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
        Map<String,String> map = new HashMap<>();
        map.put("ver", CommonUtil.getVersionCode(this)+"");
        map.put("nid",nid+"");
        map.put("token", SharedPreferenceUtil.getToken(this));
        map.put("imei", SystemUtils.getIMEI(this));
        map.put("ctx",content);
        //String urlPath = UrlParameterUtil.parameter(Const.URL_USER_COMMENT, map);

        //发送评论给服务器,需要异步请求
        new upLoadCommentTask().execute(map);
    }

    private class upLoadCommentTask extends AsyncTask<Map<String,String>,Void ,String> {
        @Override
        protected String doInBackground(Map<String, String>... params) {
            try {
                return OkHttpUtil.postString(Const.URL_USER_COMMENT,params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!= null){
                Comment comment = new Comment();//自己制造数据
                comment.setContent(et_comment.getText().toString());
                comment.setStamp(CommonUtil.getDate());//评论的时间
                comment.setPortrait("file:///assets/a7.jpg");
                LocalCommentDBManager.insert(WebViewActivity.this, comment);
                Toast.makeText(WebViewActivity.this, s, Toast.LENGTH_SHORT).show();
                wbv.requestFocus();//获取焦点
                et_comment.setText("");//清空评论

                Toast.makeText(WebViewActivity.this, s, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(WebViewActivity.this, s, Toast.LENGTH_SHORT).show();
            }
            CommonUtil.hideKeyBoard(WebViewActivity.this);
        }
    }
}
