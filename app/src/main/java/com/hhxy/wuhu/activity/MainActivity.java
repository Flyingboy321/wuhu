package com.hhxy.wuhu.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.fragment.MainFragment;
import com.hhxy.wuhu.fragment.MenuFragment;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout sr;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private long firstTime;
    private FrameLayout frameLayout;
    private MenuFragment menufragment;

    //    现在解决我们不能滑动的问题,我们看到我们可以是我们的sr.setEnabled(enable);这个
//    方法来作用我们的滑动是否能用，不过我们的sr是在我们的activity中的，我们的滑动时在fragment中的，拿不到，
//    但是我们在我们的fragment可以拿到activity，这样我们在listview滑动时就可以监听了
//    现在刷新的功能就完成了，下一步就是做出我们的主页面的toolbar的显示内容了
//    这个也是在初始化view中做的。
//    现在我们为我们的toolbar添加menu项  重写两个方法，一个是填充我们的menu
//    一个是监听我们的点击事件
//    下一步：完成我们的加载更多，这个数据肯定是在我们的fragment中完成的，我们回到fragment中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.fl_content);
//        获得menuFragment对象
        menufragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);
//
//        我们刚开始的时候肯定要初始化我们activity中的数据，其中包括我们的toolbar显示的问题
//        所以我们写入一个新的方法来初始化数据
        initView();

//        我们将我们的mainfragment 填充到我们的framelayout 中
//        我们继续封装一个方法来做这些
//        不过当我们点击侧边栏的时候我们也要切换我们的fragment了，所以这时我们要在写一个fragment了，
//        作为我们的新闻资讯显示的内容了

        loadLatest();
//        这里会引起内存溢出,所以直接不调用这个方法
//        initBmob();

    }

    //    执行完初始胡bmob后会造成内存泄露什么鬼？
    public void initBmob() {

//        初始化推送功能
//        初始化
        Bmob.initialize(this, "d71587be19e32ff96f90f64cafe2a799");
        BmobInstallation.getCurrentInstallation().save();
        BmobPush.startWork(this);

    }

    //首先创建一个菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        这里不能这么用
//        LayoutInflater.from(MainActivity.this).inflate(R.menu.menu_main,menu);
        return true;
    }

    //    下面对menu点击事件监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        我们得到我们点击的菜单的id
        int id = item.getItemId();
        if (id == R.id.action_mode) {
            Toast.makeText(this, "白天或黑夜设置", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_set) {
            Toast.makeText(this, "设置打开", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
//        找到我们的toobar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        找到我们的DrawLayout布局
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
//        加上这一句就出现了wuhu的标志，貌似不加这一个我们的toolbar才能正常显示
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("这是新的");
//        toolbar.setTitle("首页");

        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close);//这里显示的是两个知乎日报
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


//        首先解决我们不能滑动的事件，我们要先找到我们的swipereFreshLayout
        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
//        sr.setColorSchemeResources();
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                现在我们每次刷新的时候就是再次加载数据。。刚开始我们读取了一次，刷新一次，读取一次
                loadLatest();
//                刷新的同时，同时将侧边栏刷新
                menufragment.getItems();
//                这句话的意思是刷新完成后，隐藏我们的进度条，不然一直转
                sr.setRefreshing(false);
            }
        });

    }

    //在这个方法中我们替换我们的fragment到我们的布局中
    public void loadLatest() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, new MainFragment(), "latest").commit();
    }

    public void setSwipeRefreshEnable(boolean enable) {
        sr.setEnabled(enable);
    }

    public void setToolbarTitle(String s) {
        toolbar.setTitle(s);

    }

    public void closeMenu() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
//        这里不能直接继承我们的父类的方法
//        super.onBackPressed();
//        头一次点击我们获取点击时间
        long seconTime = System.currentTimeMillis();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            这里判断如果我们的侧边栏在打开状态的话，就关闭
            closeMenu();
//            头一次进来的时候我们的secondtime是远远大于Firsttime的，也就是这里满足，我们要弹出通知，
//            同时我们将我们的系统值赋值给Firsttime
//            当我们下次在次点击，我们同样或的了系统时间，这样去我们两次点击的时间差，如果满足小于2000
//            就走else道路
        } else {
            if (seconTime - firstTime > 2000) {
                firstTime = seconTime;
                Snackbar sb = Snackbar.make(frameLayout, "继续点击退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sb.show();
            } else {
                finish();
            }
        }
    }

}
