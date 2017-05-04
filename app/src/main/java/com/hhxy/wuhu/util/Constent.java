package com.hhxy.wuhu.util;

/**
 * Created by Administrator on 2016/11/30.
 */
//将我们常用的URL进行静态封装
public class Constent {
    public static final String BASEURL = "http://news-at.zhihu.com/api/4/";
    public static final String START = "start-image/1080*1776";
    public static final String NEWSTART = "http://news-at.zhihu.com/api/7/prefetch-launch-images/1080*1920";
//    这个是我们的过往消息的URL，只用在后面拼接我们的date就可以获得这个消息的json
    public static final String BEFORE = "news/before/";
    public static final String THEMES = "themes";
//    这个使我们的主题新闻获得的url一部分，全部为。http://news-at.zhihu.com/api/4/theme/11  后面是我们的id

    public static final String THEMENEWS = "theme/";
//    这个是我们解读新闻内容的url拼接
    public static final String CONTENT = "news/";


}
