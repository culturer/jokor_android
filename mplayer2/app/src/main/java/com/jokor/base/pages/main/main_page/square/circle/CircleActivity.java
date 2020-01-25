package com.jokor.base.pages.main.main_page.square.circle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jokor.base.R;
import com.jokor.base.model.bean.FriendsArticlesBean;
import com.jokor.base.pages.main.main_page.square.circle.circle_data.CircleData;
import com.jokor.base.pages.main.main_page.square.circle.circle_pages.CityFragment;
import com.jokor.base.pages.main.main_page.square.circle.circle_pages.ClassicFragment;
import com.jokor.base.pages.main.main_page.square.circle.circle_pages.NewsFragment;
import com.jokor.base.pages.main.main_page.square.circle.circle_pages.ShopFragment;
import com.jokor.base.pages.main.main_page.square.circle.circle_pages.VideoFragment;
import com.jokor.base.pages.util.article.CreateArticleActivity;
import com.jokor.base.presenter.CirclePresenter;
import com.jokor.base.util.base.DrawUtil;
import com.jokor.base.util.base.FileUtil;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.ShareUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.util.base.TimeUtil;
import com.jokor.base.util.glide.GlideRoundTransform;
import com.jokor.base.wedgit.PagerAdapter;
import com.jokor.base.wedgit.util.QrCodeUtil;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bertsir.zbar.utils.QRUtils;

import static com.jokor.base.wedgit.util.QrCodeUtil.QRInfo.QR_TYPE_CIRCLE;
import static java.util.Calendar.DAY_OF_YEAR;

public class CircleActivity extends AppCompatActivity {

