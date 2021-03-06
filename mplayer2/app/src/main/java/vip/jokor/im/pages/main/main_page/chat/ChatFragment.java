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
import vip.jokor.im.base.Config;
import vip.jokor.im.base.Datas;
import vip.jokor.im.im.MsgEvent;
import vip.jokor.im.model.DataManager;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.bean.UserBean;
import vip.jokor.im.model.db.DBManager;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.model.db.Session;
import vip.jokor.im.model.db.Session_;
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
import org.json.JSONException;
import org.json.JSONObject;

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
				adapter.notifyDataSetChanged();
				presenter.clearSessionCount(session);
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
				name.setText(item.getUserName());
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
				checkDisRedCircle();
				notifyDataSetChanged();
				Session session = DBManager.getInstance().getSessionBox().query()
						.equal(Session_.Belong, Datas.getUserInfo().getId())
						.equal(Session_.msgFrom, item.getMsgFrom())
						.equal(Session_.toId, item.getToId())
						.build()
						.findFirst();

				if (session !=null ){
					Log.e(TAG, "getView: 设置会话用户名"+item.getUserName() );
					session.setUserName(item.getUserName());
					session.setIcon(item.getIcon());
					session.setTmpMsgCount(0);
					DBManager.getInstance().getSessionBox().put(session);
					Log.e(TAG, "getView: 保存会话 "+GsonUtil.getGson().toJson(session) );
					intent.putExtra("session", GsonUtil.getGson().toJson(session));
					startActivityForResult(intent,0);
				}else {
					Log.e(TAG, "getView: 保存会话 "+GsonUtil.getGson().toJson(item) );
					DBManager.getInstance().getSessionBox().put(item);
					intent.putExtra("session", GsonUtil.getGson().toJson(item));
					startActivityForResult(intent,0);
				}

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
		Log.e(TAG, "update: "+GsonUtil.getGson().toJson(event));
		ThreadUtil.startThreadInPool(() -> {
			if (event.getMsg().getMsgUsed() == Msg.MSG_USED_CHAT){
				Session session = null;
				Msg msg = event.getMsg();
				Log.i(TAG, "update: msg  "+GsonUtil.getGson().toJson(msg));
				for (Session item :sessions){
					Log.e(TAG, "update: session "+GsonUtil.getGson().toJson(item));
					if ( item.getMsgFrom()  == msg.getMsgFrom() && (item.getToId() == msg.getToId() || item.getToId() == msg.getFromId()) ){
						session = item;
						sessions.remove(item);
						sessions.add(0,session);
						Log.i(TAG, "update: 会话存在");
						break;
					}

				}
				if (session==null){
					Log.e(TAG, "update: 会话不存在，开始新建会话！");
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
										try {
											JSONObject jb = new JSONObject(t);
											JSONObject jUser = jb.getJSONObject("user");
											String username = jUser.getString("UserName");
											String icon = jUser.getString("Icon");
											for (Session item :sessions){
												if ( item.getMsgFrom()  == msg.getMsgFrom() && (item.getToId() == msg.getToId() || item.getToId() == msg.getFromId()) ){
													Log.e(TAG, "onSuccess: 设置会话用户名"+username );
													item.setUserName(username);
													item.setIcon(icon);
													if (getActivity()!=null){
														getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
													}
													break;
												}
											}
											Session session = DBManager.getInstance().getSessionBox().query()
													.equal(Session_.Belong, Datas.getUserInfo().getId())
													.equal(Session_.msgFrom, msg.getMsgFrom())
													.equal(Session_.toId, msg.getFromId())
													.or()
													.equal(Session_.toId, msg.getToId())
													.build()
													.findFirst();
											Log.e(TAG, "onSuccess: 设置会话用户名"+username );
											session.setUserName(username);
											session.setIcon(icon);
											Log.e(TAG, "onSuccess: 保存会话"+GsonUtil.getGson().toJson(session) );
											DBManager.getInstance().getSessionBox().put(session);
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}

									@Override
									public void onFailure(int errorNo, String strMsg) {
										Log.e(TAG,  "loadUserInfo onFailure: "+errorNo+strMsg );
									}
								};
								FriendsBean user = UserPresenter.getInstance().loadUserInfo(userId,callback);
								if (user!=null){
									session.setIcon(user.getFriend().getIcon());
									String strUsername = user.getMsg();
									if (strUsername==null || strUsername.equals("")){
										strUsername = user.getFriend().getUserName();
									}
									if (strUsername==null || strUsername.equals("")){
										Random r = new Random(1);
										int random = r.nextInt(100);
										if (random<50){
											strUsername = "黑脸"+random;
										}else {
											strUsername = "白脸"+random;
										}
									}
									Log.e(TAG, "update: 设置会话用户名 "+strUsername );
									session.setUserName(strUsername);
								}
								session.setToId(userId);
								break;
							case Msg.MSG_FROM_GROUP:
								GroupBean groupBean = GroupPresenter.getInstance().loadGroupInfo(msg);
								session.setIcon(groupBean.getIcon());
								Log.e(TAG, "update: 设置会话用户名"+groupBean.getName() );
								session.setUserName(groupBean.getName());
								session.setToId(groupBean.getId());
								break;
						}
						session.setMsgFrom(event.getMsg().getMsgFrom());
						session.setBelong(Datas.getUserInfo().getId());
						session.setTmpTime(event.getMsg().getCreateTime());
						sessions.add(0,session);
					}else {
						session = new Session();
						//加载名称和头像
						switch (msg.getMsgFrom()){
							case Msg.MSG_FROM_FRIEND:
								if (msg.getToId() == Datas.getUserInfo().getId()){
									session.setToId(msg.getFromId());
								}
								if (msg.getFromId() == Datas.getUserInfo().getId()){

									session.setToId(msg.getToId());
								}
								break;
							case Msg.MSG_FROM_GROUP:
								GroupBean groupBean = GroupPresenter.getInstance().loadGroupInfo(msg);
								session.setIcon(groupBean.getIcon());
								Log.e(TAG, "update: 设置会话用户名"+groupBean.getName() );
								session.setUserName(groupBean.getName());
								session.setToId(groupBean.getId());
								break;
						}
						session.setMsgFrom(event.getMsg().getMsgFrom());
						session.setBelong(Datas.getUserInfo().getId());
						session.setTmpTime(event.getMsg().getCreateTime());
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
					session.setUserName(msg.getUsername());
					session.setIcon(msg.getIcon());
					if (session.getTmpMsgCount()<=0){ session.setTmpMsgCount(1); }
					else if (session.getTmpMsgCount()>=99){session.setTmpMsgCount(99);}
					else { session.setTmpMsgCount(session.getTmpMsgCount()+1);}
				}
			}
		});
		if (getActivity()!=null){
			getActivity().runOnUiThread(() -> {
				adapter.notifyDataSetChanged();
				if ( getActivity()instanceof MainActivity &&
						!Config.isShowTab1() &&
						event.getMsg().getFromId() != Datas.getUserInfo().getId()
				){
					((MainActivity)getActivity()).showRedCircle1();
				}
			});

		}
	}

	//控制底部小红点状态
	private void checkShowRedCircle(){
		if (!Config.isShowTab1() && sessions!=null){
			for (int i=0;i<sessions.size();i++){
				if (sessions.get(i).getTmpMsgCount()>0){
					if(getActivity()instanceof MainActivity){
						((MainActivity)getActivity()).showRedCircle1();
						break;
					}
				}
			}
		}
	}

	private void checkDisRedCircle(){
		if (Config.isShowTab1() && sessions!=null){
			boolean flag = false;
			for (int i=0;i<sessions.size();i++){
				if (sessions.get(i).getTmpMsgCount()>0){
					flag = true;
					break;
				}
			}
			if ( !flag && getActivity()instanceof MainActivity ) {
				((MainActivity)getActivity()).disableRedCircle1();
			}
		}
	}
	
}
