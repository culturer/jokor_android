package com.jokor.base.pages.main.main_page.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.jokor.base.R;
import com.jokor.base.base.Datas;
import com.jokor.base.im.MsgEvent;
import com.jokor.base.model.bean.GroupBean;
import com.jokor.base.model.bean.UserBean;
import com.jokor.base.model.db.DBManager;
import com.jokor.base.model.db.Msg;
import com.jokor.base.model.db.Session;
import com.jokor.base.pages.main.MainActivity;
import com.jokor.base.pages.main.main_page.chat.chat_page.ChatActivity;
import com.jokor.base.pages.main.main_page.group.GroupPresenter;
import com.jokor.base.presenter.ChatPresenter;
import com.jokor.base.presenter.UserPresenter;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.ThreadUtil;
import com.jokor.base.wedgit.util.EmojiUtil;
import com.jokor.base.util.base.TimeUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.jokor.base.model.db.Msg.MSG_FROM_FRIEND;
import static com.jokor.base.model.db.Msg.MSG_FROM_GROUP;
import static com.jokor.base.model.db.Msg.MSG_TYPE_AUDIO;
import static com.jokor.base.model.db.Msg.MSG_TYPE_IMG;
import static com.jokor.base.model.db.Msg.MSG_TYPE_TEXT;
import static com.jokor.base.model.db.Msg.MSG_TYPE_VIDEO;

import static com.jokor.base.pages.main.main_page.chat.chat_page.ChatActivity.CHAT_TYPE;

public class ChatFragment extends Fragment {

	private static String TAG = "ChatFragment";

	private View contentView;
	private MsgAdapter adapter;

	RequestOptions options = new RequestOptions()
			.error(R.mipmap.img_error)//加载错误之后的错误图
			.override(400,400)//指定图片的尺寸
			.circleCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片;

	ChatPresenter presenter;
	private List<Session> sessions;
	
