package com.hhxy.wuhu.model;

/**
 * Created by Administrator on 2016/11/30.
 */
//这个类主要用来存储每条侧边栏的信息的
public class NewsListItem {
    private String title;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NewsListItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
