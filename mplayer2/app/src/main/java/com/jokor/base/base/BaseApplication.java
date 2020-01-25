package com.jokor.base.base;

import android.app.Application;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jokor.base.BuildConfig;
import com.jokor.base.model.db.DBManager;
import com.jokor.base.model.db.MyObjectBox;
import com.jokor.base.pages.util.video.MyFileNameGenerator;
import com.jokor.base.util.base.FileUtil;
import com.jokor.base.util.glide.MediaLoader;
import com.jokor.base.util.base.PreferenceUtil;
import com.kymjs.okhttp3.OkHttpStack;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;

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
