package com.example.administrator.news.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.news.R;
import com.example.administrator.news.adapter.NewsAdapter;
import com.example.administrator.news.base.MyBaseActivity;
import com.example.administrator.news.biz_parser.ParserNews;
import com.example.administrator.news.common.HttpClientUtil;
import com.example.administrator.news.common.LogUtil;
import com.example.administrator.news.model_dao.NewsDBManager;
import com.example.administrator.news.model_entity.News;
import org.json.JSONObject;
import java.util.ArrayList;

public class ActivityMain extends MyBaseActivity {
    private ListView listView;
    private NewsAdapter adapter;
    private NewsDBManager dbManager;
    private Dialog dialog;
    private int limit = 10;// 第几页
    private int offset;// 假如第一次是 0 10 第二次 10 10 第三次 20 10
    private SwipeRefreshLayout refresh;
    private NavigationView navigationViewLeft;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        listView = (ListView) findViewById(R.id.listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        dbManager = NewsDBManager.getNewsDBManager(this);
        // 先加载缓存的新闻
        adapter = new NewsAdapter(this, listView);
        listView.setOnScrollListener(onScrollListener);
        // 单击事件跳转
        listView.setOnItemClickListener(onItemClickListener);
        if (dbManager.getCount() > 0) {
            // 数据库加载
            loadDataFromDB(limit, offset);
        } else {
            // 网络异步加载数据
            loadData();
        }

        refresh.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               Toast.makeText(ActivityMain.this, "refresh", Toast.LENGTH_SHORT).show();
               refresh.setRefreshing(true);
               loadData();
               adapter.notifyDataSetChanged();
               refresh.setRefreshing(false);
           }
       });
        listView.setAdapter(adapter);
        navigationViewLeft = (NavigationView) findViewById(R.id.navi1);
        navigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.item1:
                        openActivity(ActivityMain.class);
                        Toast.makeText(ActivityMain.this, "新闻界面", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        openActivity(ActivityCollect.class);
                        Toast.makeText(ActivityMain.this, "收藏界面", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item3:
                        openActivity(ActivityAllLeft.class);
                        Toast.makeText(ActivityMain.this, "本地界面", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item4:
                        openActivity(ActivityAllLeft.class);
                        Toast.makeText(ActivityMain.this, "跟帖界面", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item5:
                        openActivity(ActivityAllLeft.class);
                        Toast.makeText(ActivityMain.this, "图片界面", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                //mDrawerLayout.closeDrawer(navigationViewLeft);
                return false;
            }
        });

    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                // 存在数据库中
                loadDataFromDB(limit, offset);
            } else if (msg.what == 200) {
                showToast("网络连接错误");
            }
            dialog.dismiss();
        }
    };
        /**
         * 数据库加载
         * @param limit2 每页多少行
         * @param offset2 偏移多少行
         */
        private void loadDataFromDB(int limit2, int offset2) {
            // 第一次是第 0页 10行
            ArrayList<News> data = dbManager.queryNews(limit2, offset2);
            adapter.addendData(data, false);
            adapter.update();

            this.offset = offset + data.size();
        }
        // 解析数据 json
        private ParserNews newsParser;
        /**
         * 异步加载数据
         */
        private void loadData() {
            dialog = ProgressDialog.show(this, null, "加载中，请稍候。。。");
            //启动新线程加载数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
                    try {
                        newsParser = new ParserNews(ActivityMain.this);
                        // 发送请求，得到返回数据
                        String httpResponse = HttpClientUtil.httpGet(path);
                        LogUtil.d("请求返回的数据", httpResponse);
                        // 将返回的数据解析
                        JSONObject object = new JSONObject(httpResponse);
                        if (object.getString("message").equals("OK")) {
                            ArrayList<News> parser = newsParser.parser(object);
                            for (News news : parser) {
                                dbManager.insertNews(news);
                            }
                            newsParser.parser(object);
                            handler.sendEmptyMessage(100);
                        }
                        else {
                            handler.sendEmptyMessage(200);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 出错
                        handler.sendEmptyMessage(200);
                    }      }
            }).start();
        }
        private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                    LogUtil.d("点击子条目"+position);
                    News news = adapter.getAdapterData().get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("news", news);
                    openActivity(ActivityShow.class, bundle);
                }
            };
    /**
     * 滑动事件
     */
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }
        // 滑动时触发的方法
        // totalItemCount总数量
        // firstVisibleItem 第一行
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 20 && listView.getLastVisiblePosition() + 1 == totalItemCount) {

                loadDataFromDB(limit, offset);
            }

        }
    };


}


