package com.hhxy.wuhu.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhxy.wuhu.R;
import com.hhxy.wuhu.db.WebCacheDbHelper;
import com.hhxy.wuhu.model.Content;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.HttpUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.widget.Toast.LENGTH_SHORT;

//这里我们创建我们的主题新闻内容的activity
public class NewsContentActivity extends AppCompatActivity {

    private static final String TAG = "这是在newscontent中打印" ;
    private Toolbar toolbar;
    private int idNews;
    private WebView webView;
    private FloatingActionButton fab;
    private NestedScrollView nestedScrollView;
    private WebCacheDbHelper webCacheDbHelper;
    private Content content;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        webCacheDbHelper = new WebCacheDbHelper(this,1);
        nestedScrollView = (NestedScrollView) findViewById(R.id.news_NestedScrollView);
        Intent intent = getIntent();
        idNews = intent.getIntExtra("newsID",0);
        toolbar = (Toolbar) findViewById(R.id.toobar2);
        webView = (WebView) findViewById(R.id.webview_news);
        fab = (FloatingActionButton) findViewById(R.id.news_content_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.scrollTo(0,0);
                Log.e(TAG, "onClick: "+"点击了按钮");
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e(TAG, "onScrollChange: 这里监听到nestedscrollview的滑动"+scrollX+"==="+scrollY+"==="+oldScrollX+"==="+oldScrollY);
                Log.e(TAG, "onScrollChange: "+ nestedScrollView.getChildAt(0).getHeight()+ nestedScrollView.getHeight());
                if (scrollY<(nestedScrollView.getChildAt(0).getHeight()- nestedScrollView.getHeight())/2){
                    fab.setVisibility(View.GONE);
                }
                else{
//                    这里在xml中通过gone属性并不能是floatingactionbutton消失，只能指定一个透明度是0，来隐藏掉按钮了
                    fab.setVisibility(View.VISIBLE);
                    fab.setAlpha(0.8f);
                }
            }
        });


//        这是对我们的weview设置的
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);


        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        点击退出我们的activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,R.anim.slide_out_to_left);
            }
        });



//        下面同样来获得我们的数据
        loadContentNews();
    }

//    private void loadContentNews() {
//        HttpUtils.get(Constent.CONTENT+idNews, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//            }
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
////                这个时候我们的数据请求成功了下面就是来解析我们的数据了
//                paserJson(responseString);
//            }
//        });
//    }
//          实现数据的缓存
    private void loadContentNews() {
//        这里判断网络是否连接，如果有网络连接就请求数据，并保存到数据库中，如果没有网络连接就从数据库中读取
        if (HttpUtils.isNetworkConnected(this)){
//            这里表示有网络连接
            HttpUtils.get(Constent.CONTENT+idNews, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                这里我们先打印我们的数据

                    Log.e("这个是我们新闻内容的打印",responseString);
//                这下面是新闻的完整的URL
                    String newsURL = Constent.BASEURL + Constent.CONTENT + idNews;
//                Toast.makeText(LatestContentActivity.this, ""+Constent.BASEURL+Constent.CONTENT+idNews, Toast.LENGTH_SHORT).show();
//                不过我们解析时，首先要创建我们的javaBean类
//                responseString = responseString.replaceAll("", " ");
//                    将获得的数据存储到数据库中,建表，并获得数据库对象
                    SQLiteDatabase writableDatabase = webCacheDbHelper.getWritableDatabase();
//                    将以前的数据替换,成功替换
                    writableDatabase.execSQL("replace into Cache(newsId,json) values(" + idNews + ",'" + responseString + "')");
//                    关闭数据库
                    writableDatabase.close();
                    paserJson(responseString);

                }
            });
        }else{
//            这里是表示没有网络连接，直接从数据库中读取
//            查询数据库得到json数据
            SQLiteDatabase readableDatabase = webCacheDbHelper.getReadableDatabase();
//            查询所有的id是当前消息的数据然后返回
            Cursor cursor = readableDatabase.rawQuery("select * from Cache where newsId = " + idNews, null);
            if (cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                paserJson(json);
            }
            cursor.close();
            readableDatabase.close();
        }

    }



    private void paserJson(String responseString) {
        Gson gson = new Gson();
        content = gson.fromJson(responseString,Content.class);
        shareUrl = content.getShare_url();
//        拿到我们的新闻对象，下面就是webview的显示了
        toolbar.setTitle(content.getTitle());
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = content.getBody();
        String html2 = "<html><head>" + css + "</head><body>" + html + "</body></html>";

        html2 = html2.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL(null,html2,"text/html","UTF-8",null);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.slide_out_to_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        将分享按钮填充到toobar中
        getMenuInflater().inflate(R.menu.toobar,menu);
        return super.onCreateOptionsMenu(menu);
    }
//  对菜单项进行监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_share:
                showShare();

                break;
            default:
                break;

        }
        return true;
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
//        oks.setTitle("标题");
        oks.setTitle("");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content.getTitle());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareUrl);

// 启动分享GUI
        oks.show(this);
    }

}
