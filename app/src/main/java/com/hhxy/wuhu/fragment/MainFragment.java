package com.hhxy.wuhu.fragment;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.LatestContentActivity;
import com.hhxy.wuhu.activity.MainActivity;
import com.hhxy.wuhu.adapter.MainNewsItemAdapter;
import com.hhxy.wuhu.adapter.TestLoopAdapter;
import com.hhxy.wuhu.log.LogE;
import com.hhxy.wuhu.model.Before;
import com.hhxy.wuhu.model.Latest;
import com.hhxy.wuhu.model.StoriesBean;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.HttpUtils;
import com.hhxy.wuhu.util.ProUtils;
import com.hhxy.wuhu.viewpager.MyViewPager;
import com.jude.rollviewpager.RollPagerView;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

//但我们实现我们的加载当前数据 和刷新数据后，之后我们要做的就是加载更多了
//    http://news-at.zhihu.com/api/4/news/before/加我们的日期，这样就能够获得我们的 这个日期之前的消息了，
//    下面我们来在写一个方法。
//    现在我们实现了加载跟多的，下一步就是我们的侧边栏点击了，这个时候我们要切换我们的fragment了

public class MainFragment extends BaseFragment {
    private ListView lv_news;
    private Latest latest;
    private MainNewsItemAdapter mainNewsItemAdapter;
    private Before before;
    private String date;
    private boolean isLoading = false;
    private View rollViewPagerLayout;
    private MyViewPager rollPagerView;
    private TestLoopAdapter testLoopAdapter;
    private Handler handler = new Handler();


//    这个方法就是我们必须实现的，在oncreatview（）方法中我们会调用这个方法
//    第一步：我们要写入我们的Fragment布局文件 然后填充返回，

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//          这里我们在加载数据的时候首先改变toolbar的显示
        ((MainActivity)mActivity).getSupportActionBar().setTitle("今日热闻");
//        我们的加载的view就是一个布局文件为listview的东西。
//        这里为什么false就好了？？？？
        View view = inflater.inflate(R.layout.main_news_layout,container,false);
        lv_news = (ListView) view.findViewById(R.id.lv_news);
//        创建我们的adapt对象
        mainNewsItemAdapter = new MainNewsItemAdapter(mActivity);
//        不过我们的数据来源是我们的Latest类
//        lv_news.setAdapter(mainNewsItemAdapter);

//        下面来填充我们的轮播条
        rollViewPagerLayout = LayoutInflater.from(mActivity).inflate(R.layout.roll_viewpager_layout,
                lv_news,false);
//        初始化我们的rollviewpager
        initRollViewPager();
//        创建我们的adapt对象
        testLoopAdapter = new TestLoopAdapter(rollPagerView);

//         注意我们不能在这里添加我们的头布局
//        添加头布局
//        lv_news.addHeaderView(rollViewPagerLayout);
//        到这里我们的头布局就添加成功了，下面来完成我们的轮播条，
//         我们需要在我们的数据请求成功后来完成轮播条的显示
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                这个方法就能够监听我么的滑动事件，其中第二个参数是我们第一个可见的条目的位置
//                 ，第二个是可见条目的个数，第三个总共的条目个数
//                这里当我们可见的元素位子是第一个的时候，且，距离上边界为0dp时我们才能滑动就好了

//                现在我们就要做我们的加载更多的逻辑了，我们监听我们的滑动事件，但我们显示的条目是我们
//                集合中的最后一个的时候我们请求before数据，同时我们在得到数据后将数据添加到
//                adapt集合中，这时通知我们的adapt重新加载就好了

