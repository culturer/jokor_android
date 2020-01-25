package com.jokor.base.pages.util.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jokor.base.R;
import com.jokor.base.pages.main.main_page.square.circle.CircleActivity;
import com.jokor.base.pages.util.article.ArticleActivity;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.wedgit.util.ShowUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoActivity extends AppCompatActivity {

    RecyclerView rvPage2;
    private List<String> urlList;
    private ListVideoAdapter videoAdapter;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initToolbar();
        initVideo();
        addListener();
    }

    private void initToolbar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,false);
//        StatusBarUtil.setStatusBarColor(this,R.color.light_black);
        View container = findViewById(R.id.container);
        container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
//        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("美食专栏");
        RequestOptions options = new RequestOptions()
                .override(SizeUtil.dip2px(this,24),SizeUtil.dip2px(this,24))//指定图片的尺寸
//                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(this)
                .load(R.drawable.put)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        toolbar.setOverflowIcon(resource);
                    }
                });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        StatusBarUtil.setToolbarCustomTheme(VideoActivity.this,getSupportActionBar(),R.color.white);

    }

    private void initVideo(){

        rvPage2 = findViewById(R.id.rv_page2);

        urlList = new ArrayList<>();
        urlList.add("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4");
        urlList.add("http://test1.jokor.vip/2020-01-12-175050101.mp4");
        urlList.add("http://test1.jokor.vip/a95da49cbaf45e2a556bfe3a67818c2d.mp4");
        urlList.add("http://test1.jokor.vip/d274a9dbc21881da8316f2f315ee7beb.mp4");

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPage2);

        videoAdapter = new ListVideoAdapter(urlList);
        layoutManager = new LinearLayoutManager(VideoActivity.this, RecyclerView.VERTICAL, false);
        rvPage2.setLayoutManager(layoutManager);
        rvPage2.setAdapter(videoAdapter);

    }

    private void addListener() {

        rvPage2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(layoutManager);

                        //当前固定后的item position
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (currentPosition != position) {
                            //如果当前position 和 上一次固定后的position 相同, 说明是同一个, 只不过滑动了一点点, 然后又释放了
                            MyVideoPlayer.releaseAllVideos();
                            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                            if (viewHolder != null && viewHolder instanceof ListVideoAdapter.VideoViewHolder) {
                                ((ListVideoAdapter.VideoViewHolder) viewHolder).mp_video.startVideo();
                            }
                        }
                        currentPosition = position;

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MyVideoPlayer.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyVideoPlayer.goOnPlayOnResume();
    }

    @Override
    protected void onDestroy() {
        MyVideoPlayer.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();break;
            case R.id.info:
                ShowUtil.showToast(VideoActivity.this,"打开个人名片页面");
//                startUserInfo();
                break;
            case R.id.del:
                ShowUtil.showToast(VideoActivity.this,"删除");
//                delArticle();
                break;
            case R.id.feedback:
                ShowUtil.showToast(VideoActivity.this,"举报");
//                feedbackArticle();
        }
        return true;
    }


}
