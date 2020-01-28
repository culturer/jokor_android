package vip.jokor.im.pages.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.MainActivity;
import vip.jokor.im.presenter.LoginPresenter;
import vip.jokor.im.wedgit.util.ShowUtil;
import vip.jokor.im.wedgit.loading_button.Sprite;
import vip.jokor.im.wedgit.loading_button.ThreeBounce;
import com.scwang.wave.MultiWaveHeader;


public class LoginActivity extends AppCompatActivity {

	private static final String TAG = "LoginActivity";

	View contentView;

	ImageView mLogo;
	EditText mEtMobile;
	ImageView mIvCleanPhone;
	EditText mEtPassword;
	ImageView mCleanPassword;
	ImageView mIvShowPwd;
	Button mBtnLogin;
	TextView mRegist;
	TextView mForgetPassword;
	LinearLayout mContent;
	NestedScrollView mScrollView;
	MultiWaveHeader waveHeader;

	ConstraintLayout mRoot;
	Sprite drawable;

	LoginPresenter presenter;

	private int keyHeight = 0; //软件盘弹起后所占高度
	private float scale = 0.6f; //logo缩放比例

	String phoneId;

	@SuppressLint("InflateParams")
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		contentView = LayoutInflater.from(this).inflate(R.layout.activity_login, null);
		setContentView(contentView);
		//屏幕高度
		int screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
		keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3

