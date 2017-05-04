package com.hhxy.wuhu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.log.LogE;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/30.
 */
//开发闪屏页面的activity
public class SplashActivity extends Activity {

    private ImageView iv_start;
    private File imgFile;
    //    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        iv_start = (ImageView) findViewById(R.id.iv_start);
//        这里我们要初始化我们的imageview  需要写一个方法
//        注意在这里源代码用的是一张固定的图片没有经过通过网络获取

        initImage();
    }

    private void initImage() {
//        首先获得File目录的路径
        final File dir = getFilesDir();
        imgFile = new File(dir,"start.jpg");
//        在这里判断图片是否存在，如果存在的话直接在内存中加载，如果不存在在资源文件中加载。
        if (imgFile.exists()){
            iv_start.setImageBitmap(BitmapFactory
                    .decodeFile(String.valueOf(imgFile.getAbsoluteFile())));
        }else{
            iv_start.setImageResource(R.mipmap.start);
        }
//        创建一个缩放动画
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        动画结束后保持填充状态
        scaleAnim.setFillAfter(true);
//        动画时间
        scaleAnim.setDuration(2500);
//        对动画结束设置监听
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

//                StartActivity();
//            Picasso.with(SplashActivity.this).load("https://pic4.zhimg.com/v2-f2cf38f62276ffe6c86f7d0a31a38e9f_xxdpi.jpg").into(iv_start);
//                Bitmap bitmap = Picasso.with(SplashActivity.this).load("https://pic4.zhimg.com/v2-f2cf38f62276ffe6c86f7d0a31a38e9f_xxdpi.jpg").get();
            }
            @Override
            public void onAnimationEnd(Animation animation) {
//                当动画结束时我们请求网络图片并将fileimag文件夹中的图片改变，下次进来的时候就是显示的最新的图片
//                同时我们启动另一个activity
                HttpUtils.get(Constent.NEWSTART, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody) {
                        try {
//                            这里可以看到已经返回成功数据，下面就是要进行解析
                            Log.e("这是启动画面中的闪片页面",new String(responseBody));

                            JSONObject jsonObject2 = new JSONObject(new String(responseBody));
                            String creatives = jsonObject2.getString("creatives");
                            Log.e("这是在object得到的object对象",creatives);

                            JSONArray jsonArray = new JSONArray(creatives);
                            JSONObject jsonObject3 = jsonArray.getJSONObject(0);

                            final String imgUrl = jsonObject3.getString("url");

//                          这里发现打印成功
                            Log.e("这是在object得到的url",imgUrl);


//                            将返回的字节数组转换为jsonobject对象
//                            JSONObject jsonObject = new JSONObject(new String(responseBody));
//                            获取图片的URL,注意这里我们的url是一个全名所以这里不能直接将我们的url传入

//                            url = jsonObject.getString("img");
//                            LogE.E(url);
//                            Log.e("这个是我们的闪屏页面的URL", url);
//                            Picasso.with(SplashActivity.this).load(url).into(iv_start);
//                            这里我们发现我们的首页闪屏图片的URL为https的形式返回的，所以我们的异步请求就不能读取了，现在换掉用Pocasso来读取图片
//                            注意这里我们要开启一个新线程 不然会报错
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Bitmap bitmap = Picasso.with(SplashActivity.this).load(imgUrl).get();
//                                现在得到我们的Bitmap对象，下面进行Bitmap--->byte【】转换
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                        byte[] datas = baos.toByteArray();
//                                下面调用方法来存储我们的字节数组
                                        saveImage(imgFile,datas);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();

                            HttpUtils.get2(imgUrl, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
//                                    此时我们获得了图片的字节数组，需要写入到文件中

                                    saveImage(imgFile,binaryData);

//                                    同时启动我们的mainactivity
                                    LogE.E("成功了");
                                    StartActivity();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                                    Log.e("这个是我们图片请求返回失败码",""+statusCode);
                                    LogE.E(String.valueOf(statusCode));
                                    StartActivity();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, Throwable error) {
                        StartActivity();

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        对imageview设置动画
        iv_start.setAnimation(scaleAnim);

    }

    private void StartActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
//        结束当前activity
        finish();
        Log.e("------","启动activity");
    }

    private void saveImage(File file,byte[] bytes) {
//        清除文件数据
        file.delete();
//        创建一个输出流对象
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
