package com.hhxy.wuhu.adapter;

import android.app.Activity;
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

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.NewsContentActivity;
import com.hhxy.wuhu.model.StoriesBean;
import com.hhxy.wuhu.util.ProUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10.
 */

public class NewsItemAdapt extends BaseAdapter {
//   首先我们的数据肯定是在一个集合中的，集合中存的就是我们的
//    主题新闻的对象，所以我们先定义一个集合来存我们的对象
//    这次我们通过构造方法来传递我们的集合数据

    List<StoriesBean> entitys;
    Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions displayImageOptions;

    public NewsItemAdapt(Context context,List<StoriesBean> entitys){
//        这样我们就通过构造将我们的数据传到我们的adapt中了
        this.entitys = entitys;
        this.context = context;
//        创建我们的imageLoader对象
        imageLoader = ImageLoader.getInstance();
//        指定我们的option
        displayImageOptions = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();

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
        ViewHolder viewHolder=null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView =  LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title2);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title2);
//            同时将我们的viewholder作为tag存储
            convertView.setTag(viewHolder);
            Log.e("这是在getview（）中打印entity",entitys.get(position).getTitle());
        }else {
            //        如果我们的convertView不为空的话，我们就复用，先取出我们的viewholder
            viewHolder = (ViewHolder) convertView.getTag();
        }

//       下面就来设置我们的textview   imageview显示的内容了
        viewHolder.tv_title.setText(entitys.get(position).getTitle());
//        获得xml文件中的字符串
        String readSequence = ProUtils.getStringFromDefault(context,"read","");
        if (readSequence.contains(entitys.get(position).getId()+"")){
//            这里表示消息是点击过得，改变字体颜色
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.colorDark));
        }

//        这里我们判断当我们的图片的URL不为空的时候才显示
        if (entitys.get(position).getImages()!=null){
//            这里如果不加判断就会报空指针，，，我们通过看我们的返回数据，会发现我们的
//            用户推荐的新闻对象集合中，有的新闻是没有图片的，所以有时候会包空指针的，所以这里要判断
//             不过如果没有图片的话我们的imageview就不用显示了
            imageLoader.displayImage(entitys.get(position).getImages().get(0),

              viewHolder.iv_title,displayImageOptions);
        }
        else{
//            这里如果我们的图片不存在的话，我们就把我们的imageviewgone掉
//            如果有图片我们才去请求。
            viewHolder.iv_title.setVisibility(View.GONE);
//            做到做到这一步我们的显示基本就完成了，下面就是我们的listview的监听了，我们同样
        }
//        这里对我们的条目监听
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = entitys.get(position).getId();
//                Intent intent = new Intent(context, NewsContentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("newsID",id);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

//    创建我们的viewholder类
    private  static class ViewHolder{
        TextView tv_title;
        ImageView iv_title;
    }
}