    String TAG = "CircleActivity";

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    private CircleData circleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        initData();
        initToolBar();
        initHeaderView();
        initFloatBtn();
        initPager();
    }

    private void initData(){

        Intent intent = getIntent();
        String strData = "";
        if (intent!=null){
            strData = intent.getStringExtra("data");
            circleData = GsonUtil.getGson().fromJson(strData,CircleData.class);
        }

        fragments.add(NewsFragment.newInstance(strData,""));
        fragments.add(VideoFragment.newInstance("",""));
        fragments.add(ClassicFragment.newInstance(strData,""));
        fragments.add(CityFragment.newInstance("",""));
        fragments.add(ShopFragment.newInstance("",""));
        titles.add("最新");
        titles.add("视频");
        titles.add("精华");
        titles.add("同城");
        titles.add("商城");
    }

    private void initHeaderView(){
        ImageView icon = findViewById(R.id.icon);
        TextView name = findViewById(R.id.name);
        TextView grad = findViewById(R.id.grad);
        TextView msg1 = findViewById(R.id.msg1);

        RequestOptions options = new RequestOptions()
                .error(R.mipmap.img_error)//加载错误之后的错误图
                .transform(new GlideRoundTransform(this,4))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
        Glide.with(CircleActivity.this)
                .load(circleData.getCircle().getIcon())
                .apply(options)
                .into(icon);
        name.setText(circleData.getCircle().getName());
        msg1.setText(circleData.getCircle().getMsg());
        grad.setText("LV"+circleData.getCircleUser().getCircleGrad()+"初出茅庐");
    }

    private void initToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        AppBarLayout app_bar = findViewById(R.id.app_bar);
        app_bar.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
        StatusBarUtil.setAndroidNativeLightStatusBar(this,false);
        TextView title = findViewById(R.id.title);
        title.setText(circleData.getCircle().getName());
        title.setVisibility(View.GONE);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                //实现返回键渐变色
                int max = appBarLayout.getTotalScrollRange();
                int item = Math.abs(i);
                if (item<max/2){
                    title.setVisibility(View.GONE);
                    title.setTextColor(getColor(R.color.white));
                    StatusBarUtil.setAndroidNativeLightStatusBar(CircleActivity.this,false);
                    StatusBarUtil.setToolbarCustomTheme(CircleActivity.this,getSupportActionBar(),R.color.white);
                    StatusBarUtil.setToolbarCustomThemeAlpha(CircleActivity.this,getSupportActionBar(),255-(2*item*255/max));
                }else {
                    title.setVisibility(View.VISIBLE);
                    title.setTextColor(getColor(R.color.black));
                    StatusBarUtil.setAndroidNativeLightStatusBar(CircleActivity.this,true);
                    StatusBarUtil.setToolbarCustomTheme(CircleActivity.this,getSupportActionBar(),R.color.black);
                    StatusBarUtil.setToolbarCustomThemeAlpha(CircleActivity.this,getSupportActionBar(),255-2*255*(max-item)/max);
                }
            }
        });

        ImageView qrcode = findViewById(R.id.qrcode);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestOptions options = new RequestOptions()
                        .error(R.mipmap.img_error)//加载错误之后的错误图
                        .transform(new GlideRoundTransform(CircleActivity.this,4))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
                Glide.with(CircleActivity.this)
                        .load(circleData.getCircle().getIcon())
                        .apply(options)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                Bitmap bitmap = DrawUtil.drawable2Bitmap(resource);
                                //生成二维码
                                QrCodeUtil.QRInfo info = new QrCodeUtil.QRInfo(QR_TYPE_CIRCLE,""+circleData.getCircle().getId());
                                Bitmap qrCode = QRUtils.getInstance().createQRCodeAddLogo(GsonUtil.getGson().toJson(info),1024,1024,bitmap);
                                View contentView = LayoutInflater.from(CircleActivity.this).inflate(R.layout.pop_show_qrcode,null);
                                ImageView img = contentView.findViewById(R.id.img);
                                img.setImageBitmap(qrCode);
                                AlertDialog.Builder builder = new AlertDialog.Builder(CircleActivity.this);
                                final AlertDialog dialog = builder
                                        .setTitle("扫码分享圈子")
                                        .setView(contentView)
                                        .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String filePath = FileUtil.saveBitmap(qrCode,circleData.getCircle().getName(),CircleActivity.this);
                                                Log.i(TAG, "filePath: "+filePath);
                                                ShareUtil.shareImage(CircleActivity.this,filePath);
                                            }
                                        })
                                        .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                FileUtil.saveBitmap(qrCode,circleData.getCircle().getName(),CircleActivity.this);
                                                ShowUtil.showToast(CircleActivity.this,"二维码已经保存到相册了哦！");
                                            }
                                        })
                                        .create();
                                dialog.show(); }});
            }
        });
    }

    private void initFloatBtn(){
        FloatingActionButton fab = findViewById(R.id.fab);

        if (circleData.getCircleUser() == null){
            //关注
            fab.setImageResource(R.drawable.focus);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowUtil.showToast(getApplicationContext(),"关注");
                }
            });
        }else {
            Date now = new Date();
            Calendar dateC = Calendar.getInstance();
            Log.e(TAG, "initFloatBtn: circleData "+GsonUtil.getGson().toJson(circleData) );
            Calendar nowC = Calendar.getInstance();
            nowC.setTime(now);
            if (circleData!=null && circleData.getCircleUser()!=null)
            Log.e(TAG, "initFloatBtn: nowC.get(DAY_OF_YEAR) "+nowC.get(DAY_OF_YEAR) );
            Log.e(TAG, "initFloatBtn: dateC.get(DAY_OF_YEAR) "+dateC.get(DAY_OF_YEAR)  );
            Log.e(TAG, "initFloatBtn: dateC.get(Calendar.YEAR) "+dateC.get(Calendar.YEAR) );
            Log.e(TAG, "initFloatBtn: nowC.get(Calendar.YEAR) "+nowC.get(Calendar.YEAR) );
            if (dateC!=null && dateC.get(DAY_OF_YEAR) == nowC.get(DAY_OF_YEAR) && dateC.get(Calendar.YEAR)  == nowC.get(Calendar.YEAR)){
                Log.e(TAG, "initFloatBtn:  今天签到过哦！" );
                //发表动态
                fab.setImageResource(R.drawable.publish);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowUtil.showToast(getApplicationContext(),"发表动态");
                        Intent intent = new Intent(CircleActivity.this, CreateArticleActivity.class);
                        intent.putExtra("circle_name",circleData.getCircle().getName());
                        intent.putExtra("circle_id",circleData.getCircle().getId());
                        startActivityForResult(intent,0x02);
                    }
                });
            }else {
//                签到
                fab.setImageResource(R.drawable.sign);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpCallback callback = new HttpCallback() {

                            @Override
                            public void onSuccess(String t) {
                                Log.e(TAG, "sign onSuccess: " + t );
                                try {
                                    JSONObject jb = new JSONObject(t);
                                    int status = jb.getInt("status");
                                    if (status == 200){
                                        circleData.getCircleUser().setSignTime(TimeUtil.getCurrentTime());
                                        fab.setImageResource(R.drawable.publish);
                                        fab.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ShowUtil.showToast(getApplicationContext(),"发布动态");
                                            }
                                        });
                                        CircleEvent circleEvent = new CircleEvent(TAG,CircleEvent.OPTION_UPDATE,circleData);
                                        EventBus.getDefault().postSticky(circleEvent);
                                        ShowUtil.showToast(getApplicationContext(),"签到成功");
                                    }else {
                                        ShowUtil.showToast(getApplicationContext(),jb.getString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int errorNo, String strMsg) {
                                Log.e(TAG, "sign onFailure: " +errorNo +strMsg);
                            }
                        };
                        CirclePresenter.getInstance().sign(circleData.getCircle().getId(),callback);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle, menu);
        return true;
    }

    //设置监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                ShareUtil.shareText(this,"欢迎加入  "+circleData.getCircle().getName()+"  小圈子 ，复制这段文字打开 "+getResources().getString(R.string.app_name)+"APP 点击关注加入我们！独家暗号：天王盖地虎，宝塔镇河妖 ["+circleData.getCircle().getId()+"] ","分享"+circleData.getCircle().getName()+"到");
                break;
        }
        return true;
    }

    private void initPager(){
        ViewPager pager = findViewById(R.id.pager);
        TabLayout tab = findViewById(R.id.tab);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),fragments,titles);
        pager.setAdapter(pagerAdapter);
        tab.removeAllTabs();
        for (int i=0;i<titles.size();i++){
            tab.addTab(tab.newTab().setText(titles.get(i)));
        }
        tab.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);

        View view = getLayoutInflater().inflate(R.layout.tab_item,null);
        TextView tab_item_textview = view.findViewById(R.id.tab_item_textview);
        tab_item_textview.setTextColor(getColor(R.color.red));
        tab_item_textview.setTextSize(14);
        tab_item_textview.setGravity(Gravity.CENTER);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_item_textview.setText(tab.getText());
                pager.setCurrentItem(tab.getPosition());
                tab.setCustomView(tab_item_textview);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_item_textview.setText(titles.get(0));
        tab.getTabAt(0).setCustomView(tab_item_textview);
        tab.getTabAt(0).select();
    }


}
