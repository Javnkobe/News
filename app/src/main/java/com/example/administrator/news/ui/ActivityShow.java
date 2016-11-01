package com.example.administrator.news.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.administrator.news.R;
import com.example.administrator.news.base.MyBaseActivity;
import com.example.administrator.news.model_entity.News;

/**
 * 单击后的详细页面
 * @author Administrator
 *
 */
public class ActivityShow extends MyBaseActivity {
    /**加载网页的进度条*/
    private ProgressBar progressBar;
    /**网页*/
    private WebView webView;
    /**当前的新闻*/
    private News news;
    /**返回按钮*/
    private ImageButton imgRtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        webView=(WebView) findViewById(R.id.webView1);
        imgRtn = (ImageButton) findViewById(R.id.title_bar);
        imgRtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        news=(News) getIntent().getSerializableExtra("news");
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //设置加载全部后的监听
        webView.setWebViewClient(viewclient);
        //设置当加载时的监听
        webView.setWebChromeClient(chromeClient);
        //设置路径
        webView.loadUrl(news.getLink());
    }
    private WebViewClient viewclient=new WebViewClient(){
        //在点击请求的是链接是才会调用，重写此方法返回 true表明点击网页里面的链接还是在当前的 webview里跳转，不跳到浏览器那边。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
    };
    /**
     * 进度条的使用,根据进度
     */
    private WebChromeClient chromeClient=new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            progressBar.setProgress(newProgress);
            if(progressBar.getProgress()==100){
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}
