package com.feicui.edu.highpart.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class SharedPreferenceUtil {
    public static void saveToken(Context context,String token){
        SharedPreferences preferences = context.getSharedPreferences("token",context.MODE_PRIVATE);
        preferences.edit().putString("token",token).apply();
    }
    public static String getToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences("token",context.MODE_PRIVATE);
        return preferences.getString("token","");
    }
    public static void saveAccount(Context context, String username,String pwd) {
        SharedPreferences preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        preferences.edit().putString("username", username).putString("password",pwd).apply();
    }

    public static String[] getAccount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        String[] account = new String[2];
        account[0] = preferences.getString("username", null);
        account[1] = preferences.getString("password", null);
        return account;
    }
    public static void saveHeader(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);
        preferences.edit().putString("header", username).apply();
    }

    public static String getHeader(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("header", Context.MODE_PRIVATE);
        return preferences.getString("header", null);
    }
}
