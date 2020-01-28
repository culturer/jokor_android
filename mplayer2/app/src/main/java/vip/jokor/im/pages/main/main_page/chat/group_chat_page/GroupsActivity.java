package vip.jokor.im.pages.main.main_page.chat.group_chat_page;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.chat.group_chat_page.chat_page.GroupChatFragment;

import vip.jokor.im.pages.main.main_page.chat.group_chat_page.shop_page.ShopFragment;

import vip.jokor.im.model.stores_bean.MSession;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.bottom_navigation.BottomNavigationViewHelper;
import vip.jokor.im.wedgit.bottom_navigation.MyPagerAdapter;
import vip.jokor.im.wedgit.bottom_navigation.MyViewPager;


public class GroupsActivity extends AppCompatActivity {
	
	private static final String TAG = "GroupsActivity";
	
	private MyViewPager pager;
	BottomNavigationView navigation;
	private MyPagerAdapter adapter;
	private MenuItem menuItem;
	
	private MSession session = null ;
	GroupChatFragment groupChatFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		//解决软键盘冲突问题
//		int statusBarHeight = EncryptUtil.SizeUtil.getStatusBarHeight(this);
//		SoftHideKeyBoardUtil.assistActivity(this,statusBarHeight);
		hideSoftKeyboard(GroupsActivity.this);
		initData();
		
	}
	//初始化会话
	private void initData(){
		String strSession = getIntent().getStringExtra("session");
		if (strSession!=null && !strSession.equals("")){
			session = GsonUtil.getGson().fromJson(strSession,MSession.class);
		}
		initToolBar();
		initPage();
	}
	
	private void initToolBar(){
		LinearLayout container = findViewById(R.id.container);
		TextView toolbar_title = findViewById(R.id.toolbar_title);
//		toolbar_title.setText(mmGroup.getName()+"("+mmGroup.getCount()+")");
		toolbar_title.setText("测试群聊");
//		container.setPadding(0, EncryptUtil.SizeUtil.getStatusBarHeight(this),0,0);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GroupsActivity.this.finish();
			}
		});
		toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
		setSupportActionBar(toolbar);
	}
	
	private void initPage(){
		navigation = findViewById(R.id.navigation);
		pager = findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

		mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.group_chat:
						pager.setCurrentItem(0);
						break;
					case R.id.group_shop:
						pager.setCurrentItem(1);
						break;
				}
				hideSoftKeyboard(GroupsActivity.this);
				return true;
			}
		};

		groupChatFragment =GroupChatFragment.newInstance(GsonUtil.getGson().toJson(session),"");
		adapter.addFragment(groupChatFragment);
		adapter.addFragment(ShopFragment.newInstance("",""));

		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		//默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
		BottomNavigationViewHelper.disableShiftMode(navigation);
		
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
				hideSoftKeyboard(GroupsActivity.this);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		};
		pager.addOnPageChangeListener(pageChangeListener);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
		navigation.setVisibility(View.VISIBLE);
		View divider = findViewById(R.id.divider);
		divider.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	
	//隐藏软键盘
	private   void hideSoftKeyboard(Activity activity) {
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		if (!groupChatFragment.onBackPressed()){
//			super.onBackPressed();
//		}
	}
}
