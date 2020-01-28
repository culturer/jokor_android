package vip.jokor.im.base;

import android.app.Application;

import android.content.Context;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import vip.jokor.im.BuildConfig;
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

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.io.File;

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
