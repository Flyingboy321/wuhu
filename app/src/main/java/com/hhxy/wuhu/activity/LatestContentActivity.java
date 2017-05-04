package com.hhxy.wuhu.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhxy.wuhu.R;
import com.hhxy.wuhu.db.WebCacheDbHelper;
import com.hhxy.wuhu.model.Content;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.FoundWebView;
import com.hhxy.wuhu.util.HttpUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class LatestContentActivity extends AppCompatActivity implements FoundWebView.OnScrollInterface {
    private static final String TAG = "LatestContentActivity";
    private int id;
    private String idNews;
    private WebView mwebView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView imageView;
    private FloatingActionButton fab;
    private String newsURL;
    private String shareUrl;
    private Content content;
    private WebCacheDbHelper webCacheDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
//        创建数据库帮助类对象
        webCacheDbHelper = new WebCacheDbHelper(this,1);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        找到我们的webview
        mwebView = (WebView) findViewById(R.id.web_view);
        final NestedScrollView nestedScrollview = (NestedScrollView) findViewById(R.id.nestedScrollView);
//          这是对我们的weview设置的
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mwebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mwebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.getSettings().setJavaScriptEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.GONE);
               nestedScrollview.scrollTo(0,0);
            }
        });



        nestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e(TAG, "onScrollChange: 这里监听到nestedscrollview的滑动"+scrollX+"==="+scrollY+"==="+oldScrollX+"==="+oldScrollY);
                Log.e(TAG, "onScrollChange: "+nestedScrollview.getChildAt(0).getHeight()+nestedScrollview.getHeight());
                if (scrollY<(nestedScrollview.getChildAt(0).getHeight()-nestedScrollview.getHeight())/2){
                    fab.setVisibility(View.GONE);
                }
                else{
//                    这里在xml中通过gone属性并不能是floatingactionbutton消失，只能指定一个透明度是0，来隐藏掉按钮了
                    fab.setVisibility(View.VISIBLE);
                    fab.setAlpha(0.8f);
                }
            }
        });
        Intent intent = getIntent();
        id = intent.getIntExtra("newsID",0);
        idNews = String.valueOf(id);
        imageView = (ImageView) findViewById(R.id.iv_title);
        imageView.setImageResource(R.mipmap.dream);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("这个是toolbar");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                添加activity切换时候的动画
                overridePendingTransition(0,R.anim.slide_out_to_left_from_right);
            }
        });


//        TextView textView = (TextView) findViewById(R.id.tv_content);
//        textView.setText(idNews +"这是我们的轮播条传过来的id");
//        我们既然拿到了我们轮播条传来的新闻id下面就是来请求id数据对应的json数据了

        loadContentNews();
    }

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
                    newsURL = Constent.BASEURL+Constent.CONTENT+idNews;
//                Toast.makeText(LatestContentActivity.this, ""+Constent.BASEURL+Constent.CONTENT+idNews, Toast.LENGTH_SHORT).show();
//                不过我们解析时，首先要创建我们的javaBean类
//                responseString = responseString.replaceAll("", " ");
//                    将获得的数据存储到数据库中,建表，并获得数据库对象
                    SQLiteDatabase writableDatabase = webCacheDbHelper.getWritableDatabase();
//                    将以前的数据替换,成功替换
                    writableDatabase.execSQL("replace into Cache(newsId,json) values(" + idNews + ",'" + responseString + "')");
//                    关闭数据库
                    writableDatabase.close();
                    paseJson(responseString);

                }
            });
        }else{
//            这里是表示没有网络连接，直接从数据库中读取
//            查询数据库得到json数据
            readFromSQLite();
        }

    }

//    方法从数据库中读取数据
    public Cursor readFromSQLite(){
        SQLiteDatabase readableDatabase = webCacheDbHelper.getReadableDatabase();
//            查询所有的id是当前消息的数据然后返回
        Cursor cursor = readableDatabase.rawQuery("select * from Cache where newsId = " + idNews, null);
        if (cursor.moveToFirst()){
            String json = cursor.getString(cursor.getColumnIndex("json"));
            paseJson(json);
        }
        cursor.close();
        readableDatabase.close();
        return cursor;

    }

    private void paseJson(String responseString) {
//        现在来解析我们的java数据
        Gson gson = new Gson();
        content = gson.fromJson(responseString,Content.class);
        shareUrl = content.getShare_url();
        collapsingToolbarLayout.setTitle(content.getTitle());

        Picasso.with(this).load(content.getImage()).into(imageView);
//        final ImageLoader imageloader = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//        imageloader.displayImage(content.getImage(), imageView, options);

        Log.e("这是我们的tostring我们的消息的内容", content.toString());
        String html = content.getBody();
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html2 = "<html><head>" + css + "</head><body>" + html + "</body></html>";
        html2 = html2.replace("<div class=\"img-place-holder\">", "");

        mwebView.loadDataWithBaseURL(null,html2,"text/html","UTF-8",null);

        Log.e("这个是我们的css文件打印", content.getCss().toString());

    }
    @Override
    public void onSChanged(int i, int t, int oldi, int oldt) {
        Log.e(TAG, "onSChanged: 这是在回调接口中打印的"+i+"====="+t+"==="+oldi+"==="+oldt);

    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.slide_out_to_left_from_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_share:
                showShare();
//                Toast.makeText(this, "你单击的分享按钮", Toast.LENGTH_SHORT).show();
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
