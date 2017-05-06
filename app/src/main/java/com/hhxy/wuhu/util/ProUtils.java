package com.hhxy.wuhu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/5/6.
 */

public class ProUtils {
//    定义方法将数据存到Share_pre文件中
    public static void putStringToDefault(Context context,String key,String value){
//        获得sp对象
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        将数据提交到文件中
        sp.edit().putString(key,value).commit();
    }
//    重默认偏好文件中获得值
    public static String getStringFromDefault(Context context,String string,String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(string,value);
    }
}
