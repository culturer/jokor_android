package vip.jokor.im.pages.main.main_page.chat.chat_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.base.Config;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.db.Session;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.util.audio.AudioRecoder;
import vip.jokor.im.wedgit.util.EmojiUtil;
import vip.jokor.im.util.audio.AudioPlayer;
import vip.jokor.im.util.glide.GlideRoundTransform;
import vip.jokor.im.util.base.ThreadUtil;
import vip.jokor.im.util.base.TimeUtil;
import vip.jokor.im.pages.util.PlayActivity;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

	private static final String TAG = "ChatAdapter";
	
	private Activity activity;
	
	//测试数据
	boolean isMySend = false;

	Session session;
	List<Msg> msgs;

	RequestOptions options;//只缓存最终的图片

	public ChatAdapter(Activity activity,List<Msg> msgs,Session session) {
		this.activity = activity;
		this.session = session;
		if (msgs == null){
			this.msgs = new ArrayList<>();
		}else {
			this.msgs = msgs;
		}
		options = new RequestOptions()
				.error(R.mipmap.img_error)//加载错误之后的错误图
				.circleCrop()
				.transform(new GlideRoundTransform(activity, 8))
				.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
	}
	
	@Override
	public int getCount() {
		return msgs.size();
	}
	
	@Override
	public Msg getItem(int i) {
		return msgs.get(i);
	}
	
	@Override
	public long getItemId(int i) {
		return i;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		//加载布局
		view = LayoutInflater.from(activity).inflate(R.layout.chat_item,null);
		TextView time = view.findViewById(R.id.time);
		TextView nick = view.findViewById(R.id.nick);
		TextView name = view.findViewById(R.id.name);
		TextView msg_text = view.findViewById(R.id.msg_text);
		ImageView msg_img = view.findViewById(R.id.msg_img);
		ImageView icon_right = view.findViewById(R.id.icon_right);
		ImageView icon_left = view.findViewById(R.id.icon_left);
		LinearLayout msg_info = view.findViewById(R.id.msg_info);
		ImageView video_button = view.findViewById(R.id.video_button);
		FrameLayout audio_layout = view.findViewById(R.id.audio_layout);
		MultiWaveHeader audio_waveHeader = view.findViewById(R.id.audio_waveHeader);
		TextView audio_text = view.findViewById(R.id.audio_text);
		nick.setVisibility(View.GONE);

		//加载消息数据
		Msg item = getItem(i);

		//控制时间显示
		if (i == 0){
			time.setVisibility(View.VISIBLE);
			time.setText(TimeUtil.date2Str(item.getCreateTime()));
		}else{
			Date lastTime = getItem(i-1).getCreateTime();
			long dur = item.getCreateTime().getTime()-lastTime.getTime();
//			Log.e("testTime", "getView: "+dur );
			if (dur> Config.chat_time_dur_show || i%20==0){
				time.setVisibility(View.VISIBLE);
				time.setText(TimeUtil.date2Str(item.getCreateTime()));
			}else {
				time.setVisibility(View.GONE);
			}
		}

		isMySend = item.getFromId() == Datas.getUserInfo().getId();
		if (isMySend){
			name.setText(Datas.getUserInfo().getUserName());

			icon_left.setVisibility(View.INVISIBLE);
			icon_right.setVisibility(View.VISIBLE);
			RequestOptions options1 = new RequestOptions()
					.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
					.circleCrop()
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
			Glide.with(activity)
					.load(Datas.getUserInfo().getIcon())
					.apply(options1)
					.into(icon_right);
			msg_info.setGravity(Gravity.END);
		}else {
			name.setText(session.getUserName());
			icon_left.setVisibility(View.VISIBLE);
			icon_right.setVisibility(View.INVISIBLE);
			msg_info.setGravity(Gravity.START);
			RequestOptions options1 = new RequestOptions()
					.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
					.circleCrop()
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
			Glide.with(activity)
					.load(item.getIcon())
					.apply(options1)
					.into(icon_left);
		}
		switch ( item.getMsgType() ){
			case Msg.MSG_TYPE_TEXT:
				msg_text.setVisibility(View.VISIBLE);
				video_button.setVisibility(View.GONE);
				msg_img.setVisibility(View.GONE);
				audio_layout.setVisibility(View.GONE);
				String str =item.getData();                             //消息具体内容
				String check = "f0[0-9]{2}|f10[0-7]";              //正则表达式，用来判断消息内是否有表情
				//解析消息内容
				SpannableString spannableString = EmojiUtil.getExpressionString(activity, str, check,80);
				msg_text.setText(spannableString);
				break;
			case Msg.MSG_TYPE_IMG:
				msg_text.setVisibility(View.GONE);
				video_button.setVisibility(View.GONE);
				msg_img.setVisibility(View.VISIBLE);
				audio_layout.setVisibility(View.GONE);
				Glide.with(activity)
						.load(item.getData())
						.apply(options)
						.into(msg_img);
				msg_img.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(activity,PlayActivity.class);
						intent.putExtra("isImg",true);
						intent.putExtra("mUrl",item.getData());
						activity.startActivity(intent);
					}
				});
				break;
			case Msg.MSG_TYPE_AUDIO:
				msg_text.setVisibility(View.GONE);
				video_button.setVisibility(View.GONE);
				msg_img.setVisibility(View.GONE);
				audio_layout.setVisibility(View.VISIBLE);
				audio_waveHeader.setVelocity(3f);
				audio_waveHeader.setWaveHeight(10);
				audio_waveHeader.setProgress(0.5f);
				audio_waveHeader.setWaves("0,0,1,1,25\n90,0,1,1,25");
				audio_waveHeader.stop();
				String strDur = AudioRecoder.getRingDuring(item.getData());
				int a = Integer.parseInt(strDur);
				int s1 = a/100;
				audio_text.setText(""+s1/10+"."+s1%10+"秒");
				View.OnClickListener listener = new View.OnClickListener() {
					MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mediaPlayer) {
							Log.i("Audio", "onCompletion: ");
							if (mediaPlayer!=null){
								mediaPlayer.release();
							}
							audio_waveHeader.stop();
						}
					} ;
					MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
						@Override
						public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
							audio_waveHeader.stop();
							if (mediaPlayer!=null){
								mediaPlayer.release();
							}
							return false;
						}
					};
					AudioPlayer audioPlayer = new AudioPlayer(true,activity,item.getData(),onCompletionListener,onErrorListener);
					@Override
					public void onClick(View view) {
						if (!audio_waveHeader.isRunning()){
							ThreadUtil.startThreadInPool(new ThreadUtil.Task() {
								@Override
								public void doTask() {
									//语音播放前要清除之前的资源
									audioPlayer.stopPlaying();
									audioPlayer.startPlaying();
								}
							});
							audio_waveHeader.start();
						}else {
							audio_waveHeader.stop();
							audioPlayer.stopPlaying();
						}
					}
				};
				audio_layout.setOnClickListener(listener);
				break;
			case Msg.MSG_TYPE_VIDEO :
				video_button.setVisibility(View.VISIBLE);
				msg_text.setVisibility(View.GONE);
				msg_img.setVisibility(View.VISIBLE);
				audio_layout.setVisibility(View.GONE);

				Glide.with(activity)
						.load(item.getData())
						.apply(options)
						.into(msg_img);
				msg_img.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(activity,PlayActivity.class);
						intent.putExtra("isImg",false);
						intent.putExtra("mUrl",item.getData());
						activity.startActivity(intent);
					}
				});
				break;
		}
		
		return view;
	}
	
	public void update(Msg msg){
		if (msg.getMsgUsed() == Msg.MSG_USED_CHAT){
			if (session == null){
				Log.i(TAG, "update: 传入的session 对象有问题 ！");
			}
			if (session != null){
				boolean flag = false;
				if (!(msg.getFromId() == session.getToId() || msg.getToId() == session.getToId())){
					flag = true;
					Log.i(TAG, "不是属于当前会话的消息！");
				}
				if ( msgs==null )msgs = new ArrayList<>();
				for (Msg item : msgs) {
					if (item.getMsgId().equals(msg.getMsgId())){
						flag = true;
						Log.i(TAG, " 消息: ["+msg.getId()+"] 已经存在");
						break;
					}
				}
				if (!flag){
					msgs.add(msg);
					activity.runOnUiThread(this::notifyDataSetChanged);
				}
			}
		}
	}
	
}
