package com.hhxy.wuhu.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by Administrator on 2016/11/30.
 */
//因为我们经常用到网络请求，所以这里我们进行封装
//    通过静态的方法直接进行封装

public class HttpUtils {
//这里我们用无参构造方法
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandlerInterface){
        if (url == "http://news-at.zhihu.com/api/7/prefetch-launch-images/1080*1920"){
            client.get(url,responseHandlerInterface);

        }else{
            client.get(Constent.BASEURL+url,responseHandlerInterface);
        }

    }
    public static void get2(String url, ResponseHandlerInterface responseHandlerInterface){
        client.get(url,responseHandlerInterface);
    }
//    定义一个类来判断是否有网络连接
    public static boolean isNetworkConnected(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
//        获得网络连接信息
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!= null){
//            得到网络信息下面 判断是否有网络连接 是否是available 能找到的
            return networkInfo.isAvailable();

        }
        return false;
    }


}
