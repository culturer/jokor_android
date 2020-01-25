package com.jokor.base.pages.main.main_page.group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jokor.base.R;
import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.GroupBean;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.util.base.TimeUtil;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupInfoActivity extends AppCompatActivity {
	
	private static final String TAG="GroupInfoActivity";
	
	public static final String GROUP_INFO = "group_info";

	private GroupBean groupBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		initToolBar();
	    initData();
	    initView();
	}
	
	private void initToolBar(){
		LinearLayout container = findViewById(R.id.container);
		container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
		setSupportActionBar(toolbar);
	}

	private void initData(){
	    String strGroup = getIntent().getStringExtra("strGroup");
	    if (strGroup.equals("")){
            long groupId = getIntent().getLongExtra("groupId",0);
            //本地搜
            for (GroupBean item : Datas.getGroups()){
                if (item.getId() == groupId){
                    groupBean = item;
                    break;
                }
            }
            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Log.i(TAG, "search_group onSuccess: "+t);
                    try {
                        JSONObject jb = new JSONObject(t);
                        int status = jb.getInt("status");
                        if (status == 200){
                            String str =  jb.getString("group");
                            groupBean = GsonUtil.getGson().fromJson(str,GroupBean.class);
                            //这里需要补上一些页面刷新逻辑
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
            GroupPresenter.getInstance().search_group(""+groupId,callback);
        }else{
	        groupBean = GsonUtil.getGson().fromJson(strGroup,GroupBean.class);
        }
    }

	ImageView icon;
	TextView name;
	TextView label;
	TextView time;
	TextView msg;
	TextView count;
	TextView manager;
	TextView members;
	TextView group_desc;
    private void initView(){

		icon = findViewById(R.id.icon);
		name = findViewById(R.id.name);
		label = findViewById(R.id.label);
		time = findViewById(R.id.time);
		msg = findViewById(R.id.msg);
		count = findViewById(R.id.count);
		manager = findViewById(R.id.manager);
		members = findViewById(R.id.members);
		group_desc = findViewById(R.id.group_desc);
		setView();
	}

	public void setView(){
    	if (groupBean!=null){
			RequestOptions options = new RequestOptions()
					.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
					.circleCrop()
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
			Glide.with(this)
					.load(groupBean.getIcon())
					.apply(options)
					.into(icon);
			name.setText(groupBean.getName());
			label.setText(""+groupBean.getGrad()+"级");
			time.setText(TimeUtil.getDateToString(TimeUtil.String2Date(groupBean.getCreateTime()).getTime()));
			msg.setText(groupBean.getSign());
			manager.setText("管理员："+10+"人");
			members.setText("群成员："+groupBean.getMemCount()+"人");
			group_desc.setText(groupBean.getMsg());
    	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.group_menu, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:finish();break;
            case R.id.add: add_group();break;
            case R.id.feedback:feedback_group();break;

        }
        return true;
    }

    private void add_group(){
        for (GroupBean item : Datas.getGroups()){
            if (item.getId() == groupBean.getId()){
                ShowUtil.showToast(this,"已经加入了 [ "+groupBean.getName()+" ] ");
                return;
            }
        }
    	HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(String t) {
				Log.i(TAG, "add_group onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        String strGroup = jb.getString("group");
                        GroupBean groupBean = GsonUtil.getGson().fromJson(strGroup,GroupBean.class);
                        GroupEvent event = new GroupEvent(TAG,groupBean);
                        EventBus.getDefault().postSticky(event);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

			@Override
			public void onFailure(int errorNo, String strMsg) {
				Log.i(TAG, "add_group onFailure: "+errorNo+" , "+strMsg);
			}
		};
        GroupPresenter.getInstance().add_group(""+groupBean.getId(),callback);
    }

    private void feedback_group(){
    ShowUtil.showToast(this,"feedback");
    }

}
