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


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.GetFriendBean;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.pages.main.MainActivity;
import vip.jokor.im.pages.main.main_page.friends.adapter.NodeTreeAdapter;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.FirstNode;
import vip.jokor.im.presenter.FriendPresenter;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.pages.main.main_page.group.CreateGroupActivity;
import vip.jokor.im.pages.util.userinfo.UserInfoActivity;
import vip.jokor.im.pages.main.main_page.group.GroupInfoActivity;
import vip.jokor.im.pages.main.main_page.group.GroupPresenter;
import vip.jokor.im.pages.main.main_page.search.SearchActivity;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {
	
	private static final String TAG = "FriendsFragment";
	
	private View contentView;

	View circle;
	NodeTreeAdapter adapter;
	RecyclerView list;

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
		circle = contentView.findViewById(R.id.circle);	//		小红点
		initToolBar();
		initSearch();
		initNewFriends();
		initList(new GetFriendBean(),new ArrayList<>());
		initData();
		EventBus.getDefault().register(this);
		return contentView;
	}

	private void initToolBar(){
		Toolbar toolbar = contentView.findViewById(R.id.toolbar);
		toolbar.setTitle("");
		AppBarLayout app_bar_layout = contentView.findViewById(R.id.app_bar_layout);
		app_bar_layout.setPadding(0, SizeUtil.getStatusBarHeight(getContext()),0,0);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), MainActivity.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.syncState();
	}

	private void initNewFriends(){
		View new_friend = contentView.findViewById(R.id.new_friend);
		new_friend.setOnClickListener(v -> startActivity(new Intent(getContext(),ConfirmActivity.class)));
	}

	private void initList(GetFriendBean friendBean,List<GroupBean> groupBeans){
		list = contentView.findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new NodeTreeAdapter();
		list.setAdapter(adapter);
		adapter.setNewData(changeData(friendBean));
	}

	private List<BaseNode> changeData(GetFriendBean getFriendBean) {
		List<BaseNode> datas = new ArrayList<>();
		if (getFriendBean.getFriends()!=null && getFriendBean.getCategorys()!=null){
			for (int i=0 ; i<getFriendBean.getCategorys().size();i++){
				FirstNode data = new FirstNode(new ArrayList<>(),getFriendBean.getCategorys().get(i));
				for (int j=0;j<getFriendBean.getFriends().size();j++){
					if (getFriendBean.getFriends().get(j).getCategoryId() == getFriendBean.getCategorys().get(i).getId()){
						data.friends.add(getFriendBean.getFriends().get(j));
					}
				}
				datas.add(data);
			}
		}else {
			getFriendBean.setCategorys(new ArrayList<>());
			getFriendBean.setFriends(new ArrayList<>());
		}
		return datas;
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
				initList(Datas.getFriendBean(),new ArrayList<>());
			}
			@Override
			public void onFailure(VolleyError error) {
				Log.e(TAG, "getFriemds: "+error.getMessage() );
			}
		};
		FriendPresenter.getInstance().getFriemds(callback);
	}
	
	private void initSearch(){
		contentView.findViewById(R.id.search).setOnClickListener(view -> startActivity(new Intent(getContext(), SearchActivity.class)));
	}

	@Subscribe
	public void update(FriendEvent event){
		Log.i(TAG, "update: update friend list !"+GsonUtil.getGson().toJson(event.friendsBean));
		//添加好友成功后更新好友列表

		// 新增node
		list.postDelayed(new Runnable() {
			@Override
			public void run() {
				List<BaseNode> datas = adapter.getData();
				boolean flag = false;
				int categoryIndex = -1;
				for (int i=0;i<datas.size();i++){
					FirstNode firstNode = (FirstNode) datas.get(i);
					//找出分类索引
					if (firstNode.categorysBean.getId() == event.friendsBean.getCategoryId())categoryIndex = i;
					//判断是否已经是好友
					for (int j=0;j<firstNode.friends.size();j++){
						FriendsBean secondNode = (FriendsBean) firstNode.friends.get(j);
						if (secondNode.getFriend().getId() == event.friendsBean.getFriend().getId()){
							flag = true;
							break;
						}
					}
					if (flag){
						break;
					}
				}
				//如果好友列表里面没有这个好友，那么添加到好友列表
				if (!flag){
					if (categoryIndex == -1){
						categoryIndex = 0;
					}
					adapter.nodeAddData(adapter.getData().get(categoryIndex),event.friendsBean);
				}
			}
		}, 100);

	}

	@Subscribe
	public void update(NewFriendEvent event){
		boolean flag = false;
		for (int i=0;i<Datas.getFriendBean().getFriends().size();i++){
			if (Datas.getFriendBean().getFriends().get(i).getFriend().getId() == event.getUserId()){
				flag = true;
				break;
			}
		}
		if (!flag){
			ShowUtil.sendSimpleNotification(getContext(),"新朋友",event.getUsername()+" 请求添加好友");
			getActivity().runOnUiThread(() -> circle.setVisibility(View.VISIBLE));
		}else {
			Log.e(TAG, "update: 添加好友好友已经存在" );
		}
	}

	@Subscribe
	public void update(DelFriendEvent event){

		// 删除好友
		list.postDelayed(new Runnable() {
			@Override
			public void run() {
				List<BaseNode> datas = adapter.getData();
				boolean flag = false;
				for (int i=0;i<datas.size();i++){
					FirstNode firstNode = (FirstNode) datas.get(i);
					//判断是否已经是好友
					for (int j=0;j<firstNode.friends.size();j++){
						FriendsBean secondNode = (FriendsBean) firstNode.friends.get(j);
						if (secondNode.getFriend().getId() == event.getUserId()){
							adapter.nodeRemoveData(adapter.getData().get(i),j);
							flag = true;
							break;
						}
					}
					if (flag){
						break;
					}
				}
			}
		}, 100);
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
