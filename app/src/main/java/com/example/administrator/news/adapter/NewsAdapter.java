package com.example.administrator.news.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.news.R;
import com.example.administrator.news.base.MyBaseAdapter;
import com.example.administrator.news.common.LoadImage;
import com.example.administrator.news.common.LogUtil;
import com.example.administrator.news.model_entity.News;

/**
 * 新闻数据适配器
 *
 * 1.holdview 绑定控件三个控件赋值图片为默认
 *
 * 2.图片处理
 */
public class NewsAdapter extends MyBaseAdapter<News> {

    /**
     * 加载图片之前默认图片
     */
    private Bitmap defaultBitmap;
    private ListView listview;
    //图片工具类
    private LoadImage loadImage;

    public NewsAdapter(Context c, ListView lv) {
        super(c);
        loadImage=new LoadImage(c, listener);
        defaultBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.cccc);
        listview=lv;
    }
    //回调的接口
    private LoadImage.ImageLoadListener listener=new LoadImage.ImageLoadListener() {
        /**
         *回调方法
         *@parambitmap请求回来的 bitmap
         *@paramurl图片请求地址
         */
        public void imageLoadOk(Bitmap bitmap, String url) {
            //类似于 findviewById得到每个 listview的图片通过异步加载显示图片
            ImageView iv=(ImageView) listview.findViewWithTag(url);
            LogUtil.d(url);
            if(iv!=null){
                LogUtil.d("异步加载得到图片的 url="+url);
                //加载图片
                iv.setImageBitmap(bitmap);
            }
        }
    };

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        HoldView holdView=null;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_list_news, null);
            holdView=new HoldView(convertView);
            convertView.setTag(holdView);
        }else{
            holdView=(HoldView) convertView.getTag();
        }
        News news=list.get(position);
        holdView.tv_title.setText(news.getTitle());
        holdView.tv_text.setText(news.getSummary());
        holdView.iv_list_image.setImageBitmap(defaultBitmap);//默认图片

        //1，得到图片的地址
        String uriImage=news.getIcon();
        //String s = uriImage.replace("localhost", "192.168.2.109");
        //给每个图片控件存入编号把图片的名字作为表示
        holdView.iv_list_image.setTag(LogUtil.CommonUtil.NETPATH+uriImage);
        //获取图片   1. 先从网络   2.如果已经下载过了存在本地文件中下次启动程序从文件读取  3.当程序不关闭再次进入该界面从内存中读取
        Bitmap bitmap=loadImage.getBitmap(uriImage);
        //如果不是网络的则马上加载可以是文件或内存
        if(bitmap!=null){
            holdView.iv_list_image.setImageBitmap(bitmap);
        }
        /***返回每一个子条目的视图*/
        return convertView;
    }

    /**标签类*/
    public class HoldView {
        public ImageView iv_list_image;
        public TextView tv_title;
        public TextView tv_text;
        public HoldView(View view) {
            iv_list_image = (ImageView) view.findViewById(R.id.imageView1);
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_text = (TextView) view.findViewById(R.id.textView2);
        }
    }

}

