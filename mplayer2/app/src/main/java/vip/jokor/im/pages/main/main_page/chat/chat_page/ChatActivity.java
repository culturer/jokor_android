package vip.jokor.im.pages.main.main_page.chat.chat_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import vip.jokor.im.R;
import vip.jokor.im.im.MsgEvent;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.model.db.Session;
import vip.jokor.im.presenter.ChatPresenter;
import vip.jokor.im.presenter.UserPresenter;
import vip.jokor.im.util.base.FileUtil;
import vip.jokor.im.util.audio.AudioRecoder;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.SoftKeyBoardListener;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import vip.jokor.im.wedgit.util.SoftHideKeyBoardUtil;
import vip.jokor.im.util.base.TimeUtil;
import vip.jokor.im.wedgit.BarChartView;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
	
	private String TAG = "ChatActivity";

	public static final String CHAT_TYPE = "chat_type";

    LinearLayout container;

	private ChatPresenter presenter;
	private int chatType;
	private Session session;

	//监听手势
	GestureDetector mDetector;
	AudioRecoder audioRecoder;
	ListView chat_list;
	ChatAdapter adapter;
	GridView emoji_grid;
	int statusBarHeight;
	EditText chat_edit;

	//假数据记得修改
	long userId =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initData();
		initToolBar();
		initBaseView();
		initList();
		initRefresh();
		initAudio();
		initSendText();
		initSendImg();
		initSendVideo();
		initCall();
		initEmoji();
		setSoftKeyBoardListener();
		hideSoftKeyboard(ChatActivity.this);
		EventBus.getDefault().register(this);
	}

	private void initData(){
		presenter = ChatPresenter.getInstance();
		//初始化session和mmsgs
		Intent intent = getIntent();
		int type = intent.getIntExtra(CHAT_TYPE,0);
		String strFriend = intent.getStringExtra("session");
		if ( !strFriend.equals("") ){
			session = GsonUtil.getGson().fromJson(strFriend, Session.class);
		}
	}
	
	private void initToolBar(){
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
        container = findViewById(R.id.container);
        container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		if (session !=null){
			RequestOptions options = new RequestOptions()
					.override(SizeUtil.dip2px(this,48),SizeUtil.dip2px(this,48))//指定图片的尺寸
//					.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
					.circleCrop()
					.diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
			Glide.with(this)
					.load(session.getIcon())
					.apply(options)
					.into(new SimpleTarget<Drawable>() {
						@Override
						public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
							toolbar.setOverflowIcon(resource);
						}
					});
		}
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initList(){
		chat_list = findViewById(R.id.chat_list);
		chat_list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		adapter = new ChatAdapter(this,presenter.getMsgs(session.getToId(),session.getMsgFrom()),session);
		chat_list.setAdapter(adapter);
		chat_list.setLongClickable(true);
		chat_list.setOnTouchListener((view, motionEvent) -> {
			mDetector.onTouchEvent(motionEvent);
			return false;
		});
	}
	
	private void initRefresh(){
		SmartRefreshLayout chat_refresh = findViewById(R.id.chat_refresh);
		chat_refresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull RefreshLayout refreshLayout) {
				//下拉加载
				refreshLayout.finishRefresh();
			}
		});
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private void initAudio(){
		BarChartView audio_bar = findViewById(R.id.audio_bar);
		AndPermission.with(this)
				.runtime()
				.permission(Permission.Group.STORAGE,Permission.Group.MICROPHONE)
				.onGranted(permissions -> {
				
				})
				.onDenied(permissions -> {
					// Storage permission are not allowed.
				})
				.start();
		ImageButton chat_audio = findViewById(R.id.chat_audio);
		//发送语音，预留接口，暂时屏蔽
		chat_audio.setOnTouchListener((v, event) -> {
			switch (event.getAction()){
				case MotionEvent.ACTION_DOWN:
					//初始化录音设置
					recordAudio();
					//开始录音
					audioRecoder.startRecord();
					audio_bar.setVisibility(View.VISIBLE);
					audio_bar.start();
					break;
				case MotionEvent.ACTION_UP:
					//结束录音（保存录音文件）
					audioRecoder.stopRecord();
					audio_bar.setVisibility(View.GONE);
					audio_bar.stop();
					break;
			}
			return false;
		});
	}

	//发生文本消息
	private void initSendText(){
		chat_edit = findViewById(R.id.chat_edit);
		chat_edit.setOnEditorActionListener((textView, actionId, keyEvent) -> {
			switch(actionId){
				case EditorInfo.IME_NULL:
					Log.i(TAG, "Done_content: "+textView.getText());
					break;
				case EditorInfo.IME_ACTION_SEND:
					String msg = textView.getText().toString();
					if (!msg.equals("")){
						Msg msg1 =  presenter.senTextdMsg(session,chat_edit.getText().toString());
						adapter.update(msg1);
						chat_edit.setText("");
					}
					break;
				case EditorInfo.IME_ACTION_DONE:
					Log.i(TAG, "action done for number_content: "+textView.getText());
					break;
			}
			return true;
		});
		TextView chat_send = findViewById(R.id.chat_send);
		chat_send.setOnClickListener(view -> {
			String msg = chat_edit.getText().toString();
			if (!msg.equals("")){
				//发送消息
				adapter.update(presenter.senTextdMsg(session,chat_edit.getText().toString()));
				chat_edit.setText("");
			}
		});
	}
	
	private void initSendImg(){
		Widget widget = Widget.newDarkBuilder(ChatActivity.this)
				.title("选择图片") // Title.
				.statusBarColor(getResources().getColor(R.color.black,null)) // StatusBar color.
				.toolBarColor(getResources().getColor(R.color.black,null)) // Toolbar color.
				.navigationBarColor(getResources().getColor(R.color.black,null)) // Virtual NavigationBar color of Android5.0+.
				.mediaItemCheckSelector(getResources().getColor(R.color.grey_dark,null), getResources().getColor(R.color.black,null)) // Image or video selection box.
				.bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
				.buttonStyle (// Used to configure the style of button when the image/video is not found.
						Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
								.setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
								.build()
				)
				.build();
		ImageButton chat_camera = findViewById(R.id.chat_camera);
		chat_camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Album.image(ChatActivity.this) // 选择图片。
						.multipleChoice()
						.widget(widget)
						.camera(true)
						.columnCount(2)
						.selectCount(1)
						.checkedList(new ArrayList<AlbumFile>())
						.filterSize(null)
						.filterMimeType(null)
						.afterFilterVisibility(false) // 显示被过滤掉的文件，但它们是不可用的。
						.onResult(new Action<ArrayList<AlbumFile>>() {
							@Override
							public void onAction(@NonNull ArrayList<AlbumFile> result) {
								//图片上传
								presenter.sendImgMsg(session,result.get(0).getPath());
							}
						})
						.onCancel(new Action<String>() {
							@Override
							public void onAction(@NonNull String result) {
								Log.i(TAG, "onAction: "+result);
							}
						})
						.start();
			}
		});
	}
	
	private void initSendVideo(){
		Widget widget = Widget.newDarkBuilder(ChatActivity.this)
				.title("选择视频") // Title.
				.statusBarColor(getResources().getColor(R.color.black,null)) // StatusBar color.
				.toolBarColor(getResources().getColor(R.color.black,null)) // Toolbar color.
				.navigationBarColor(getResources().getColor(R.color.black,null)) // Virtual NavigationBar color of Android5.0+.
				.mediaItemCheckSelector(getResources().getColor(R.color.grey_dark,null), getResources().getColor(R.color.black,null)) // Image or video selection box.
				.bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
				.buttonStyle (// Used to configure the style of button when the image/video is not found.
						Widget.ButtonStyle.newLightBuilder(ChatActivity.this) // With Widget's Builder model.
								.setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
								.build()
				)
				.build();
		ImageButton chat_video = findViewById(R.id.chat_video);
		chat_video.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Album.video(ChatActivity.this) // 选择图片。
						.multipleChoice()
						.widget(widget)
						.camera(true)
						.columnCount(2)
						.selectCount(1)
						.checkedList(new ArrayList<AlbumFile>())
						.filterSize(null)
						.filterMimeType(null)
						.afterFilterVisibility(false) // 显示被过滤掉的文件，但它们是不可用的。
						.onResult(new Action<ArrayList<AlbumFile>>() {
							@Override
							public void onAction(@NonNull ArrayList<AlbumFile> result) {
								//视频文件上传
								presenter.sendVideoMsg(session,result.get(0).getPath());
							}
						})
						.onCancel(new Action<String>() {
							@Override
							public void onAction(@NonNull String result) {
								Log.i(TAG, "onAction: "+result);
							}
						})
						.start();
			}
		});
	}
	
	private void initCall(){
		ImageButton chat_tel = findViewById(R.id.chat_tel);
	}
	
	private void recordAudio(){
		audioRecoder = new AudioRecoder(FileUtil.CHAT_PATH_AUDIO,"audio_"+userId+"_"+System.currentTimeMillis());
		Log.i(TAG, "recordAudio: audio_path "+FileUtil.CHAT_PATH_AUDIO);
		Log.i(TAG, "recordAudio: audio_key " +"audio_"+userId+"_"+ TimeUtil.getCurrentTime());
		//录音回调
		audioRecoder.setOnAudioStatusUpdateListener(new AudioRecoder.OnAudioStatusUpdateListener() {
			@Override
			public void onUpdate(double db, long time) {
//               根据分贝值来设置录音时话筒图标的上下波动
//               mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
//               mTextView.setText(TimeUtils.long2String(time));
				Log.i(TAG, "onUpdate: Audio --- "+((int) (3000 + 6000 * db / 100)));
				Log.i(TAG, "Audio --- onUpdate: time["+time/1000+"]秒"+" , db["+db+"]");
			}
			
			@Override
			public void onStop(String filePath,long audio_time) {
				Log.i(TAG, "Audio --- onStop: filePath["+filePath+"]");
				Log.i(TAG, "Audio --- onStop: audio_time["+audio_time/1000f+"]秒");
				//录音完成文件上传
				presenter.sendAudioMsg(session,filePath);
			}
		});
	}
	
	private void initEmoji(){
		Handler handler = new Handler();
		ImageButton chat_emoji = findViewById(R.id.chat_emoji);
		emoji_grid = findViewById(R.id.emoji_grid);
		chat_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (emoji_grid.getVisibility() == View.VISIBLE) {
					emoji_grid.setVisibility(View.GONE);
				}
			}
		});
		chat_emoji.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (emoji_grid.getVisibility() == View.VISIBLE) {
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							emoji_grid.setVisibility(View.GONE);
						}
					}, 50);
					
				} else {
					hideSoftKeyboard(ChatActivity.this);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							emoji_grid.setVisibility(View.VISIBLE);
						}
					}, 50);
				}
			}
		});
		List<Integer> emojiList = new ArrayList<>();
		int[] emojiIds = new int[66];
		for (int i = 0; i < 66; i++) {
			if (i < 10) {
				int id = getResources().getIdentifier(
						"f00" + i,
						"mipmap", getPackageName());
				emojiIds[i] = id;
				emojiList.add(id);
			} else if (i < 66) {
				int id = getResources().getIdentifier(
						"f0" + i,
						"mipmap", getPackageName());
				emojiIds[i] = id;
				emojiList.add(id);
			}
		}
		EmojiAdapter emojiAdapter = new EmojiAdapter(ChatActivity.this, emojiList);
		emoji_grid.setAdapter(emojiAdapter);
		emoji_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(), emojiIds[i % emojiIds.length]);
				bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);//设置表情大小
				ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
				String str = null;
				if (i < 10) {
					str = "f00" + i;
				} else if (i < 66) {
					str = "f0" + i;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				chat_edit.getText().insert(chat_edit.getSelectionStart(), spannableString);
			}
		});
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private void initBaseView(){
		TextView title = findViewById(R.id.toolbar_title);
		statusBarHeight = SizeUtil.getStatusBarHeight(this);
		switch (chatType){
			case Msg.MSG_FROM_FRIEND: if ( session !=null ) title.setText(session.getUserName()); break;
			case Msg.MSG_FROM_GROUP:if ( session !=null ) title.setText(session.getUserName()); break;
		}
		SoftHideKeyBoardUtil.assistActivity(this,statusBarHeight);
		//监听手势
		mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
			private static final float FLIP_DISTANCE = 200;
			@Override
			public boolean onDown(MotionEvent motionEvent) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent motionEvent) {
			
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent motionEvent) {
				return false;
			}
			
			@Override
			public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent motionEvent) {
			
			}
			
			@Override
			public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
				if (motionEvent!=null && motionEvent1!=null){
					if (motionEvent.getX() - motionEvent1.getX() > FLIP_DISTANCE) {
						Log.i("TAG", "向左滑...");
						return true;
					}
					if (motionEvent1.getX() - motionEvent.getX() > FLIP_DISTANCE) {
						Log.i("TAG", "向右滑...");
						ChatActivity.this.finish();
						return true;
					}
					if (motionEvent.getY() - motionEvent1.getY() > FLIP_DISTANCE) {
						Log.i("TAG", "向上滑...");
						return true;
					}
					if (motionEvent1.getY() - motionEvent.getY() > FLIP_DISTANCE) {
						Log.i("TAG", "向下滑...");
					}
				}
				return false;
			}
		});
		LinearLayout container = findViewById(R.id.container);
		container.setLongClickable(true);
		container.setOnTouchListener((view, motionEvent) -> {
			mDetector.onTouchEvent(motionEvent);
			return false;
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_info_stranger, menu);
//		if (session.getMsgFrom() == MSG_FROM_FRIEND) getMenuInflater().inflate(R.menu.user_info_stranger, menu);
//		if (session.getMsgFrom() == MSG_FROM_GROUP)  getMenuInflater().inflate(R.menu.group_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case R.id.delete:
				HttpCallback callback = new HttpCallback() {
					@Override
					public void onSuccess(String t) {
						Log.e(TAG, "删除好友 onSuccess: "+t );
						try {
							JSONObject jb = new JSONObject(t);
							int status = jb.getInt("status");
							if (status == 200){
								ShowUtil.showToast(getApplicationContext(),"删除好友成功");
								finish();
							}else {
								ShowUtil.showToast(ChatActivity.this,"删除好友失败");
							}
						} catch (JSONException e) {
							ShowUtil.showToast(ChatActivity.this,"参数错误");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(VolleyError error) {
						Log.e(TAG, "删除好友 onFailure: "+error.getMessage() );
					}
				};
				Log.e(TAG, "onOptionsItemSelected: 删除好友" + GsonUtil.getGson().toJson(session) );
				UserPresenter.getInstance().deleteFriend(TAG,session.getToId(),callback);
		}
		return true;
	}

	/**
	 * 添加软键盘的监听
	 */
	private void setSoftKeyBoardListener(){
		SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
		softKeyBoardListener.setOnSoftKeyBoardChangeListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
			@Override
			public void keyBoardShow(int height) {
//				软键盘显示
				if (emoji_grid.getVisibility() == View.VISIBLE) emoji_grid.setVisibility(View.GONE);
			}

			@Override
			public void keyBoardHide(int height) {
//				软键盘影藏
			}
		});
	}

	//隐藏软键盘
	private void hideSoftKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (emoji_grid.getVisibility() == View.VISIBLE) {
			emoji_grid.setVisibility(View.GONE);
		}else {
			super.onBackPressed();
		}
	}

	@Subscribe
	public void update(MsgEvent event){
		Log.i(TAG, "update: sessionUserId"+session.getToId());
		Log.i(TAG, "update: Msg "+GsonUtil.getGson().toJson(event.getMsg()));
		if (event.getMsg().getFromId() == session.getToId() || event.getMsg().getToId() == session.getToId()){
			Log.i(TAG, "update: 处理接受到的消息！");
			adapter.update(event.getMsg());
		}
	}
}
