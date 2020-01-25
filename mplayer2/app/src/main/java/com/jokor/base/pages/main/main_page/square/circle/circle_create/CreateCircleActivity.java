package com.jokor.base.pages.main.main_page.square.circle.circle_create;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jokor.base.R;
import com.jokor.base.base.Datas;
import com.jokor.base.pages.main.main_page.square.circle.circle_data.Circle;
import com.jokor.base.pages.main.main_page.square.circle.CircleActivity;
import com.jokor.base.pages.main.main_page.square.circle.circle_data.CircleData;
import com.jokor.base.pages.main.main_page.square.circle.CircleEvent;
import com.jokor.base.pages.main.main_page.square.circle.circle_data.CircleUser;
import com.jokor.base.presenter.CirclePresenter;
import com.jokor.base.util.base.FileUtil;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.QiNiuUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.util.base.ThreadUtil;
import com.jokor.base.util.glide.GlideRoundTransform;
import com.jokor.base.util.glide.MediaLoader;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static com.jokor.base.base.Urls.QINIU_URL;

public class CreateCircleActivity extends AppCompatActivity {

    String TAG = "CreateCircleActivity" ;

    LinearLayout container;
    final int REQUEST_CODE_CHOOSE = 0x01;

    ImageView icon;
    TextView username;
    TextView uid;
    TextView tel;
    ImageView status;

