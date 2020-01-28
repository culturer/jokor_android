package vip.jokor.im.pages.main.main_page.friends;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.im.MsgService;
import vip.jokor.im.model.bean.GetFriendBean;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.bean.GroupListBean;
import vip.jokor.im.pages.main.MainActivity;
import vip.jokor.im.presenter.FriendPresenter;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.pages.main.main_page.group.CreateGroupActivity;
import vip.jokor.im.pages.util.userinfo.UserInfoActivity;
import vip.jokor.im.pages.main.main_page.group.GroupEvent;
import vip.jokor.im.pages.main.main_page.group.GroupInfoActivity;
import vip.jokor.im.pages.main.main_page.group.GroupPresenter;
import vip.jokor.im.pages.main.main_page.search.SearchActivity;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.wedgit.iphone_treeview.IphoneTreeView;
import vip.jokor.im.wedgit.iphone_treeview.TreeViewAdapter;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import vip.jokor.im.model.db.Msg;


public class FriendsFragment extends Fragment {
	
	private static final String TAG = "FriendsFragment";
	
	private View contentView;
	private IphoneTreeView friend_list;

	TreeViewAdapter adapter;
	
	public FriendsFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_friends, container, false);
		initToolBar();
		initSearch();
		initNewFriends();
		initList(new GetFriendBean());
		initData();
		EventBus.getDefault().register(this);
		return contentView;
	}

	private void initToolBar(){
		Toolbar toolbar = contentView.findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.inflateMenu(R.menu.main_friends);
		AppBarLayout app_bar_layout = contentView.findViewById(R.id.app_bar_layout);
		app_bar_layout.setPadding(0, SizeUtil.getStatusBarHeight(getContext()),0,0);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), MainActivity.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.syncState();
	}

	private void initNewFriends(){
		TextView new_friend = contentView.findViewById(R.id.new_friend);
		new_friend.setOnClickListener(v -> startActivity(new Intent(getContext(),ConfirmActivity.class)));
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.main_friends,menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_friend: add_friend();break;
            case R.id.black_friend:black_friend();break;
            case R.id.create_group:create_group();break;
            case R.id.search_group:search_group();break;
        }
        return true;
    }

    @Override
	public void onDestroy() {
		Glide.with(this).pauseRequests();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initData(){
		HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(String t) {
				Log.e(TAG, "onSuccess: "+t );
				Gson gson = GsonUtil.getGson();
				GetFriendBean friendBean = gson.fromJson(t,GetFriendBean.class);
				Datas.setFriendBean(friendBean);
				initList(Datas.getFriendBean());
			}
			@Override
			public void onFailure(VolleyError error) {
				Log.e(TAG, "getFriemds: "+error.getMessage() );
			}
		};
		FriendPresenter.getInstance().getFriemds(callback);
		HttpCallback callback1 = new HttpCallback() {
			@Override
			public void onSuccess(String t) {
				Log.i(TAG, "get group onSuccess: "+t);
				try {
					JSONObject jb = new JSONObject(t);
					int status = jb.getInt("status");
					if (status == 200){
						GroupListBean groupListBean = GsonUtil.getGson().fromJson(t,GroupListBean.class);
						for (int i=0;i<groupListBean.getGroups().size();i++){
							Datas.getGroups().add(groupListBean.getGroups().get(i).getGroup());
						}
						Log.i(TAG, "init group: "+GsonUtil.getGson().toJson(Datas.getGroups()));
						MsgService.subscribe();
					}
					adapter.update(Datas.getGroups());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(VolleyError error) {
				Log.i(TAG, "onFailure: "+error.getMessage());
			}
		};
		GroupPresenter.getInstance().getGroups(callback1);
	}
	
	private void initSearch(){
		contentView.findViewById(R.id.search).setOnClickListener(view -> startActivity(new Intent(getContext(), SearchActivity.class)));
	}
	
	private void initList(GetFriendBean friendBean){
		Log.i(TAG, "initList: 开始初始化好友列表！");
		friend_list = contentView.findViewById(R.id.friend_list);
		friend_list.setHeaderView(getLayoutInflater().inflate(R.layout.iphonetreeview_list_head_view, friend_list, false));
		friend_list.setGroupIndicator(null);
		//		初始化好友列表
		adapter = new TreeViewAdapter(getContext(),this,friend_list, friendBean,Datas.getGroups());
		friend_list.setAdapter(adapter);
		friend_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				int type = adapter.getChildType(groupPosition);
				if (type == Msg.MSG_FROM_FRIEND)MainPresenter.getInstance().openP2PSession(adapter.getChild(groupPosition,childPosition),getContext(),false);
				if (type == Msg.MSG_FROM_GROUP)MainPresenter.getInstance().openGroupSession(adapter.getChatGroup(childPosition),getContext());
				return true;
			}
		});
	}

	@Subscribe
	public void update(FriendEvent event){
		Log.i(TAG, "update: update friend list !");
		Datas.getFriendBean().getFriends().add(event.friendsBean);
		adapter.update(event.friendsBean);
	}

	@Subscribe
	public void update(GroupEvent event){
        Log.i(TAG, "update: update group list !"+GsonUtil.getGson().toJson(event));
		GroupBean groupBean = event.getGroupBean();
		adapter.update(groupBean);
	}

	private void add_friend(){
		View contentView = getLayoutInflater().inflate(R.layout.pop_add_friend,null);
		EditText et_tel = contentView.findViewById(R.id.tel);
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
		final android.app.AlertDialog dialog = builder.setTitle("添加好友")
				.setView(contentView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HttpCallback callback = new HttpCallback() {
							@Override
							public void onSuccess(String t) {
								Log.i(TAG, "onSuccess: "+t);
                                try {
                                    JSONObject jb = new JSONObject(t);
                                    int status = jb.getInt("status");
                                    if (status == 200){
                                        String strUser = jb.getString("user");
                                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                                        intent.putExtra("userInfo",strUser);
                                        startActivity(intent);
                                    }else {
										ShowUtil.showToast(getContext(),"此用户不存在");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
							@Override
							public void onFailure(int errorNo, String strMsg) {
								Log.i(TAG, "onFailure: "+errorNo+","+strMsg);
							}
						};
						MainPresenter.getInstance().getUserInfo(et_tel.getText().toString(),callback);
					}
				})
				.create();
		dialog.show();
	}

	private void black_friend(){
		ShowUtil.showToast(getContext(),"敬请期待");
	}

	private void create_group(){
		Intent intent = new Intent(getContext(), CreateGroupActivity.class);
		startActivity(intent);
	}

	private void search_group(){
		View contentView = getLayoutInflater().inflate(R.layout.pop_add_friend,null);
		EditText et_tel = contentView.findViewById(R.id.tel);
		et_tel.setHint("请输入群号码");
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
		final android.app.AlertDialog dialog = builder.setTitle("添加群")
				.setView(contentView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HttpCallback callback = new HttpCallback() {
							@Override
							public void onSuccess(String t) {
                                Log.i(TAG, "search_group onSuccess: "+t);
								try {
									JSONObject jb = new JSONObject(t);
									int status = jb.getInt("status");
									if (status == 200){
										String str =  jb.getString("group");
										//这里需要对群消息做一些处理
										Intent intent = new Intent(getContext(), GroupInfoActivity.class);
										intent.putExtra("strGroup",str);
										startActivity(intent);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							@Override
							public void onFailure(int errorNo, String strMsg) {
								Log.i(TAG, "search_group onFailure: "+errorNo+","+strMsg);
							}
						};
						GroupPresenter.getInstance().search_group(et_tel.getText().toString(),callback);
					}
				})
				.create();
		dialog.show();
	}
}