		initData();
		initView();
		initEvent();
		changeTopBackground();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (waveHeader != null && !waveHeader.isRunning()) waveHeader.start();
	}

	@Override
	public void onStop() {
		if (drawable != null && drawable.isRunning()) drawable.stop();
		if (waveHeader != null && waveHeader.isRunning()) waveHeader.stop();
		super.onStop();
	}

	private void initData() {

	}

	private void initView(){
		initBaseView();
	}

	private void initBaseView(){

		mLogo = contentView.findViewById(R.id.logo);
		mEtMobile = contentView.findViewById(R.id.et_mobile);
		mIvCleanPhone = contentView.findViewById(R.id.iv_clean_phone);
		mEtPassword = contentView.findViewById(R.id.et_password);
		mCleanPassword = contentView.findViewById(R.id.clean_password);
		mIvShowPwd = contentView.findViewById(R.id.iv_show_pwd);
		mBtnLogin = contentView.findViewById(R.id.btn_login);
		mRegist = contentView.findViewById(R.id.regist);
		mForgetPassword = contentView.findViewById(R.id.forget_password);
		mContent = contentView.findViewById(R.id.content);
		mScrollView = contentView.findViewById(R.id.scrollView);
		mRoot = contentView.findViewById(R.id.root);
		mEtMobile.setText(phoneId);
		mIvCleanPhone.setOnClickListener(v -> mEtMobile.setText(""));
		mCleanPassword.setOnClickListener(v -> mEtPassword.setText(""));
		mIvShowPwd.setOnClickListener(v -> {
			if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
				mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mIvShowPwd.setImageResource(R.drawable.ic_visibility_black_24dp);
			} else {
				mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mIvShowPwd.setImageResource(R.drawable.ic_visibility_off_black_24dp);
			}
			String pwd = mEtPassword.getText().toString();
			if (!TextUtils.isEmpty(pwd))
				mEtPassword.setSelection(pwd.length());
		});
		mRegist.setOnClickListener(v -> {
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			intent.putExtra("login",true);
			startActivity(intent);
		});
		mForgetPassword.setOnClickListener(v -> {
//			Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
//			startActivity(intent);
		});


		waveHeader = findViewById(R.id.waveHeader);

		waveHeader.setVelocity(3f);
		waveHeader.setColorAlpha(0.3f);
		waveHeader.setStartColor(R.color.black);
		waveHeader.setCloseColor(R.color.grey_dark);
		waveHeader.setWaveHeight(20);
		waveHeader.setProgress(0.5f);
		waveHeader.setWaves("0,0,1,1,25\n90,0,1,1,25");
		waveHeader.start();

	}

	@SuppressLint("ClickableViewAccessibility")
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
	private void initEvent() {
		mEtMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s) && mIvCleanPhone.getVisibility() == View.GONE) {
					mIvCleanPhone.setVisibility(View.VISIBLE);
				} else if (TextUtils.isEmpty(s)) {
					mIvCleanPhone.setVisibility(View.GONE);
				}
			}
		});
		mEtPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s) && mCleanPassword.getVisibility() == View.GONE) {
					mCleanPassword.setVisibility(View.VISIBLE);
				} else if (TextUtils.isEmpty(s)) {
					mCleanPassword.setVisibility(View.GONE);
				}
				if (s.toString().isEmpty())
					return;
				if (!s.toString().matches("[A-Za-z0-9]+")) {
					String temp = s.toString();
					ShowUtil.showToast(LoginActivity.this, "请输入数字或字母");
					s.delete(temp.length() - 1, temp.length());
					mEtPassword.setSelection(s.length());
				}
			}
		});
		/*
		 * 禁止键盘弹起的时候可以滚动
		 */
		mScrollView.setOnTouchListener((v, event) -> true);
		mScrollView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
          /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
          现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
			if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
				int dist = mContent.getBottom() - bottom;
				if (dist > 0) {
					ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
					mAnimatorTranslateY.setDuration(300);
					mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
					mAnimatorTranslateY.start();
					zoomIn(mLogo, scale, dist);
				}

			} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
				Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
				if ((mContent.getBottom() - oldBottom) > 0) {
					ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
					mAnimatorTranslateY.setDuration(300);
					mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
					mAnimatorTranslateY.start();
					//键盘收回后，logo恢复原来大小，位置同样回到初始位置
					zoomOut(mLogo, scale);
				}
			}
		});

		mBtnLogin.setOnClickListener(v -> login(mEtMobile.getText().toString(),mEtPassword.getText().toString()));
	}

	private void login(String tel,String pwd){
		if ((!tel.equals("")) && (!pwd.equals(""))){
			mBtnLogin.setText("登录中");
			if (drawable == null){
				drawable = new ThreeBounce();
				drawable.setBounds(0, 0, 100, 100);
				//noinspection deprecation
				drawable.setColor(getResources().getColor(R.color.white));
			}
			mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mIvShowPwd.setImageResource(R.drawable.ic_visibility_off_black_24dp);
			mBtnLogin.setCompoundDrawables(drawable, null,drawable , null);
			drawable.start();
			mBtnLogin.setEnabled(false);
			mEtMobile.setEnabled(false);
			mIvCleanPhone.setEnabled(false);
			mEtPassword.setEnabled(false);
			mCleanPassword.setEnabled(false);
			mIvShowPwd.setEnabled(false);
			mRegist.setEnabled(false);
			mForgetPassword.setEnabled(false);
			if (presenter == null ){presenter = LoginPresenter.getInstance();}
			presenter.login(tel, pwd, (isSuccess, msg) -> {
				boolean result = isSuccess ? loginSuccess() : loginFail(msg);
				Log.i(TAG, "login: "+result);
			});
		}else {
			ShowUtil.showSnack(contentView,"账号或密码为空！");
		}
	}

	public boolean loginSuccess() {
		Log.i(TAG, "loginSuccess: ");
		ShowUtil.showToast(getApplicationContext(),getResources().getString(R.string.welcome_msg));
		//跳转到主页
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		finish();
		return true;
	}

	public boolean loginFail(String msg) {
		mBtnLogin.setText("登录");
		mBtnLogin.setCompoundDrawables(null, null, null, null);
		mBtnLogin.setEnabled(true);
		mEtMobile.setEnabled(true);
		mIvCleanPhone.setEnabled(true);
		mEtPassword.setEnabled(true);
		mCleanPassword.setEnabled(true);
		mIvShowPwd.setEnabled(true);
		mRegist.setEnabled(true);
		mForgetPassword.setEnabled(true);
		Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).show();
		return false;
	}

	//修改顶部状态栏颜色
	@RequiresApi(api = Build.VERSION_CODES.M)
	private void changeTopBackground(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//获取窗口区域
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			window.setStatusBarColor(getColor(R.color.white));
		}
	}

	public void zoomIn(View view, float scale, float dist) {
		view.setPivotY((float)view.getHeight());
		view.setPivotX((float)(view.getWidth() / 2));
		AnimatorSet mAnimatorSet = new AnimatorSet();
		ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0F, scale);
		ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0F, scale);
		ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0F, -dist);
		mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
		mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
		mAnimatorSet.setDuration(300L);
		mAnimatorSet.start();
	}

	public void zoomOut(View view, float scale) {
		view.setPivotY((float)view.getHeight());
		view.setPivotX((float)(view.getWidth() / 2));
		AnimatorSet mAnimatorSet = new AnimatorSet();
		ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0F);
		ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0F);
		ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0.0F);
		mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
		mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
		mAnimatorSet.setDuration(300L);
		mAnimatorSet.start();
	}

	private long lastTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - lastTime) > 2000) {
				lastTime = System.currentTimeMillis();
				ShowUtil.showToast(this,"再按一次退出程序");
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}