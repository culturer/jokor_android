package vip.jokor.im.pages.main.main_page.chat;

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

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.im.MsgEvent;
import vip.jokor.im.model.DataManager;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.bean.UserBean;
import vip.jokor.im.model.db.DBManager;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.model.db.Session;
import vip.jokor.im.pages.main.MainActivity;
import vip.jokor.im.pages.main.main_page.chat.chat_page.ChatActivity;
import vip.jokor.im.pages.main.main_page.friends.DelFriendEvent;
import vip.jokor.im.pages.main.main_page.group.GroupPresenter;
import vip.jokor.im.presenter.ChatPresenter;
import vip.jokor.im.presenter.UserPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.ThreadUtil;
import vip.jokor.im.wedgit.util.EmojiUtil;
import vip.jokor.im.util.base.TimeUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static vip.jokor.im.pages.main.main_page.chat.chat_page.ChatActivity.CHAT_TYPE;

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
			if (item.getTmpTime() == null){
				time.setText("");
			}else {
				time.setText(TimeUtil.date2Str(item.getTmpTime()));
			}

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
			if (item.getMsgFrom() == Msg.MSG_FROM_FRIEND){
				intent.putExtra(CHAT_TYPE, Msg.MSG_FROM_FRIEND);
				if (item.getMsg() == null || item.getMsg().equals("")){
					name.setText(item.getUserName());
				}else {
					name.setText(item.getMsg());
				}
				label.setText("vip");
			}
			//群聊
			if (item.getMsgFrom() == Msg.MSG_FROM_GROUP){
				intent.putExtra(CHAT_TYPE, Msg.MSG_FROM_GROUP);
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
	public void update(DelFriendEvent event){
		int index = -1;
		for (int i=0;i<sessions.size();i++){
			if (sessions.get(i).getToId() == event.getUserId()){
				index = i;
				break;
			}
		}
		if (index!=-1){
			ChatPresenter.getInstance().removeSession(sessions.get(index));
			sessions.remove(index);
			adapter.notifyDataSetChanged();
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
						case Msg.MSG_FROM_FRIEND:
							HttpCallback callback = new HttpCallback() {
								@Override
								public void onSuccess(String t) {
									Log.e(TAG, "loadUserInfo onSuccess: "+t );
								}

								@Override
								public void onFailure(int errorNo, String strMsg) {
									Log.e(TAG,  "loadUserInfo onFailure: "+errorNo+strMsg );
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
						case Msg.MSG_FROM_GROUP:
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
						case Msg.MSG_FROM_FRIEND:
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
						case Msg.MSG_FROM_GROUP:
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
				case Msg.MSG_TYPE_TEXT:session.setTmpMsg(event.getMsg().getData());break;
				case Msg.MSG_TYPE_IMG:session.setTmpMsg("[图片]");break;
				case Msg.MSG_TYPE_AUDIO:session.setTmpMsg("[语音]");break;
				case Msg.MSG_TYPE_VIDEO:session.setTmpMsg("[视频]");break;
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
