package com.feicui.edu.highpart.util;

import android.content.Context;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class DensityUtil {
    /**
     * 根据手机分辨率从dp转换为px
     */
    public static  int dip2px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机分辨率从dp转换为px
     */
    public static  int px2dip(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
