package com.hhxy.wuhu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.LatestContentActivity;
import com.hhxy.wuhu.model.Latest;
import com.hhxy.wuhu.model.StoriesBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/9.
 */
//这个是我们的Listview的适配器

public class MainNewsItemAdapter extends BaseAdapter {
    private static final String TAG = "MainNewsItemAdapter";
//    在这里我们的数据只是封装在我们的新闻故事中的，所以我们先定义一个新闻数组
//    这个集合中的对象应该就是我们的新闻消息
    List<StoriesBean> entitys;
    Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions displayImageOptions;
    private int id;

    public MainNewsItemAdapter(Context context) {

        super();
//        在刚创建我们的adapt的时候我们的集合是空的
        entitys = new ArrayList<>();
//        通过getInstance方法来创建对象
        imageLoader = ImageLoader.getInstance();
//        这个是加入图片的缓存功能。缓存到磁盘，缓存到内存中
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

//        无参构造方法
        this.context = context;
    }
    public void addList(List<StoriesBean> entitys){
//        这里将我们传过来的集合全部添加到我们的集合中
        this.entitys.addAll(entitys);
//        通知系统重绘制
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entitys.size();
    }

    @Override
    public Object getItem(int position) {
        return entitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getItemId: 这里打印出position"+position);

//        创建我们的viewholder对象
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
//            注意这里需要一个contex对象，所以我们要在创造adapt的时候传递过来
//            c现在就需要一个布局文件了，代表我们的列表条目布局文件
//            在次，这个false是什么作用
            convertView = LayoutInflater.from(context).inflate(R.layout.main_news_item,parent,false);
//            找到我们的两个组件
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_news = (TextView) convertView.findViewById(R.id.tv_news);
            convertView.setTag(viewHolder);
        }else {
//            强转为我们的viewholder
            viewHolder = (ViewHolder) convertView.getTag();

        }

            //        设置我们的view显示的标题
            viewHolder.tv_title.setText(entitys.get(position).getTitle());
//        这里通过UIL框架请求图片，这里用Picasso来处理图片会更好
//            imageLoader.displayImage(entitys.get(position).getImages().get(0)
//                    ,viewHolder.iv_title,displayImageOptions);
        Picasso.with(context).load(entitys.get(position).getImages().get(0)).into(viewHolder.iv_title);
        Log.e(TAG, "getView:这个是图片的URL打印"+entitys.get(position).getImages().get(0));
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Log.e(TAG, "onclik: 这里打印出position"+position);

                    id = entitys.get(position).getId();
                    Intent intent  = new Intent(context, LatestContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("newsID", id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    Toast.makeText(context,position+""+id,Toast.LENGTH_SHORT).show();
                }
            });





        return convertView;
    }
    public  static class ViewHolder {
        TextView tv_title;
        ImageView iv_title;
        TextView tv_news;
    }
}