	public ChatFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate: " );
		setHasOptionsMenu(true);
		initData();
	}
	
	private void initData(){
		presenter = ChatPresenter.getInstance();
		sessions = presenter.getSessions();
		if (sessions == null)sessions = new ArrayList<>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView: " );
		contentView = inflater.inflate(R.layout.fragment_chat, container, false);
		initToolBar();
		initList();
		EventBus.getDefault().register(this);
		return contentView;
	}

	private void initToolBar(){
		Toolbar toolbar = contentView.findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.inflateMenu(R.menu.main_chat);
		AppBarLayout app_bar_layout = contentView.findViewById(R.id.app_bar_layout);
		app_bar_layout.setPadding(0, SizeUtil.getStatusBarHeight(getContext()),0,0);
		Log.e(TAG, "initToolBar: " );
		if (getActivity()!=null) { ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); }
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), MainActivity.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.syncState();
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
//        switch (id){
//            case R.id.confirm_friend: confirm_friend();break;
//            case R.id.add_friend:add_friend();break;
//            case R.id.add_group:add_Group();break;
//            case R.id.create_group:create__Group();break;
//        }
		return true;
	}


	private void initList(){
		ListView msg_list = contentView.findViewById(R.id.msg_list);
		adapter = new MsgAdapter(sessions,getContext());
		msg_list.setAdapter(adapter);
		msg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Session session = adapter.getItem(i);
				session.setTmpMsgCount(0);
				presenter.clearSessionCount(session);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private class MsgAdapter extends BaseAdapter{

		private List<Session> sessions;
		private Context context;
		Intent intent = new Intent(getContext(),ChatActivity.class);

		public MsgAdapter(List<Session> sessions, Context context) {
			this.sessions = sessions;
			this.context = context;
		}

		@Override
		public int getCount() {
			return sessions.size();
		}
		
		@Override
		public Session getItem(int position) {
			return sessions.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return sessions.get(position).getId();
		}
		
		@SuppressLint({"ViewHolder", "SetTextI18n"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.chat_msg_listitem,null);
			ImageView icon = convertView.findViewById(R.id.icon);
			TextView name = convertView.findViewById(R.id.name);
			TextView time = convertView.findViewById(R.id.time);
			TextView msg = convertView.findViewById(R.id.msg);
			TextView count = convertView.findViewById(R.id.count);
			TextView label = convertView.findViewById(R.id.label);

			Session item = getItem(position);

			Glide.with(getContext())
					.load(item.getIcon())
					.apply(options)
					.into(icon);
			//发送时间
			time.setText(TimeUtil.date2Str(item.getTmpTime()));
			//消息内容
			String str = item.getTmpMsg();
			//消息具体内容
			//emoji处理
			String check = "f0[0-9]{2}|f10[0-7]";              //正则表达式，用来判断消息内是否有表情
			//解析消息内容
			SpannableString spannableString = EmojiUtil.getExpressionString(getContext(), str, check,60);
			msg.setText(spannableString);
			//消息条数
			if (item.getTmpMsgCount()>0){
				count.setVisibility(View.VISIBLE);
				count.setText(""+item.getTmpMsgCount());
			}else {
				count.setVisibility(View.GONE);
			}

			//单聊
			if (item.getMsgFrom() == MSG_FROM_FRIEND){
				intent.putExtra(CHAT_TYPE, MSG_FROM_FRIEND);
				name.setText("["+item.getMsg()+"] "+item.getUserName());
				label.setText("vip");
			}
			//群聊
			if (item.getMsgFrom() == MSG_FROM_GROUP){
				intent.putExtra(CHAT_TYPE, MSG_FROM_GROUP);
				name.setText("[群聊] "+item.getUserName());
				label.setText(""+item.getGrad()+"级");
			}
			convertView.setOnClickListener(v -> {
				item.setTmpMsgCount(0);
				notifyDataSetChanged();
				DBManager.getInstance().getSessionBox().put(item);
				intent.putExtra("session", GsonUtil.getGson().toJson(item));
				startActivity(intent);
			});
			return convertView;
		}
	}

	@Subscribe
	public void update(MsgEvent event){
		Log.i(TAG, "update: "+GsonUtil.getGson().toJson(event));
		ThreadUtil.startThreadInPool(() -> {
			Session session = null;
			Msg msg = event.getMsg();
			Log.i(TAG, "update: msg  "+GsonUtil.getGson().toJson(msg));
			for (Session item :sessions){
				Log.i(TAG, "update: session "+GsonUtil.getGson().toJson(item));
				if ( item.getMsgFrom()  == msg.getMsgFrom() && (item.getToId() == msg.getToId() || item.getToId() == msg.getFromId()) ){
					session = item;
					sessions.remove(item);
					sessions.add(0,session);
					Log.i(TAG, "update: 会话存在");
					break;
				}
			}
			if (session==null){
				Log.i(TAG, "update: 会话不存在，开始新建会话！");
				if (msg.getFromId() == Datas.getUserInfo().getId()){
					//消息是自己发的
					long userId = msg.getToId();
					session = new Session();
					//加载名称和头像
					switch (msg.getMsgFrom()){
						case MSG_FROM_FRIEND:
							HttpCallback callback = new HttpCallback() {
								@Override
								public void onSuccess(String t) {
									Log.e(TAG, "onSuccess: "+t );
								}

								@Override
								public void onFailure(int errorNo, String strMsg) {
									Log.e(TAG, "onFailure: "+errorNo+strMsg );
								}
							};
							UserBean user = UserPresenter.getInstance().loadUserInfo(userId,callback);
							if (user!=null){
								session.setIcon(user.getIcon());
								String strUsername = user.getName();
								if (strUsername==null || strUsername.equals("")){
									strUsername = user.getUserName();
									if (strUsername==null || strUsername.equals("")){
										Random r = new Random(1);
										int random = r.nextInt(100);
										if (random<50){
											strUsername = "黑脸"+random;
										}else {
											strUsername = "白脸"+random;
										}
									}
								}
								session.setUserName(strUsername);
								session.setId(userId);
								session.setToId(userId);
							}
							break;
						case MSG_FROM_GROUP:
							GroupBean groupBean = GroupPresenter.getInstance().loadGroupInfo(msg);
							session.setIcon(groupBean.getIcon());
							session.setUserName(groupBean.getName());
							session.setId(groupBean.getId());
							session.setToId(groupBean.getId());
							break;
					}
					session.setMsgFrom(event.getMsg().getMsgFrom());
					session.setBelong(Datas.getUserInfo().getId());
					session.setTmpTime(event.getMsg().getCreateTime());
					session.setMsg(event.getMsg().getUsername());
					sessions.add(0,session);
				}else {
					session = new Session();
					//加载名称和头像
					switch (msg.getMsgFrom()){
						case MSG_FROM_FRIEND:
							session.setIcon(msg.getIcon());
							session.setUserName(msg.getUsername());
							if (msg.getToId() == Datas.getUserInfo().getId()){
								session.setId(msg.getFromId());
								session.setToId(msg.getFromId());
							}
							if (msg.getFromId() == Datas.getUserInfo().getId()){
								session.setId(msg.getToId());
								session.setToId(msg.getToId());
							}
							break;
						case MSG_FROM_GROUP:
							GroupBean groupBean = GroupPresenter.getInstance().loadGroupInfo(msg);
							session.setIcon(groupBean.getIcon());
							session.setUserName(groupBean.getName());
							session.setId(groupBean.getId());
							session.setToId(groupBean.getId());
							break;
					}
					session.setMsgFrom(event.getMsg().getMsgFrom());
					session.setBelong(Datas.getUserInfo().getId());
					session.setTmpTime(event.getMsg().getCreateTime());
					session.setMsg(event.getMsg().getUsername());
					sessions.add(0,session);
				}
				Log.i(TAG, "update: 会话初始化完毕");
			}

			session.setTmpTime(event.getMsg().getCreateTime());
			switch (event.getMsg().getMsgType()){
				case MSG_TYPE_TEXT:session.setTmpMsg(event.getMsg().getData());break;
				case MSG_TYPE_IMG:session.setTmpMsg("[图片]");break;
				case MSG_TYPE_AUDIO:session.setTmpMsg("[语音]");break;
				case MSG_TYPE_VIDEO:session.setTmpMsg("[视频]");break;
			}
			if (event.getMsg().getFromId() == Datas.getUserInfo().getId()){
				//自己发的消息就不要设置未读消息了
				session.setTmpMsgCount(0);
			}else {
				if (session.getTmpMsgCount()<=0){ session.setTmpMsgCount(1); }
				else if (session.getTmpMsgCount()>=99){session.setTmpMsgCount(99);}
				else { session.setTmpMsgCount(session.getTmpMsgCount()+1);}
			}
		});
		if (getActivity()!=null){
			getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
		}
	}
	
}
