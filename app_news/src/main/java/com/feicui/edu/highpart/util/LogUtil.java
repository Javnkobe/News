package com.feicui.edu.highpart.util;

import android.util.Log;

/**
 * 日志管理
 */
public class LogUtil {
    public static final String TAG = "新闻随意看";
    //调试日志的开关
    public static boolean isDebug = true;

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }
    public static void d(String msg) {
        if (isDebug)
            Log.d(LogUtil.TAG,msg);
    }
    public static class CommonUtil {
        /** 联网的 ip */
        public static String NETIP = "118.244.212.82";
        /** 联网的路径 */
        public static String NETPATH = "http://" + NETIP + ":9092/newsClient";
        /** SharedPreferences保存用户名键 */
        public static final String SHARE_USER_NAME = "userName";
        /** SharedPreferences保存用户名密码 */
        public static final String SHARE_USER_PWD = "userPwd";
        /** SharedPreferences保存是否第一次登陆 */
        public static final String SHARE_IS_FIRST_RUN = "isFirstRun";
        /** SharedPreferences存储路径 */
        public static final String SHAREPATH = "news_share";
    }
}


