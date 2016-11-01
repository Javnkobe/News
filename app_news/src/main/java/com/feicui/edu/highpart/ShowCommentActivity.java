package com.feicui.edu.highpart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.feicui.edu.highpart.Adapter.CommentsAdapter;
import com.feicui.edu.highpart.Common.OkHttpUtil;
import com.feicui.edu.highpart.bean.BaseEntity;
import com.feicui.edu.highpart.bean.Comment;
import com.feicui.edu.highpart.bean.News;
import com.feicui.edu.highpart.util.CommonUtil;
import com.feicui.edu.highpart.util.Const;
import com.feicui.edu.highpart.util.UrlParameterUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCommentActivity extends AppCompatActivity {

    private ListView lv;
    private News news;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);
        initView();
        //得到传过来的intent
        Intent intent = getIntent();
        if (intent!=null){
            news = (News) intent.getSerializableExtra("news");
            //下载评论
            loadComment();
        }
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.list);
        adapter = new CommentsAdapter(this);
        lv.setAdapter(adapter);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void loadComment() {
        String url = getUrl();
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... params) {
                return OkHttpUtil.getString(params[0]);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null){
                    List<Comment> data = parseComments(s);
                    adapter.appendData(data,false);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(ShowCommentActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            private List<Comment> parseComments(String s) {
                Gson g = new Gson();
                Type type = new TypeToken<BaseEntity<ArrayList<Comment>>>() {
                }.getType();
                BaseEntity baseEntity = g.fromJson(s,type);
                return (List<Comment>) baseEntity.getData();
            }
        }.execute(url);
    }

    public String getUrl() {
          /*
        cmt_list ?ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
         */
        Map<String,String> map = new HashMap<>();
        map.put("ver", CommonUtil.getVersionCode(this)+"");
        map.put("nid",news.getNid()+"");
        map.put("type","1");
        map.put("stamp", "20111111");//来一个选择框可以选择查看什么时候的评论
        map.put("cid", "1");
        map.put("dir", "1");
        map.put("cnt", "20");
        String urlPath = UrlParameterUtil.parameter(Const.URL_USER_COMMENT_INFO,map);
        return urlPath;
    }
}
