package vip.jokor.im.pages.util.userinfo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.im.MsgEvent;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.UserBean;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.pages.main.main_page.friends.DelFriendEvent;
import vip.jokor.im.presenter.ChatPresenter;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.pages.main.main_page.friends.FriendEvent;
import vip.jokor.im.presenter.UserPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import vip.jokor.im.wedgit.util.UcropUtil;
import vip.jokor.im.util.glide.MediaLoader;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
	
	private final String TAG = "UserInfoActivity";


	UserBean user;
	boolean isStranger = false;

	private View myView;
	private ListView listview;
	private UserInfoAdapter adapter;

	private View headerview;
	private TextView tv_name;
	private TextView tv_tel;
	private TextView tv_sign;

    private static List<FriendsArticlesBean.ArticlesBean> datas = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView = getLayoutInflater().inflate(R.layout.activity_user_info,null);
		setContentView(myView);
		initData();
//		initMenu();
		initToolBar();
		initHeaderView();
		initImg();
		initList();
	}

	private void initData(){
		String strUserInfo = getIntent().getStringExtra("userInfo");
		Log.e(TAG, "initData: "+strUserInfo );
		user = GsonUtil.getGson().fromJson(strUserInfo,UserBean.class);
		if (Datas.getFriends_articles()!=null && Datas.getFriends_articles().size()>0){
		    for ( int i = 0; i<Datas.getFriends_articles().size(); i++ ){
		        if (Datas.getFriends_articles().get(i).getArticle().getBelongId() == Datas.getUserInfo().getId()){
		            datas.add(Datas.getFriends_articles().get(i));
                }
            }
			Log.e(TAG, "initData: datas  "+datas.size() );
		    Datas.setSelf_articles(datas);
        }
	}

	private void initToolBar(){
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		findViewById(R.id.container).setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setElevation(0);  //去掉底部阴影
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_info_stranger, menu);
		return true;
	}

	//设置监听事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: finish();break;
			case R.id.add :addFriend();break;
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
								EventBus.getDefault().postSticky(new DelFriendEvent(user.getId()));
								finish();
							}else {
								ShowUtil.showToast(UserInfoActivity.this,"删除好友失败");
							}
						} catch (JSONException e) {
							ShowUtil.showToast(UserInfoActivity.this,"参数错误");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(VolleyError error) {
						Log.e(TAG, "删除好友 onFailure: "+error.getMessage() );
					}
				};
				UserPresenter.getInstance().deleteFriend(TAG,user.getId(),callback);
				break;
		}
		return true;
	}

	private void initHeaderView(){
		headerview = getLayoutInflater().inflate(R.layout.user_info_header,null);
		tv_name = headerview.findViewById(R.id.tv_name);
		tv_tel = headerview.findViewById(R.id.tv_tel);
		tv_sign = headerview.findViewById(R.id.tv_sign);

		tv_name.setText(user.getUserName()+"（"+user.getName()+"）");
		tv_tel.setText("tel : "+user.getTel());
		tv_sign.setText(user.getSign());
	}

	private void initList(){
		listview = findViewById(R.id.listview);
		listview.addHeaderView(headerview);
		adapter = new UserInfoAdapter(this,datas);
		listview.setAdapter(adapter);
	}

	private void initImg(){
		ImageView icon = headerview.findViewById(R.id.icon);
		Glide.with(this)
				.load(user.getIcon())
				.apply(MediaLoader.getOption())
				.into(icon);
		if (user.getId() == Datas.getUserInfo().getId()) {
			Widget widget = Widget.newDarkBuilder(UserInfoActivity.this)
					.title("选择头像") // Title.
					.statusBarColor(getResources().getColor(R.color.black,null)) // StatusBar color.
					.toolBarColor(getResources().getColor(R.color.black,null)) // Toolbar color.
					.navigationBarColor(getResources().getColor(R.color.black,null)) // Virtual NavigationBar color of Android5.0+.
					.mediaItemCheckSelector(getResources().getColor(R.color.grey_dark,null), getResources().getColor(R.color.black,null)) // Image or video selection box.
					.bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
					.buttonStyle (// Used to configure the style of button when the image/video is not found.
							Widget.ButtonStyle.newLightBuilder(UserInfoActivity.this) // With Widget's Builder model.
									.setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
									.build()
					)
					.build();
			icon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Album.image(UserInfoActivity.this) // 选择图片。
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
									String ImgPth = UcropUtil.startUCrop(
											UserInfoActivity.this,
											result.get(0).getPath(),
											0x01,
											1,
											1);
									Glide.with(UserInfoActivity.this)
											.load(ImgPth)
											.into(icon);
								}
							})
							.onCancel(new Action<String>() {
								@Override
								public void onAction(@NonNull String result) {
								
								}
							})
							.start();
				}
			});
		}
	}

	private void addFriend(){
		//这里要先验证该用户是否是好友
		View contentView = getLayoutInflater().inflate(R.layout.pop_add_friend,null);
		EditText et_tel = contentView.findViewById(R.id.tel);
		et_tel.setInputType(InputType.TYPE_CLASS_TEXT);
		et_tel.setHint("验证消息");
		Spinner type = contentView.findViewById(R.id.type);
		type.setVisibility(View.VISIBLE);
		String[] strSpinnerItems = new String[Datas.getFriendBean().getCategorys().size()];
		for (int j=0;j<5;j++){
			strSpinnerItems[j]=Datas.getFriendBean().getCategorys().get(j).getName();
		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(UserInfoActivity.this,R.layout.appoint_select,strSpinnerItems);
		spinnerAdapter.setDropDownViewResource(R.layout.appoint_item);
		type.setAdapter(spinnerAdapter);
		type.setSelection(1);
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserInfoActivity.this);
		android.app.AlertDialog dialog = builder.setTitle("添加好友")
				.setView(contentView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						HttpCallback callback = new HttpCallback() {
							@Override
							public void onSuccess(String t) {
								Log.e(TAG, "addFriend onSuccess: "+t );
								try {
									JSONObject jb = new JSONObject(t);
									int status = jb.getInt("status");
									if (status == 200){
										ShowUtil.showToast(getApplicationContext(),"添加好友成功");
										//在这里需要更新UI界面
										FriendsBean friendsBean = GsonUtil.getGson().fromJson(jb.getString("new_friend"),FriendsBean.class);
										Datas.getFriendBean().getFriends().add(friendsBean);
										EventBus.getDefault().postSticky(new FriendEvent(TAG,friendsBean));
										//向对方推送添加好友请求
										Msg msg = new Msg();
										msg.setMsgUsed(Msg.MSG_USED_ADD_FRIEND);
										msg.setMsgFrom(Msg.MSG_FROM_FRIEND);
										msg.setFromId(Datas.getUserInfo().getId());
										msg.setToId(user.getId());
										msg.setUsername(Datas.getUserInfo().getUserName());
										MsgEvent msgEvent = new MsgEvent(TAG,msg);
										EventBus.getDefault().postSticky(msgEvent);
										//打开会话
										MainPresenter.getInstance().openP2PSession(friendsBean,UserInfoActivity.this,true);
										//关闭当前页面
										finish();
									}else {
										String str = jb.getString("msg");
										if (str.equals("该用户已经是你的好友了哦")){
											Snackbar.make(myView,"该用户已经是你的好友了哦", Snackbar.LENGTH_SHORT).show();
										}else{
											Snackbar.make(myView,"该用户不存在", Snackbar.LENGTH_SHORT).show();
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(VolleyError error) {
								Log.e(TAG, "addFriend onFailure: "+error.getMessage() );
								Snackbar.make(myView,"网络错误！", Snackbar.LENGTH_SHORT).show();
							}
						};
						UserPresenter.getInstance().addFriend(Datas.getFriendBean().getCategorys().get((int) type.getSelectedItemId()).getId(),et_tel.getText().toString(),user.getId(),callback);
					}
				})
				.create();
		dialog.show();
	}

}
