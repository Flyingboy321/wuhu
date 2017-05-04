package com.hhxy.wuhu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/5/2.
 */
//自定义webview
public class FoundWebView extends WebView {
//    实现构造方法
    public FoundWebView(Context context) {
        super(context);
    }

    public FoundWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoundWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//写回调监听
//    重写滑动状态改变方法


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.e(TAG, "onScrollChanged:+这是在webview中打印的 ");
        onScrollInterface.onSChanged(l,t,oldl,oldt);
    }
    public OnScrollInterface onScrollInterface;
//    定义方法接受回调实现类
    public void setOnScrollinterfaceListerner(OnScrollInterface onScrollinterfaceListerner){
        this.onScrollInterface = onScrollinterfaceListerner;
    }
//    定义回调接口
    public interface OnScrollInterface{
        public void onSChanged(int i,int t,int oldi,int oldt);
}
}
