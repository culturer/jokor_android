package vip.jokor.im.wedgit.notification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import vip.jokor.im.R;

import static android.app.Notification.DEFAULT_ALL;

public class NotificationUtil {
	public static void sendNotification(Context context,String title,String content,Intent intent){
		//获取NotificationManager实例
		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder;
		//判断是否是8.0Android.O
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel chan1 = new NotificationChannel("static",
					"Primary Channel", NotificationManager.IMPORTANCE_HIGH);
			notifyManager.createNotificationChannel(chan1);
			builder = new NotificationCompat.Builder(context, "static");
		} else {
			builder = new NotificationCompat.Builder(context);
		}
		//对builder进行配置
		builder.setContentTitle(title) //设置通知栏标题
				.setContentText(content) //设置通知栏显示内容
				.setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
				.setSmallIcon(R.mipmap.logo)
				.setDefaults(DEFAULT_ALL)
				.setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消
		//绑定intent，点击图标能够进入某activity
		if (intent!=null){
			PendingIntent mPendingIntent=PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(mPendingIntent);
		}
		//绑定Notification，发送通知请求
		notifyManager.notify(0, builder.build());
//		播放提示音
		MediaPlayer mMediaPlayer=MediaPlayer.create(context, R.raw.notification);
		mMediaPlayer.start();
	}
}
