package com.example.administrator.news;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.administrator.news.biz_parser.ParserNews;
import com.example.administrator.news.common.HttpClientUtil;
import com.example.administrator.news.common.LogUtil;
import com.example.administrator.news.model_entity.News;

import java.util.List;

public class NewsTestCase extends AndroidTestCase {
    /**
     * 解析新闻数据，测试的是 parseNews()方法
     *//*
    public void testParserNews(){
        try {
            String path ="http://localhost:8080/newsClient/news_sort?ver=1&imei=1";
            //发送请求，得到返回数据
            String json = HttpClientUtil.httpGet(path);
            LogUtil.d(LogUtil.TAG,"新闻请求数据返回："+json);
            ParserNews parser = new ParserNews(getContext());
            List<News> mList = parser.parser(json);
            for (News news : mList) {
                Log.d("vivi", news.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
