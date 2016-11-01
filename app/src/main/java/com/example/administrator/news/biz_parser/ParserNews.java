package com.example.administrator.news.biz_parser;

import android.content.Context;

import com.example.administrator.news.model_entity.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * json
 * 数据解析
 */
public class ParserNews {
    private Context context;
    public ParserNews(Context context){
        this.context=context;
    }
    //解析新闻数据
    public ArrayList<News> parser(JSONObject jsonObject) throws Exception{
        ArrayList<News> newsLit=new ArrayList<News>();
        //根据数据块名称获取一个数组
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        //循环遍历这个数组
        for (int i = 0; i <jsonArray.length(); i++) {
            JSONObject object=jsonArray.getJSONObject(i);
            int nid = object.getInt("nid");
            String title = object.getString("title");
            String summary = object.getString("summary");
            String icon = object.getString("icon");
            String link = object.getString("link");
            int type = object.getInt("type");
            News news=new News(nid, title, summary, icon, link,type);
            newsLit.add(news);
        }
        return newsLit;
    }
}
