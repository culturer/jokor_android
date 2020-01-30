package vip.jokor.im.pages.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import vip.jokor.im.R;
import vip.jokor.im.base.BaseActivity;
import vip.jokor.im.base.Config;
import vip.jokor.im.base.Datas;
import vip.jokor.im.pages.main.main_page.friends.NewFriendEvent;
import vip.jokor.im.pages.util.SettingActivity;
import vip.jokor.im.pages.util.article.ArticleEvent;
import vip.jokor.im.pages.util.userinfo.UserInfoActivity;
import vip.jokor.im.pages.main.main_page.chat.ChatFragment;
import vip.jokor.im.pages.main.main_page.friends.ConfirmActivity;
import vip.jokor.im.pages.main.main_page.friends.FriendsFragment;
import vip.jokor.im.pages.login.LoginActivity;
import vip.jokor.im.pages.main.main_page.search.SearchActivity;
import vip.jokor.im.pages.main.main_page.square.SquareFragment;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.PreferenceUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.wedgit.util.QrCodeUtil;
import vip.jokor.im.util.glide.MediaLoader;
import vip.jokor.im.wedgit.bottom_navigation.BottomNavigationViewHelper;
import vip.jokor.im.wedgit.bottom_navigation.MyPagerAdapter;
import vip.jokor.im.wedgit.bottom_navigation.MyViewPager;
import vip.jokor.im.wedgit.util.ShowUtil;

