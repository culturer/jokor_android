package vip.jokor.im.util.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;
import com.youth.banner.loader.ImageLoader;
import com.zhihu.matisse.engine.ImageEngine;

public class MediaLoader extends ImageLoader implements AlbumLoader , ImageEngine {
	@Override
	public void load(ImageView imageView, AlbumFile albumFile) {
		load(imageView, albumFile.getPath());
		
	}
	
	@Override
	public void load(ImageView imageView, String url) {
		Glide.with(imageView.getContext())
				.load(url)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	@Override
	public void displayImage(Context context, Object path, ImageView imageView) {
		Glide.with(context)
				.load(path)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	public static RequestOptions getOption(){
		RequestOptions requestOptions = new RequestOptions()
//				.placeholder(R.mipmap.img_error)//加载成功之前占位图
//				.error(R.mipmap.logo)//加载错误之后的错误图
				.override(400, 400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
				.circleCrop()
//						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
				.diskCacheStrategy(DiskCacheStrategy.ALL);
		return requestOptions;
	}

	@Override
	public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
		Glide.with(context)
				.load(uri)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	@Override
	public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
		Glide.with(context)
				.load(uri)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	@Override
	public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
		Glide.with(context)
				.load(uri)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	@Override
	public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
		Glide.with(context)
				.load(uri)
//				.error(R.drawable.img_error)
//				.placeholder(R.mipmap.logo)
//				.crossFade()
				.into(imageView);
	}

	@Override
	public boolean supportAnimatedGif() {
		return true;
	}
}
