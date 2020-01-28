package vip.jokor.im.pages.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import vip.jokor.im.R;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.util.glide.MediaLoader;
import vip.jokor.im.wedgit.TimeCount;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

	private String TAG = "RegisterActivity";

	private AutoCompleteTextView tvUser;
	private EditText etPass;
	private EditText name;
	private EditText msg;
	private TextView getMsg;

	TimeCount timeCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initToolBar();
		initBanner();
		tvUser = findViewById(R.id.email);
		etPass = findViewById(R.id.password);
		msg = findViewById(R.id.msg);
		Button tvUserSignInButton = findViewById(R.id.email_sign_in_button);
		getMsg = findViewById(R.id.getMsg);
		getMsg.setOnClickListener(view -> getMsg());
		tvUserSignInButton.setOnClickListener(this::register);
	}

	private void initToolBar(){
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		LinearLayout container;
		container = findViewById(R.id.container);
		container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);

		setSupportActionBar(toolbar);
	}

	private void initBanner(){
		List<String>  images = new ArrayList<>();
		images.add("https://images.pexels.com/photos/1257110/pexels-photo-1257110.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
		images.add("https://images.pexels.com/photos/160994/family-outdoor-happy-happiness-160994.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
		images.add("https://images.pexels.com/photos/545016/pexels-photo-545016.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
		Banner banner = findViewById(R.id.banner);
		//设置banner样式
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		//设置图片加载器
		banner.setImageLoader(new MediaLoader());
		//设置图片集合
		banner.setImages(images);
		//设置banner动画效果
		banner.setBannerAnimation(Transformer.DepthPage);
		//设置标题集合（当banner样式有显示title时）
//		banner.setBannerTitles(titles);
		//设置自动轮播，默认为true
		banner.isAutoPlay(true);
		//设置轮播时间
		banner.setDelayTime(3*1000);
		//设置指示器位置（当banner模式中有指示器时）
		banner.setIndicatorGravity(BannerConfig.RIGHT);
		//banner设置方法全部调用完毕时最后调用
		banner.start();
	}

	private void register(View view){
		Log.i(TAG, "register: ");
		String user = tvUser.getText().toString();
		String password = etPass.getText().toString();
		String password2 = name.getText().toString();
		String message = msg.getText().toString();

		if (TextUtils.isEmpty(message)) {
			Snackbar.make(view, "请获取验证码", Snackbar.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(user)) {
			Snackbar.make(view, "手机号不能为空", Snackbar.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
			Snackbar.make(view, "密码不能为空", Snackbar.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.equals(password, password2)) {
			Snackbar.make(view, "两次密码不一致", Snackbar.LENGTH_SHORT).show();
			return;
		}
		Log.i(TAG, "register: 开始处理注册业务逻辑 ！");
	}

	//获取短信验证码
	private void getMsg(){
		if (!tvUser.getText().toString().equals("") ){
			Log.i(TAG, "getMsg: 获取到用户输入的验证码！");
			timeCount = new TimeCount(60000, 1000, getMsg);
			timeCount.start();
		}else {
			ShowUtil.showToast(RegisterActivity.this,"请输入手机号！");
		}
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}
}
