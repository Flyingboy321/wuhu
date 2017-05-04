package com.hhxy.wuhu.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.hhxy.wuhu.R;
import com.hhxy.wuhu.activity.MainActivity;

import java.io.File;

import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {
    static int count;
    public MyPushMessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){

            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context, "客户端收到推送内容："+intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();

            Intent intentNew = new Intent(context, MainActivity.class);
//                  创建pendingIntent对象
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intentNew,0);
//                首相获得系统服务管理对象
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                获得notification对象
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(intent.getStringExtra("msg"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setContentText("这个是内容栏这个是内容栏这个是内容栏这个" +
//                                "这个是内容栏这个是内容栏这个是内容栏是内容栏这个是内容栏这个是内容栏这个是内容栏")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.big_image))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                        .setVibrate(new long[]{0,1000,1000,1000})
                    .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
//                        .setLights(Color.GREEN,1000,1000)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("dfsfgsdgff这个是内容栏这个是内容栏是内容栏这个是内容栏这个是内容栏这个这个是内容栏这" +
                            "这个是内容栏这个是内容栏是内容栏这个是内容栏这个是内容栏这个个是内容栏是内容栏这个是内容栏这个是内容栏这个sdd"))
//                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.pastyou)))
                    .build();
            notificationManager.notify(count,notification);
            count++;

        }
    }
}
