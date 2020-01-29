package vip.jokor.im.base;

import android.app.Application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.danikula.videocache.HttpProxyCacheServer;
import vip.jokor.im.BuildConfig;
import vip.jokor.im.R;
import vip.jokor.im.model.db.DBManager;
import vip.jokor.im.model.db.MyObjectBox;
import vip.jokor.im.pages.util.video.MyFileNameGenerator;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.util.base.FileUtil;
import vip.jokor.im.util.glide.MediaLoader;
import vip.jokor.im.util.base.PreferenceUtil;
import com.kymjs.okhttp3.OkHttpStack;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;

import com.taobao.agoo.BaseNotifyClickActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.io.File;
import java.lang.reflect.Field;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import okhttp3.OkHttpClient;
import ren.yale.android.cachewebviewlib.CacheWebView;

public class BaseApplication extends Application {
	
	private static final String TAG = "BaseApplication";
	private BoxStore boxStore;
	
	@Override
	public void onCreate(){
		super.onCreate();
		RxVolley.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER, new OkHttpStack(new OkHttpClient())));
		PreferenceUtil.init(this);
		FileUtil.initFilePath(this);
		boxStore = MyObjectBox.builder().androidContext(this).build();
		if (BuildConfig.DEBUG){
			new AndroidObjectBrowser(boxStore).start(this);
			Log.i(TAG, "Using ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
		}
		DBManager.getInstance().init(this);
		Album.initialize(AlbumConfig.newBuilder(this).setAlbumLoader(new MediaLoader()).build());
		//修改webview缓存大小
		CacheWebView.getCacheConfig().init(this,new File(this.getCacheDir(),"web_cache").getAbsolutePath(),1024*1024*1000,1024*1024*20).enableDebug(true);//1000M 磁盘缓存空间,10M 内存缓存空间
		initPush();
	}

	private void initPush(){
		//设置LOG开关，默认为false
		UMConfigure.setLogEnabled(true);
		try {
			Class<?> aClass = Class.forName("com.umeng.commonsdk.UMConfigure");
			Field[] fs = aClass.getDeclaredFields();
			for (Field f:fs){
				Log.e(TAG,"ff="+f.getName()+"   "+f.getType().getName());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		UMConfigure.setProcessEvent(true);//支持多进程打点
		// 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
		// 参数一：当前上下文context；
		// 参数二：应用申请的Appkey（需替换）；
		// 参数三：渠道名称；
		// 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
		// 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
		UMConfigure.init(this,
				"5e2fc0b04ca357c4d200013a",
				"Android",
				UMConfigure.DEVICE_TYPE_PHONE,
				"bb78f489ce135678e95928e65c895037"
		);
		//获取消息推送代理示例
		PushAgent mPushAgent = PushAgent.getInstance(this);

		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

		mPushAgent.setNotificaitonOnForeground(false);

		mPushAgent.setDisplayNotificationNumber(1);

		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回deviceToken deviceToken是推送消息的唯一标志
				Log.i(TAG,"注册成功：deviceToken：-------->  " + deviceToken);
				Urls.DEVICE_TOKEN = deviceToken;
				MainPresenter.getInstance().updateDeviceToken();
			}

			@Override
			public void onFailure(String s, String s1) {
				Log.e(TAG,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			UmengMessageHandler umengMessageHandler = new UmengMessageHandler() {
				@Override
				public Notification getNotification(Context context, UMessage uMessage) {
					Log.e(TAG, "getNotification: "+uMessage.title+","+uMessage.text);
					String channelId = getResources().getString(R.string.app_name);
					NotificationChannel channel = new NotificationChannel(channelId,"小丑", NotificationManager.IMPORTANCE_HIGH);
					channel.enableLights(true);
					// 自定义声音
					channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/notification"),null);
					channel.enableVibration(true);
					NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
					manager.createNotificationChannel(channel);
					Notification notification = new NotificationCompat.Builder(context,channelId)
							.setContentTitle(uMessage.title)
							.setContentText(uMessage.text)
							.setWhen(System.currentTimeMillis())
							.setSmallIcon(R.mipmap.logo)
							.setAutoCancel(true)
							.setDefaults(Notification.DEFAULT_ALL)
							.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
							.setPriority(NotificationCompat.PRIORITY_HIGH)
							.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo))
							.build();
					manager.notify(1,notification);
					return notification;
				}
			};
			mPushAgent.setMessageHandler(umengMessageHandler);
		}

//		Log.e(TAG, "initPush: 设备品牌 MANUFACTURER "+ Build.MANUFACTURER);
//		Log.e(TAG, "initPush: 设备品牌 BRAND "+ Build.BRAND);
//		if (isMainProcess){
//			//红米通道
//			if (Build.BRAND.toLowerCase().equals("redmi")){
//				Log.e(TAG, "initMiPush: 检查设备" +MiPushRegistar.checkDevice(this));
//				Log.e(TAG, "initMiPush: 初始化小米推送" );
//				BaseNotifyClickActivity.addNotifyListener(new BaseNotifyClickActivity.INotifyListener() {
//					@Override
//					public String parseMsgFromIntent(Intent intent) {
//						if (intent!=null) Log.e(TAG, "parseMsgFromIntent: "+intent.getDataString());
//						return null;
//					}
//
//					@Override
//					public String getMsgSource() {
//						return null;
//					}
//				});
//				Runnable thread = new Runnable() {
//					public void run() {
//						MiPushClient.registerPush(BaseApplication.this, "2882303761518290216", "5271829067216");
//					}
//				};
//				(new Thread(thread)).start();
//			}else {
//				//小米通道
//				MiPushRegistar.register(this,"2882303761518290216", "5271829067216");
//				//华为通道
//				Log.e(TAG, "initPush: 初始化华为厂商同道" );
//				Log.e(TAG, "initPush: check brand "+Build.BRAND );
//				HuaWeiRegister.register( this);
//				//OPPO通道，参数1为app key，参数2为app secret
//				OppoRegister.register(this, "07fe9cec78a64725b3753bb2d9f1f7ba", "83bf634e29fe438c8c9d4e27c8f3bc7d");
//				//vivo 通道
//				VivoRegister.register(this);
//			}
//		}
//
//		 选用AUTO页面采集模式
//		MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
	}


	public BoxStore getBoxStore(){
		return boxStore;
	}

	private HttpProxyCacheServer proxy;
	public static HttpProxyCacheServer getProxy(Context context) {
		BaseApplication app = (BaseApplication) context.getApplicationContext();
		return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
	}

	private HttpProxyCacheServer newProxy() {
		return new HttpProxyCacheServer.Builder(this)
				.maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
				.fileNameGenerator(new MyFileNameGenerator())
				.build();
	}
	
}
