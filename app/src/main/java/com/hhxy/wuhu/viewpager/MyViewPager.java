package com.hhxy.wuhu.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jude.rollviewpager.RollPagerView;

/**
 * Created by Administrator on 2017/5/5.
 */
//重写viewpager，阻止父控件拦截子空间事件
public class MyViewPager extends RollPagerView {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
//      重写这个函数没有什么卵用
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