                if (lv_news != null && lv_news.getChildCount() > 0){
//                    注意这里不能用Paddingtop   要用top  不然在第一个的时候就能被拦截

//                    boolean enable = firstVisibleItem == 0&&view.getChildAt(firstVisibleItem).getPaddingTop() ==0;
                    boolean enable = firstVisibleItem == 0&&view.getChildAt(firstVisibleItem).getTop() ==0;

//                    这里我们发现直接调用我们的额mactivity是无法获得我们的刷新组件的，因为我们的sr是provite
//                    的，所以我们肯定要在我们的activity总构建一个方法，用来控制我们的sr
                        ((MainActivity)mActivity).setSwipeRefreshEnable(enable);
//                    Log.e("这个是检测我们的额刷新是否能用",""+enable);
//                    此时我们判断我们可见的第一个条目的位置加上我们可见的条目的数量，这样就得到了我们总共加载的条目了
//                    如果我们这个值与我们的总条数相等的话，我们就到底部了，这时候加载跟多
//                      表示当他不是正在加载的时候  且显示的是在底部的时候才去相应下载更多
//                    不过当我们在加载的时候我们要置标志位  islOading 为   true，，这样我们的滑动中才不会再次加载
//                    因为如果我们还在加载的时候我们在滑，此时我们的date不变，等于我们请求同样的数据多次，
//                    只有当我们的数据加载完成时，也就说我们的date改变时，我们才去加载more
                    if ((firstVisibleItem+visibleItemCount == totalItemCount)&&!isLoading){
//                        注意一个完整的url是http://news-at.zhihu.com/api/4/news/before/加我们的日期
//                        不过我们的get中封装了http://news-at.zhihu.com/api/4/
//                        而我们的constents中也有了 public static final String BEFORE = "news/before/";
                        Log.e("这是在我们加载跟多的时候if中","表示我们执行到了这里");
//                        所以这里我们只用加上我们的日期就好了
                        loadMore(Constent.BEFORE+date);
                    }
                }
            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                返回的是一个新闻条目
                StoriesBean storiesBean = (StoriesBean) parent.getAdapter().getItem(position);
                String newsId = storiesBean.getId()+"";
                //                    这里将我们的newsId存到偏好文件中
//                    首先获得偏好文件类容,第一次访问的时候什么都没有返回空字符
                String readSequence = ProUtils.getStringFromDefault(getContext(),"read","");
//                    判断时候包含当前点击的news若果不到含就将当前的数据存储到偏好中
                if (!readSequence.contains((newsId))){
                    readSequence = readSequence+newsId+",";
                }
//                    将数据存储到文件中
                ProUtils.putStringToDefault(getContext(),"read",readSequence);


                Intent intent = new Intent(getContext(), LatestContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("newsID",storiesBean.getId());
                intent.putExtras(bundle);

                TextView textView = (TextView) view.findViewById(R.id.tv_title);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                getContext().startActivity(intent);
            }
        });

        return view;
    }
//重写我们的初始化数据方法，用来加载数据，注意这个方法会在我们的activity创建好了后调用

    @Override
    protected void initDate() {
        super.initDate();
//        这里我们要用网络来请求我们的json数据然后解析，我们定义一个方法来做这些；loadFirst（）
        loadFirst();
    }

    private void loadFirst() {
//        这个时候我们来请求网络数据，通过我们的android-http-async  来做我们传入我们请求的首页的url
//        不过这里我们返回的是一个文本信息，一个json数据。
//        此时正在加载数据
        isLoading = true;
        HttpUtils.get("news/latest", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                LogE.E("这个是在loadFirst中失败的请求");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("这个是我们请求首页的json数据",responseString);
//                这里我们的数据有请求出来了，所以下一步解析我们的数据
//                同样我们在写一个方法来解析我们的数据
                parseLatestJson(responseString);


            }
        });

    }
     private void loadMore(String urlBefore){
         isLoading = true;
//         下面我们传入我们的url，然后进行网络请求，最后解析
         HttpUtils.get(urlBefore, new TextHttpResponseHandler() {
             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

             }

             @Override
             public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                 这里如果成功了，我们的过往消息就会以json字符串的格式返回，下面我们要再次解析我们的beforejson
//                    我们发现我们的before中只有两个字段，一是日期，一个就是我们的新闻条目，所以我们可以用我们
//                 前面抽取出来的新闻消息对象,通过打印这里也可以执行，
                 Log.e("这是我们的before新闻json",responseString);
                 parseBeforeJson(responseString);

             }
         });

     }
    //    现在我们就来定义一个新的方法来请求我们的过往新闻
    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
