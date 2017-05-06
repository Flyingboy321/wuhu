package com.hhxy.wuhu.swiperefreshlayout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/5/5.
 */

public class VpSwipeRefreshLayout extends SwipeRefreshLayout {
    private float startY;
    private float startX;    // 记录viewPager是否拖拽的标记
    private boolean mIsVpDragger;

    public VpSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
//                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
//                if (mIsVpDragger) {
//                    return false;
//                }
                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX >= distanceY) {
//                    mIsVpDragger = true;
//                    如果这里横向的滑动距离大于竖向的滑动距离不拦截viewpager的滑动事件。世界返回false
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
//                mIsVpDragger = false;
                break;
                  }
            // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
//              程序执行到这里表示在判断移动的case中没有返回false，这里表示竖向的距离大于横向的距离
//        表示可以拦截交给父类的拦截事件分析
            return super.onInterceptTouchEvent(ev);
        }

}