import cn.jzvd.Jzvd;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

	private static final String TAG = "MainActivity";

	private MyViewPager pager;
	BottomNavigationView navigation;
	private MenuItem menuItem;
	public static DrawerLayout drawer;

	ImageView nav_header_icon;
	View circle0;
	View circle1;
	View circle2;

	MainPresenter presenter = MainPresenter.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		boolean isLogin = getIntent().getBooleanExtra("login",false);
		Log.i(TAG, "onCreate: isLogin "+isLogin);
		Log.i(TAG, "onCreate: isAutoLogin "+presenter.isAutoLogin());
		if (isLogin || presenter.isAutoLogin()){
			Log.i(TAG, "onCreate: 登录成功");
			initToolBar();
			initPage();
			Log.i(TAG, "onCreate: 启动IM服务");
			//启动IM服务
			presenter.startIM(this);
		}else {
			Log.i(TAG, "onCreate: 登录失败");
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initToolBar(){
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
		nav_header_icon = headerLayout.findViewById(R.id.nav_header_icon);

		if (Datas.getUserInfo().getIcon().equals("")){
			Glide.with(this)
					.load(R.mipmap.logo)
					.apply(MediaLoader.getOption())
					.into(nav_header_icon);
		}else {
			Glide.with(this)
					.load(Datas.getLogo())
					.apply(MediaLoader.getOption())
					.into(nav_header_icon);
		}

		TextView nav_header_name = headerLayout.findViewById(R.id.nav_header_name);
		TextView nav_header_tel = headerLayout.findViewById(R.id.nav_header_tel);
		nav_header_name.setText(Datas.getUserInfo().getUserName()+"["+Datas.getUserInfo().getName()+"]");
		nav_header_tel.setText(Datas.getUserInfo().getTel());
		headerLayout.setOnClickListener(view -> {
			Intent intent = new Intent(MainActivity.this,UserInfoActivity.class);
			intent.putExtra("userInfo", GsonUtil.getGson().toJson(Datas.getUserInfo()));
			startActivity(intent);
		});
	}

	private void initPage(){
		navigation = findViewById(R.id.navigation);
		//获取整个的NavigationView
		BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
		//这里就是获取所添加的每一个Tab(或者叫menu)，
		View tab = menuView.getChildAt(0);
		BottomNavigationItemView tab0 = (BottomNavigationItemView) tab;
		//加载我们的角标View，新创建的一个布局
		View badge = LayoutInflater.from(this).inflate(R.layout.main_bottom_circle_red, menuView, false);
		//添加到Tab上
		tab0.addView(badge);
		circle0 = badge.findViewById(R.id.circle);
		circle0.setVisibility(View.GONE);

		BottomNavigationItemView tab1 = (BottomNavigationItemView) menuView.getChildAt(1);
		//加载我们的角标View，新创建的一个布局
		View badge1 = LayoutInflater.from(this).inflate(R.layout.main_bottom_circle_red, menuView, false);
		//添加到Tab上
		tab1.addView(badge1);
		circle1 = badge1.findViewById(R.id.circle);
		circle1.setVisibility(View.GONE);

		BottomNavigationItemView tab2 = (BottomNavigationItemView) menuView.getChildAt(2);
		//加载我们的角标View，新创建的一个布局
		View badge2 = LayoutInflater.from(this).inflate(R.layout.main_bottom_circle_red, menuView, false);
		//添加到Tab上
		tab2.addView(badge2);
		circle2 = badge2.findViewById(R.id.circle);

		if (Config.isShowTab0()){
			circle0.setVisibility(View.VISIBLE);
		}else{
			circle0.setVisibility(View.GONE);
		}
		if (Config.isShowTab1()){
			circle1.setVisibility(View.VISIBLE);
		}else{
			circle1.setVisibility(View.GONE);
		}
		if (Config.isShowTab2()){
			circle2.setVisibility(View.VISIBLE);
		}else{
			circle2.setVisibility(View.GONE);
		}

		pager = findViewById(R.id.pager);
		BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
			switch (item.getItemId()) {
//				case R.id.navigation_home:
//					pager.setCurrentItem(0);
//					return true;
				case R.id.navigation_square:
					pager.setCurrentItem(0);
					return true;
				case R.id.navigation_friends:
					pager.setCurrentItem(2);
					return true;
				case R.id.navigation_msg:
					pager.setCurrentItem(1);
					return true;
			}
			return false;
		};
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		//默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
		BottomNavigationViewHelper.disableShiftMode(navigation);
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
//		adapter.addFragment(new MainFragment());
		adapter.addFragment(new SquareFragment());
		adapter.addFragment(new ChatFragment());
		adapter.addFragment(new FriendsFragment());
		ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}
			@Override
			public void onPageSelected(int position) {
				if (menuItem != null) {
					menuItem.setChecked(false);
				} else {
					navigation.getMenu().getItem(0).setChecked(false);
				}
				menuItem = navigation.getMenu().getItem(position);
				menuItem.setChecked(true);
			}
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		};
		pager.addOnPageChangeListener(pageChangeListener);
		pager.setOffscreenPageLimit(5);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Jzvd.releaseAllVideos();
	}

	private long lastTime;
	@Override
	public void onBackPressed() {
		if (Jzvd.backPress()) {
			return;
		}
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if ((System.currentTimeMillis() - lastTime) > 2000) {
				lastTime = System.currentTimeMillis();
				ShowUtil.showToast(this,"再按一次退出程序");
			} else {
				finish();
			}
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		switch (id){
			case R.id.nav_qrcode :presenter.showQRCode(QrCodeUtil.QRInfo.QR_TYPE_TEL,Datas.getUserInfo().getTel(),"jokor"+Datas.getUserInfo().getUserName()+".jpg",MainActivity.this);break;
			case R.id.nav_add: startActivity(new Intent(this,ConfirmActivity.class)); break;
			case R.id.nav_plan :   					break;
			case R.id.nav_share:presenter.appShare(MainActivity.this);break;
			case R.id.nav_finish:presenter.logout(MainActivity.this);break;
			case R.id.nav_setting:startActivity( new Intent(this, SettingActivity.class));break;
		}
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void confirm_friend(){
		Intent intent = new Intent(this,ConfirmActivity.class);
		startActivity(intent);
	}

	private void add_Group(){
		Intent intent = new Intent(this,SearchActivity.class);
		startActivity(intent);
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
			ShowUtil.sendSimpleNotification(getApplicationContext(),"新朋友",event.getUsername()+" 请求添加好友");
			MainActivity.this.runOnUiThread(() -> {
				Config.setTab2(true);
				if (circle2!=null){
					circle2.setVisibility(View.VISIBLE);
				}
			});

		}else {
			Log.e(TAG, "update: 添加好友好友已经存在" );
		}
	}

	public void disableRedCircle2(){
		circle2.setVisibility(View.GONE);
	}
}
