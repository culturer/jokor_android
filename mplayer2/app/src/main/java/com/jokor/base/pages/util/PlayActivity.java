package com.jokor.base.pages.util;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jokor.base.R;
import com.jokor.base.wedgit.ZoomImageView;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class PlayActivity extends AppCompatActivity {
	
	private static final String TAG = "PlayActivity";
	
	String mUrl;
	JzvdStd mNiceVideoPlayer;
	ZoomImageView img;
	boolean isImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
//		initToolBar();
		isImg = getIntent().getBooleanExtra("isImg",true);
		mUrl = getIntent().getStringExtra("mUrl");
		Log.i(TAG, "mUrl: "+mUrl);
		mNiceVideoPlayer = findViewById(R.id.nice_video_player);
		img = findViewById(R.id.img);
		if (isImg){
			Log.i(TAG, "isImg: true");
			initImg();
		}else {
			Log.i(TAG, "isImg: false");
			initVideo();
		}
	}
	
//	private void initToolBar(){
//		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//		toolbar.setPadding(0,SizeUtil.getStatusBarHeight(this),0,0);
//		toolbar.setTitle("");
//		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//			}
//		});
//		toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
//		setSupportActionBar(toolbar);
//	}
	
	private void initVideo(){
		mNiceVideoPlayer.setVisibility(View.VISIBLE);
		img.setVisibility(View.GONE);
		Glide.with(this)
				.load(mUrl+"_l")
				.into(mNiceVideoPlayer.thumbImageView);
		mNiceVideoPlayer.setUp(new JZDataSource(mUrl,"小丑"),Jzvd.SCREEN_NORMAL);
		mNiceVideoPlayer.startVideo();
	}
	
	private void initImg(){
		mNiceVideoPlayer.setVisibility(View.GONE);
		img.setVisibility(View.VISIBLE);
		RequestOptions options = new RequestOptions()
				.error(R.mipmap.img_error)//加载错误之后的错误图
//				.override(400,400)//指定图片的尺寸
				.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//				.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//				.transform(new GlideRoundTransform(this, 8))
//				.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
				.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存所有版本的图像
		Glide.with(this)
				.load(mUrl)
				.apply(options)
				.into(img);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		if (!isImg){
			if (Jzvd.backPress()) {
				return;
			}
		}
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		if (!isImg){
			Jzvd.releaseAllVideos();
		}
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Jzvd.releaseAllVideos();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		if (!isImg){
			Jzvd.releaseAllVideos();
		}
		super.onDestroy();
	}
}