//        这个时候我们就拿到了我们的过往新闻对象了
        before = gson.fromJson(responseString,Before.class);
//        这时我们跟新我们的date
        date = before.getDate();
//        我们先log一下我们的过往新闻消息，这里我们成功的打印出来了我们的过往消息新闻
        Log.e("这个是我们的过往消息的toString 打印",before.toString());
//        现在就要把我们的before中包含新闻对象的集合传到我们的adapt中，然后跟新就好了
        List<StoriesBean> storiesBeen = before.getStories();
//        现在要把这个传到我们的adapt中 注意我们的adapt中是添加了这个集合的所有元素，并没有替换，这样我们
//        的新闻才是连续的
//      做到这里我们发现我们的消息能够显示了，不过我们总是显示的是头一天了，我们要更新我们的date才行
        mainNewsItemAdapter.addList(storiesBeen);
        Toast.makeText(mActivity,date,Toast.LENGTH_SHORT).show();
        isLoading = false;

    }


    private void parseLatestJson(String responseString) {
//        现在来解析数据
        Gson gson = new Gson();
//        我们用Gson解析的时候只用传入我们的class 然后就会返回我们的bean对象
//        注意这里后面传入的是我们的类名。class
        latest  = gson.fromJson(responseString,Latest.class);

//        因为我们要用到过往消息的新闻，所以我们要取出我们的date数据
        date = latest.getDate();
//        现在我们要拿到我们的新闻轮播条消息集合
        List<Latest.TopStoriesBean> topStories= latest.getTop_stories();

//        现在我们要想办法将这个数据传到我们的LoopPagerAdapter实现类中
//        这是我们的adapter中就有数据了
        testLoopAdapter.setTopStories(topStories,mActivity);
//        这个时候我们的adapt中就有数据了，这是我们setadapt
//          这下面一行代码会阻塞主线程  导致anr 异常
//        rollPagerView.setAdapter(testLoopAdapter);

//        我们还要注意这里需要重写toString方法
        Log.e("这个是我们请求首页的json数据",latest.toString());
//         现在我们的到了我们新闻首页的对象了，下一步就是要将这个数据显示在listview中，
//        现在的到我们的新闻对象，得到我们的新闻对象的集合
         List<StoriesBean> storiesBeen =latest.getStories();
//        这个时候我们怎么才能将这个数据传到我们的adapt中呢，我们应该在adapt中暴露一个方法
        StoriesBean storiesBeanTitle = new StoriesBean();
        storiesBeanTitle.setTitle("今日新闻");
        storiesBeanTitle.setType(123);
//        storiesBeen.add(0,storiesBeanTitle);

        mainNewsItemAdapter.addList(storiesBeen);
//        rollPagerView.setAdapter(testLoopAdapter);

        handler.post(new Runnable() {
            @Override
            public void run() {
//                rollPagerView.setAdapter(testLoopAdapter);
//                为什么要在这里添加我们的head布局就好了、、
//                为什么这里会出现oom异常
                lv_news.addHeaderView(rollViewPagerLayout);
                rollPagerView.setAdapter(testLoopAdapter);
                lv_news.setAdapter(mainNewsItemAdapter);
            }
        });

//        数据加载完成
        isLoading = false;
    }
//    下面在写一个方法来初始化我们的轮播条数据的
    private void initRollViewPager(){
//        找到我们的rollpagerview对象
        rollPagerView = (MyViewPager) rollViewPagerLayout.findViewById(R.id.RPV);
        rollPagerView.setPlayDelay(2000);
        rollPagerView.setAnimationDurtion(800);
    }


}
