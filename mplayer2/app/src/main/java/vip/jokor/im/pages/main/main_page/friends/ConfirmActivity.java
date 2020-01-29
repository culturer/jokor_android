package vip.jokor.im.pages.main.main_page.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import vip.jokor.im.R;
import vip.jokor.im.base.BaseActivity;
import vip.jokor.im.model.bean.ApplyBean;
import vip.jokor.im.model.bean.UserBean;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.pages.util.userinfo.UserInfoActivity;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.util.base.ThreadUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmActivity extends BaseActivity {

	private static final String TAG = "ConfirmActivity";

	MainPresenter presenter;
	private RecyclerView mListView;
	private ItemAdapter mAdapter;
	private View myView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myView = getLayoutInflater().inflate(R.layout.activity_confirm,null);
		setContentView(myView);
		initToolBar();
		initData();
		initList();
	}

	private void initData(){
		HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(String t) {
				Log.i(TAG, "获取好友申请列表: "+t);
				ApplyBean applyBean;
				Gson gson = GsonUtil.getGson();
				applyBean = gson.fromJson(t,ApplyBean.class);
				mAdapter = new ItemAdapter(ConfirmActivity.this,applyBean.getApplies());
				mListView.setAdapter(mAdapter);
			}
		};
		if (presenter == null ) presenter = MainPresenter.getInstance();
		ThreadUtil.startThreadInPool(() -> presenter.getApplies(callback));
	}

	private void initToolBar(){
		LinearLayout container = findViewById(R.id.container);
		container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
	}

	private void initList(){
		mListView = findViewById(R.id.list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		mListView.setLayoutManager(mLayoutManager);
		mListView.setItemAnimator(new SampleItemAnimator());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home: finish();break;
            case R.id.add_friend : add_friend() ;break;
        }
		return super.onOptionsItemSelected(item);
	}

	private void add_friend(){
		View contentView = getLayoutInflater().inflate(R.layout.pop_query_friend,null);
		EditText et_tel = contentView.findViewById(R.id.tel);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加好友").
                setView(contentView)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    String tel = et_tel.getText().toString();
                    if (tel.length()<11){
                        Snackbar.make(myView,"请输入正确的手机号哦！", Snackbar.LENGTH_SHORT).show();
                    }else {
						presenter.getUserInfo(tel, new HttpCallback() {
							@Override
							public void onSuccess(String t) {
								Log.e(TAG, "add_friend onSuccess: "+t );
                                try {
                                    JSONObject jb = new JSONObject(t);
                                    if (jb.getInt("status") == 200){
                                        UserBean userBean = GsonUtil.getGson().fromJson(jb.getString("user"),UserBean.class);
                                        Intent intent = new Intent(ConfirmActivity.this,UserInfoActivity.class);
                                        intent.putExtra("userInfo",GsonUtil.getGson().toJson(userBean));
                                        startActivity(intent);
                                    }else {
										Snackbar.make(myView,"无此用户！", Snackbar.LENGTH_SHORT).show();
									}
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

							}

							@Override
							public void onFailure(VolleyError error) {
								Log.e(TAG, "add_friend onFailure: "+error.getMessage() );
								Snackbar.make(myView,"网络错误！", Snackbar.LENGTH_SHORT).show();
							}
						});

                    }
				});
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
