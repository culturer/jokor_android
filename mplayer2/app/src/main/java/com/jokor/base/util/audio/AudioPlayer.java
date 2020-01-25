package com.jokor.base.util.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//注意耗时操作尽量不要在主线程中进行
public class AudioPlayer {
	
	private String fileName;
	private MediaPlayer mediaPlayer;
	private  MediaPlayer.OnCompletionListener listener;
	private MediaPlayer.OnErrorListener errListener;
	private boolean isNet = false;
	private Context context;
	
	public AudioPlayer(boolean isNet, String fileName, MediaPlayer.OnCompletionListener listener, MediaPlayer.OnErrorListener errListener) {
		this.fileName = fileName;
		this.listener = listener;
		this.errListener = errListener;
		this.isNet = isNet;
	}
	
	public AudioPlayer(boolean isNet, Context context, String fileName, MediaPlayer.OnCompletionListener listener, MediaPlayer.OnErrorListener errListener) {
		this.fileName = fileName;
		this.listener = listener;
		this.errListener = errListener;
		this.isNet = isNet;
		this.context = context;
	}
	
	//播放本地音频文件
	public void startPlaying(){
		if (isNet){
			if (mediaPlayer == null ){
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setOnCompletionListener(listener);
				mediaPlayer.setOnErrorListener(errListener);
			}
			if (!fileName.equals("")){
				try {
//				mediaPlayer.setDataSource(context, Uri.parse("http://localhost:8080/music"));
					mediaPlayer.setDataSource(context, Uri.parse(fileName));
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			if (mediaPlayer == null ){
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setOnCompletionListener(listener);
				mediaPlayer.setOnErrorListener(errListener);
			}
			if (!fileName.equals("")){
				try {
					File tempFile = new File(fileName);
					FileInputStream fis = new FileInputStream(tempFile);
					mediaPlayer.reset();
					mediaPlayer.setDataSource(fis.getFD());
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public void stopPlaying(){
		if (mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	public boolean  mediaIsNull(){
		return mediaPlayer == null;
	}
}
