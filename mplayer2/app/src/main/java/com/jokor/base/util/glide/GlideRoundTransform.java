package com.jokor.base.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;
import com.jokor.base.util.base.SizeUtil;

import java.nio.ByteBuffer;
import java.security.MessageDigest;


public class GlideRoundTransform extends BitmapTransformation {
	private static float radius = 0f;
	private static final String ID = "www,jokor.vip";
	private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
	
	public GlideRoundTransform(Context context) {
		this(context, 4);
	}
	
	public GlideRoundTransform(Context context, int dp) {
		radius = SizeUtil.dip2px(context,dp);
	}
	
	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
		Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
		return roundCrop(pool, bitmap);
	}
	
	private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
		if (source == null) return null;
		Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
		Log.e("draw", "roundCrop: "+radius);
		canvas.drawRoundRect(rectF, radius, radius, paint);
		return result;
	}
	
	public String getId() {
		return getClass().getName() + Math.round(radius);
	}
	
	@Override
	public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
		messageDigest.update(ID_BYTES);

		byte[] radiusData = ByteBuffer.allocate(4).putFloat(radius).array();
		messageDigest.update(radiusData);
	}

	@Override
	public int hashCode() {
		return Util.hashCode(ID.hashCode(), Util.hashCode(radius));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GlideRoundTransform) {
			GlideRoundTransform other = (GlideRoundTransform) obj;
			return radius == radius;
		}
		return false;
	}


}
