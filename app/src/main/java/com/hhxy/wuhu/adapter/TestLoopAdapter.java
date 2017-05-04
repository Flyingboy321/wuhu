package com.hhxy.wuhu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.LatestContentActivity;
import com.hhxy.wuhu.activity.MainActivity;
import com.hhxy.wuhu.model.Latest;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TestLoopAdapter extends LoopPagerAdapter {
    private static final String TAG = "TestLoopAdapter";
//这个可能是但是为了试验用了，没有什么用了，固定的显示当时的图片
    private int[] mImageIds = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};

    List<Latest.TopStoriesBean> topStories;
    Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions displayImageOptions;
    private int id;

    public TestLoopAdapter(RollPagerView viewPager) {
//        在创建adapt对象的时候就获得imageload对象，这个是单例模式创建的对象，与Application类中的相同
        super(viewPager);
        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void setTopStories(List<Latest.TopStoriesBean> topStories, Context context){
//        在这里我们通过调用方法来实现我们的轮播集合内容
        this.topStories = topStories;
        this.context = context;

        Log.e("这是在轮播条的adapt中打印的",topStories.toString());
    }


    @Override
    public View getView(final ViewGroup container, final int position) {
//        这下面的两段代码好像也是没有什么用的，可能是为了试验用的
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//      这里用一个layout布局来填充view对象，layout中包含一个imageview 一个textview
        View view = LayoutInflater.from(context).inflate(R.layout.image_voll_pager,container,false);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.iv_pager);
        TextView textView = (TextView) view.findViewById(R.id.tv_pager);

        textView.setText(topStories.get(position).getTitle());
//        这里用我们的另一种 框架在请求图片
        Picasso.with(context).load(topStories.get(position).getImage()).into(imageView1);
        Log.e(TAG, "getView: 这个是在轮播图中得到的图片的URL"+topStories.get(position).getImage());

//        imageLoader.displayImage(topStories.get(position).getImage(),
//                imageView1,displayImageOptions);
//        ImageView view = new ImageView(context);
////            设置我们图片的显示内容  这里用setBackgroundResource，这样我们的图片就会铺满全viewpager
//        view.setImageResource(mImageIds[position]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                这里的到我们消息的id
//                id = String.valueOf(topStories.get(position).getId());
                id = topStories.get(position).getId();
                Intent intent = new Intent(context, LatestContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("newsID",id);
                intent.putExtras(bundle);
                context.startActivity(intent);
//                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public int getRealCount() {
        return topStories.size();

    }
}