    String strData;
    String fileUrl = "";
    long strBelong2Id = -1;
    int checkNameStatus = 0;
    final int CHECK_UNKNOW = 0;
    final int CHECK_OK = 1;
    final int CHECK_ERROR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);
        initData();
        initToolBar();
        initIcon();
    }

    private void initData(){
        Intent intent = getIntent();
        if (intent!=null){
            strData = intent.getStringExtra("data");
        }
    }

    private void initToolBar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
        container = findViewById(R.id.container);
        container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    private void initIcon(){
        View change_icon = findViewById(R.id.change_icon);
        View change_name = findViewById(R.id.change_name);
        View belong2 = findViewById(R.id.belong2);
        View change_desc = findViewById(R.id.change_desc);
        icon = findViewById(R.id.icon);
        username = findViewById(R.id.username);
        uid = findViewById(R.id.uid);
        tel = findViewById(R.id.tel);
        status = findViewById(R.id.status);

        if (strData!=null){
            username.setText(strData);
            checkName();
        }

        status.setImageResource(R.drawable.unknow);
        checkNameStatus = CHECK_UNKNOW;
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkNameStatus){
                    case CHECK_UNKNOW:
                        ShowUtil.showToast(getApplicationContext(),"未核名");
                        ShowUtil.showToast(getApplicationContext(),"正在进行核名！");
                        checkName();
                        break;
                    case CHECK_OK:
                        ShowUtil.showToast(getApplicationContext(),"名称可用");
                        break;
                    case CHECK_ERROR:
                        ShowUtil.showToast(getApplicationContext(),"该圈子已存在！");
                        break;
                }
            }
        });

        change_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(CreateCircleActivity.this)
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                        .onGranted(permissions -> {
                            Matisse.from(CreateCircleActivity.this)
                                    .choose(MimeType.ofImage())
                                    .countable(false)
                                    .maxSelectable(1)
                                    .autoHideToolbarOnSingleTap(true)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .capture(true)
                                    .captureStrategy(new CaptureStrategy(true,"com.jokor.base.fileProvider"))
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new MediaLoader())
                                    .autoHideToolbarOnSingleTap(true)
                                    .showSingleMediaType(true)
                                    .originalEnable(true)
                                    .forResult(REQUEST_CODE_CHOOSE);
                        })
                        .onDenied(permissions -> {
                            Toast.makeText(CreateCircleActivity.this,"请授权！",Toast.LENGTH_LONG).show();
                        })
                        .start();
            }
        });
        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateCircleActivity.this);
                View inputView = getLayoutInflater().inflate(R.layout.pop_input_data,null);
                EditText input = inputView.findViewById(R.id.input);
                input.setText(username.getText().toString());
                builder.setTitle("修改圈子名称")
                        .setView(inputView)
                        .setNegativeButton("取消",(dialog, which) -> dialog.dismiss())
                        .setPositiveButton("确定",(dialog, which) -> {
                            ShowUtil.showToast(CreateCircleActivity.this,"修改圈子名");
                            username.setText(input.getText());
                            checkName();
                        })
                        .create().show();

            }
        });
        belong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateCircleActivity.this);
                View inputView = getLayoutInflater().inflate(R.layout.pop_input_data,null);
                EditText input = inputView.findViewById(R.id.input);
                input.setInputType(TYPE_CLASS_PHONE);
                builder.setTitle("填写圈子副管理员手机号")
                        .setView(inputView)
                        .setNegativeButton("取消",(dialog, which) -> dialog.dismiss())
                        .setPositiveButton("确定",(dialog, which) -> {
                            boolean flag = false;
                            for (int i=0;i<Datas.getFriendBean().getFriends().size();i++){
                                Log.e(TAG, "onClick: "+new Gson().toJson(Datas.getFriendBean().getFriends().get(i).getFriend()));
                                if (Datas.getFriendBean().getFriends().get(i).getFriend().getTel().equals(input.getText().toString())){
                                    uid.setText(Datas.getFriendBean().getFriends().get(i).getFriend().getUserName());
                                    strBelong2Id = Datas.getFriendBean().getFriends().get(i).getFriend().getId();
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag){
                                ShowUtil.showToast(getApplicationContext(),"该用户还不是你的好友哦！");
                            }
                        })
                        .create().show();
            }
        });
        change_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateCircleActivity.this);
                View inputView = getLayoutInflater().inflate(R.layout.pop_input_data,null);
                EditText input = inputView.findViewById(R.id.input);
                builder.setTitle("填写圈子介绍")
                        .setView(inputView)
                        .setNegativeButton("取消",(dialog, which) -> dialog.dismiss())
                        .setPositiveButton("确定",(dialog, which) -> {
                            ShowUtil.showToast(CreateCircleActivity.this,"修改圈子介绍");
                            tel.setText(input.getText());
                        })
                        .create().show();
            }
        });
    }

    List<Uri> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Uri uri = mSelected.get(0);
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.img_error)//加载错误之后的错误图
                    .transform(new GlideRoundTransform(this,4))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
            Glide.with(CreateCircleActivity.this)
                    .load(uri)
                    .apply(options)
                    .into(icon);

            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Log.e(TAG, "onSuccess: "+t );
                    try {
                        JSONObject jb = new JSONObject(t);
                        int status = jb.getInt("status");
                        if (status == 200){
                            String strToken = jb.getString("token");
                            ContentResolver contentResolver = getContentResolver();
                            Cursor cursor = contentResolver.query(uri,null,null,null,null);
                            String suffix = "";
                            while (cursor.moveToNext()){
                                String filename = cursor.getString(cursor.getColumnIndex("_display_name"));
                                Log.i(TAG, "onActivityResult: _display_name --- "+filename);
                                suffix = filename.substring(filename.lastIndexOf(".") + 1);
//                                suffix是文件格式
                            }
                            String filename = "C_"+ Datas.getUserInfo().getId()+System.currentTimeMillis()+"."+suffix;
                            fileUrl = QINIU_URL+filename;
                            Log.e(TAG, "onSuccess: fileUrl "+fileUrl );
                            ThreadUtil.startThreadInPool(() -> {
                                try {
                                    AssetFileDescriptor fd = contentResolver.openAssetFileDescriptor(uri,"r");
                                    if (fd!=null){
                                        FileInputStream inputStream = fd.createInputStream();
                                        File file = new File(getApplicationContext().getCacheDir(),"cache");
                                        FileUtil.copy( inputStream ,file );
                                        Log.i(TAG, "onActivityResult: file length --- "+file.length());
                                        QiNiuUtil.getInstance().upload(strToken,file,filename,null,null);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }else {
                            ShowUtil.showToast(getApplicationContext(),"获取token失败!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    Log.e(TAG, "onFailure: "+errorNo+strMsg);
                    ShowUtil.showToast(getApplicationContext(),"网络错误！!");
                }
            };
            QiNiuUtil.getInstance().getToken(callback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle_create, menu);
        return true;
    }

    //设置监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_create:
                ShowUtil.showToast(getApplicationContext(),"创建圈子");
                createCircle();
                break;
        }
        return true;
    }

    private void createCircle(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.e(TAG, "createCircle onSuccess: "+t );
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        String strCircle = jb.getString("circle");
                        String strCUser = jb.getString("cuser");
                        CircleUser cuser = GsonUtil.getGson().fromJson(strCUser,CircleUser.class);
                        Circle circle = GsonUtil.getGson().fromJson(strCircle,Circle.class);
                        CircleData data = new CircleData(circle,cuser);
                        EventBus.getDefault().postSticky(new CircleEvent(TAG,CircleEvent.OPTION_ADD,data));
                        Intent intent = new Intent(CreateCircleActivity.this, CircleActivity.class);
                        intent.putExtra("data",GsonUtil.getGson().toJson(data));
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int errorNo, String strMsg) {
                Log.e(TAG, "createCircle onFailure: "+errorNo+strMsg );
            }
        };
        if (checkNameStatus!=CHECK_OK){
            ShowUtil.showToast(getApplicationContext(),"请核名！");
            return;
        }
        if (fileUrl.equals("")){
            ShowUtil.showToast(getApplicationContext(),"请选择头像");
            return;
        }
        if (username.getText().toString().equals("")){
            ShowUtil.showToast(getApplicationContext(),"请输入圈子名称");
            return;
        }
        if (tel.getText().toString().equals("")){
            ShowUtil.showToast(getApplicationContext(),"请输入圈子简介");
            return;
        }
        if (strBelong2Id == -1){
            ShowUtil.showToast(getApplicationContext(),"请填选副圈主");
            return;
        }
        CirclePresenter.getInstance().createCircle(fileUrl,username.getText().toString(),tel.getText().toString(),strBelong2Id,callback);
    }

    private void checkName(){
        checkNameStatus = CHECK_UNKNOW;
        status.setImageResource(R.drawable.unknow);
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.e(TAG, "checkName onSuccess: "+t );
                try {
                    JSONObject jb = new JSONObject(t);
                    if (jb.getInt("status") == 200){
                        checkNameStatus = CHECK_OK;
                        status.setImageResource(R.drawable.ok);
                    }else {
                        checkNameStatus = CHECK_ERROR;
                        status.setImageResource(R.drawable.error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                Log.e(TAG, "checkName onFailure: "+errorNo+strMsg );
                ShowUtil.showToast(getApplicationContext(),"网络连接异常，请检查网络后重试！");
                checkNameStatus = CHECK_UNKNOW;
                status.setImageResource(R.drawable.unknow);
            }
        };
        CirclePresenter.getInstance().checkName(username.getText().toString(),callback);
    }
}
