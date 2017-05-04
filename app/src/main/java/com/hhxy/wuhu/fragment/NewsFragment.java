package com.hhxy.wuhu.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.MainActivity;
import com.hhxy.wuhu.adapter.NewsItemAdapt;
import com.hhxy.wuhu.model.News;
import com.hhxy.wuhu.model.StoriesBean;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.HttpUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10.
 */
//刚开始我们只是在我们的Fragment总添加了一个按钮，
//    我们先测试我们的Fragment显示，，即侧边栏的点击事件

//    下一步：我们注意到知乎日报的主题栏的上方有一个图片，显示我们现在就
//    为我们的listview添加一个头布局
//    首先我们要做的是用布局文件填充view对象 我们的布局就是news_header.xml 文件中间是一个帧布局，里面嵌套
//    imageview+textview



//这个是什么意思，这个是忽略不用默认的构造方法的警告
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment {
    private ListView lv_news;
    private String urlId;
    private String title;
    private NewsItemAdapt newsItemAdapt;
    private TextView tv_title;
    private ImageView iv_title;
    private ImageLoader imageLoader;


    //    这里我们要创造我们自己的布局文件，然后填充
//    不过我们现在这里也是一个listview
    public NewsFragment(String urlId,String title){
//        注意我们不能在这里弹吐司，，我们的Context对象这时候可能还没有
//        这时候我们已经拿到了主题新闻的url 了，下面我们就可以来请求数据了
//        我们注意到我们的主题新闻是没有跟多的。所以我们只用写一个网络请求就好了
        this.urlId = urlId;
        this.title = title;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout,container,false);//这里传入false 有什么用？
//        找到我们的listview
        lv_news = (ListView) view.findViewById(R.id.lv_news);
//        我们既然要加载我们的头布局，并填充，我们坑定用imageloader来做，所以我们先创造对象
//        不过我们肯定在我们解析完数据的时候来请求我们的图片，和标题
        imageLoader = ImageLoader.getInstance();
//        下面加载我们的头布局文件
        View header = LayoutInflater.from(mActivity).inflate(R.layout.news_header,lv_news,false);
//        找到我们的imageview和textview
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        iv_title = (ImageView) header.findViewById(R.id.iv_title);
//        将我们的header添加到listview头部
        lv_news.addHeaderView(header);
//        我们在加载我们的Fragment的时候同时要对我们的header中的元素赋值


//        同时我们要改变我们的toolbar显示的标题
        ((MainActivity)mActivity).getSupportActionBar().setTitle(title);


//        这里同样需要我们创造我们自己的adapt 不过我们在请求导数据的时候才去set
//        这里我们要处理我们的滑动事件，
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//              在滑动的时候我们来判断我们的显示的条目是否处在第一位，如果是的话我们才去刷新，如果不是我们就
//                禁用掉刷新
                if (lv_news !=null&&lv_news.getChildCount()!=0){
                    boolean enable =
                            (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop()==0);
                    ((MainActivity)mActivity).setSwipeRefreshEnable(enable);


                }

            }
        });


        return view;
    }
//         这里我们初始化我们的数据，不过我们
    @Override
    protected void initDate() {
        super.initDate();
                Toast.makeText(mActivity,urlId+title,Toast.LENGTH_SHORT).show();
        loadNews();


    }
//    我们写一个方法来请求我们的网络数据
//    这个方法在我们初始化我们的Fragment时调用
    public void loadNews(){
        HttpUtils.get(Constent.THEMENEWS + urlId, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                这里我们请求成功后打印我们的主题新闻
//              哈哈  到这一步 我们已经能够拿到我们的主题新闻的数据了，下面来解析
                Log.e("这是在我们的主题新闻中获得的主题新闻",responseString);
                parseJson(responseString);

            }
        });
    }

    private void parseJson(String responseString) {
//        现在我们来解析我们的json数据,首先我们要创建我们的javaBean同过GsonFormat

        Gson gson =  new Gson();
        News news = gson.fromJson(responseString,News.class);
//        在我们的主题日报返回数据的时候，有这么几个字段
//        description: "内容由知乎用户推荐，海纳主题百万，趣味上天入地",这个就是我们的标题
//        image: "http://pic1.zhimg.com/153c4cb468b766a8eea35fcab05c3da5.jpg",这个就是我们的图片
//        stories：【】  这个是我们的主题新闻对象集合，
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
//        下面设置我们的显示内容
        tv_title.setText(news.getDescription());
        imageLoader.displayImage(news.getImage(),iv_title,options);


//        现在我们log到我们的控制台看看
        Log.e("这是我们解析后的数据打印，主题列表新闻",news.toString());
//        走到这里我们的javaBean就搞好了，下一步就是通过构造将数据传给我们的adapt了，
        List<StoriesBean> storiesBeen = news.getStories();
//        创建我们的adapt 这里将我们的Context对象，和我们的主题新闻对象数组都传过去
//        现在我们的adapt中就有数据了，我们就能够来setadapt了，
        newsItemAdapt = new NewsItemAdapt(mActivity,storiesBeen);

        lv_news.setAdapter(newsItemAdapt);


    }
}
