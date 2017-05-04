package com.hhxy.wuhu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/7.
 */
//这个是我们的基本Fragment，我们只用继承这个就好了，提高了代码的复用性。
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        这里我们要返回我们自己的view对象，不过我么定义的是一个抽象类，让子类来实现方法返回view即可
//        获得我们的activity对象
        mActivity = getActivity();
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        当我们的Fragment销毁的时候同时将我们的mActivity引用释放
        mActivity = null;
    }
//    同样我们的Fragment也要初始化数据,这个方法等到我们的activity创建好了再调用
    protected void initDate(){

    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState);

}
