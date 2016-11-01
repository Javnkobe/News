package com.feicui.edu.highpart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;


import com.feicui.edu.highpart.Adapter.LeadImgAdapter;
import com.feicui.edu.highpart.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *引导界面
 **/
public class LeadActivity extends MyBaseActivity {

    private ViewPager viewPager;
    private ImageView[] points=new ImageView[4];
    private LeadImgAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        SharedPreferences preferences =
                getSharedPreferences("lead_config", Context.MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (!isFirstRun) {
            openActivity(LogoActivity.class);
            finish();
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
        viewPager=(ViewPager) findViewById(R.id.viewpager);
    //设置每一个具体界面的样式
        List<View> viewList=new ArrayList<View>();
        viewList.add(getLayoutInflater().inflate(R.layout.lead_1, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_2, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_3, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_4, null));

        points[0]=(ImageView) findViewById(R.id.iv_p1);
        points[1]=(ImageView) findViewById(R.id.iv_p2);
        points[2]=(ImageView) findViewById(R.id.iv_p3);
        points[3]=(ImageView) findViewById(R.id.iv_p4);
        setPoint(0);
    //初始化控件
        viewPager=(ViewPager) findViewById(R.id.viewpager);
    //设置每一个具体界面的样式
        //初始化适配器
        adapter=new LeadImgAdapter(viewList);
        //设置适配器
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(listener);
    }
    private void setPoint(int index) {
        for (int i = 0; i < points.length; i++) {
            if(i==index){
                points[i].setAlpha(255);
            }else{
                points[i].setAlpha(100);
            }
        }
    }
    /**当界面切换后调用*/
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        /**
         * 当界面切换后调用
         */
        @Override
        public void onPageSelected(int arg0) {
            setPoint(arg0);
            if (arg0 >= 3) {
                openActivity(LogoActivity.class);
                finish();
            }
        }
        /**界面切换时调用*/
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2){

        }
        /**滑动状态变化时调用*/
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}




