package com.hhxy.wuhu.fragment;

//import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.MainActivity;
import com.hhxy.wuhu.log.LogE;
import com.hhxy.wuhu.model.NewsListItem;
import com.hhxy.wuhu.util.Constent;
import com.hhxy.wuhu.util.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/30.
 */
//@SuppressLint("ValidFragment")
//    我们的主题日报是通过http://news-at.zhihu.com/api/4/themes这个URL得到的
//    而我们每个主题日报对应的消息是在主题日报的 others（这是一个数组，用来存我们的每条主题日报的，）中
//    每个元素的id对应的，也就是用URL：  http://news-at.zhihu.com/api/4/theme/11
//    这样就可以得到我们对应的主题日报的内容了，所以我们要将我们的主题日报的id，和  title 通过构造方法传给我们的
//    newsFragment  这样我们才可以解析


public class MenuFragment extends Fragment {

    private TextView tv_main;
    private ListView lv_item;
    private ArrayList<NewsListItem> items;
//    在这里创建一个handle对象，用来在子线程中更新侧边栏的listview防止线程阻塞
    private Handler handler = new Handler();
    private Activity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        这里获得我们的activity对象
        mActivity = getActivity();
    }

//    重写onCreateview（）方法用来填充侧边栏
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        用布局填充文件填充我们的view并返回，作为我们的fragment显示的view
        View view = inflater.inflate(R.layout.menu,container);
//       找到这两个控件
//        这个tv_main其实就是我们的首页textview，我们要对他监听，点击后回到首页
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_main:
//                        此时打开首页，也就是mainactivity中的方法就好。
                        ((MainActivity)mActivity).loadLatest();

                }
//                同时关闭我们的侧边栏
                ((MainActivity)mActivity).closeMenu();


            }
        });
//        这里是我们的listview，我们要是想实现跳转我们就要对listview进行监听
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                在这里对我们的侧边栏点击事件进行处理
//                     现在我们的newsFragment就能够显示了。
                getFragmentManager().beginTransaction().replace(R.id.fl_content
                        ,new NewsFragment(items.get(position).getId()
                ,items.get(position).getTitle()),"news")
                        .commit();
//                同时我们要关闭我们的侧边栏,我们可以通过调用我们的mDrawerLayout.closeDrawers();
//                来关闭我们的侧边栏，不过这里我们的mDrawerLayout试provide的所以我们拿不到，我们必须在
//                activity中定义一个方法。在方法中调用，而我们在这里可以拿到activity，所以调方法就好了

                ((MainActivity)mActivity).closeMenu();
//                现在我们的menu可以关闭了，下一步 就是显示我们的数据了



            }
        });
//        此时为listview初始化数据

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getItems();

    }

    //        此方法用来为listview填充数据，通过请求网络填充数据
    public void getItems() {
        items = new ArrayList<NewsListItem>();
        HttpUtils.get(Constent.THEMES,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
//                    获取others对应的json数组，封装了一个多个目录的数据，我们只需要获得标题 id即可
                    JSONArray itemsArry = response.getJSONArray("others");
                    for (int i = 0;i<itemsArry.length();i++){
                        NewsListItem newsListItem = new NewsListItem();
//                        获取jsonabject
                        JSONObject itemObject = itemsArry.getJSONObject(i);
//                        这里我们把name熟悉的标签赋值给我们的title了，
//                         所以在上面我们构造newsFragment时传入的title其实是name
                        newsListItem.setTitle(itemObject.getString("name"));
                        newsListItem.setId(itemObject.getString("id"));
//                        打印出语句
                        LogE.E(newsListItem.toString());
//                        将这个对象添加到数组中
                        items.add(newsListItem);
//                        lv_item.setAdapter(new NewsTypeAdapter());


                    }
                    lv_item.setAdapter(new NewsTypeAdapter());



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public class  NewsTypeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                 convertView = LayoutInflater.from(getActivity()).inflate(R.layout.menu_item,parent,false);
            }
            Log.e("=========", String.valueOf(position));
            TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
            tv_item.setText(items.get(position).getTitle());
//            Toast.makeText(getActivity().getApplicationContext(),tv_item.getText().toString(),
//                    Toast.LENGTH_SHORT,).show();
            LogE.E(tv_item.getText().toString());
            return convertView;
        }
    }
}
