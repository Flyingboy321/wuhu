package com.hhxy.wuhu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/4.
 */

public class WebCacheDbHelper extends SQLiteOpenHelper {
//    创建构造方法来继承父类的构造。不过这里在穿件sqliteopenhelper类的时候只用传入context和版本号就好了，数据库的名字
//    直接定义
    public WebCacheDbHelper(Context context,  int version) {
        super(context, "webCache.db",null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        实现见表语句
        db.execSQL("create table if not exists Cache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
