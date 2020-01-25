package com.jokor.base.pages.util.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jokor.base.R;

import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

public class WebActivity extends AppCompatActivity {
	
	private static final String TAG = "WebActivity";
	
	private FrameLayout mFrameLayout;
	private CacheWebView webBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		//常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_web);
		mFrameLayout =  findViewById(R.id.mFrameLayout);
		webBase = findViewById(R.id.web_base);
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initData(){
		String webBaseURL = getIntent().getStringExtra("URL");
		if (webBaseURL == null || webBaseURL.equals("")) {
			//设置默认URL
			webBaseURL = "http://v.sigu.me/";
		}
		WebSettings webSettings = webBase.getSettings();
		if (Build.VERSION.SDK_INT >= 19) {
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
		}
		
		if (Build.VERSION.SDK_INT >= 19) {
			webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
		} else {
			webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
		}
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
		}
		CacheWebView.getCacheConfig().setEncodeBufferSize(1024);
		webBase.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码
		webBase.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
		webBase.setBlockNetworkImage(true);
		webBase.setEnableCache(true);
		
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setSupportZoom(true);// 设置可以支持缩放
		webSettings.setBuiltInZoomControls(false);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
		
		webSettings.setDisplayZoomControls(true);//隐藏缩放工具
		webSettings.setUseWideViewPort(true);// 扩大比例的缩放
		//自适应屏幕
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
		webSettings.setDatabaseEnabled(true);//
		webSettings.setSavePassword(true);//保存密码
		
		webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
		webBase.setSaveEnabled(true);
		webBase.setKeepScreenOn(true);
		
		
		// 设置setWebChromeClient对象
		InsideWebChromeClient mInsideWebChromeClient = new InsideWebChromeClient();
		webBase.setWebChromeClient(mInsideWebChromeClient);
		//设置此方法可在WebView中打开链接，反之用浏览器打开
		webBase.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (!webBase.getSettings().getLoadsImagesAutomatically()) {
					webBase.getSettings().setLoadsImagesAutomatically(true);
				}
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}
			
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				String uri_authority = request.getUrl().getAuthority();
				Log.i(TAG, "shouldInterceptRequest: uri_authority --- "+uri_authority);
				if(uri_authority.contains("misssglobal")
						|| uri_authority.contains("yuyue008")
						|| uri_authority.contains("mendoc")
						|| uri_authority.contains("hanmenghui")){
					return new WebResourceResponse(null,null,null);
				}else{
					return super.shouldInterceptRequest(view, request);         
				}
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("http:") || url.startsWith("https:")) {
					view.loadUrl(url);
					return false;
				}
				// Otherwise allow the OS to handle things like tel, mailto, etc.
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		webBase.setDownloadListener((paramAnonymousString1, paramAnonymousString2, paramAnonymousString3, paramAnonymousString4, paramAnonymousLong) -> {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(Uri.parse(paramAnonymousString1));
			startActivity(intent);
		});
		webBase.loadUrl(webBaseURL);
		
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		
		try {
			super.onConfigurationChanged(newConfig);
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT");
			}
		} catch (Exception ex) {
		
		}
	}
	
	@Override
	public void onBackPressed() {
		if (webBase.canGoBack()) {
			webBase.goBack();
		}else {
			super.onBackPressed();
		}
	}
	
	private class InsideWebChromeClient extends WebChromeClient {
		private View mCustomView;
		private CustomViewCallback mCustomViewCallback;
		
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mCustomView = view;
			mFrameLayout.addView(mCustomView);
			mCustomViewCallback = callback;
			webBase.setVisibility(View.GONE);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		public void onHideCustomView() {
			webBase.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			mFrameLayout.removeView(mCustomView);
			mCustomViewCallback.onCustomViewHidden();
			mCustomView = null;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			super.onHideCustomView();
		}
	}
	
}
