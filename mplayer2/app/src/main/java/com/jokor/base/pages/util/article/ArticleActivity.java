package com.jokor.base.pages.util.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.reflect.TypeToken;
import com.jokor.base.R;
import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.CommentsBean;
import com.jokor.base.model.bean.FriendsArticlesBean;
import com.jokor.base.model.bean.LikesBean;
import com.jokor.base.model.bean.ReplysBean;
import com.jokor.base.pages.main.main_page.chat.chat_page.EmojiAdapter;
import com.jokor.base.presenter.ArticlePresenter;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.util.glide.MediaLoader;
import com.jokor.base.wedgit.NestedListView;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.TRANSCRIPT_MODE_NORMAL;

public class ArticleActivity extends AppCompatActivity {

    private String TAG = "ArticleActivity" ;

    private FriendsArticlesBean.ArticlesBean data;
    private CommentAdapter commentAdapter;
    private EditText chat_edit;
    private NestedListView listview;

    private List<CommentsBean> newComments = new ArrayList<>();
    private LikesBean newLikes;
    private List<ReplysBean> newReplys = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initData();
        initToolBar();
        initBanner();
        setLike();
        initUser();
        initEmoji();
        initCommentList();
        initSendComment();
    }

    private void initData(){
        String strData = getIntent().getStringExtra("data");
        Log.e(TAG, "initData: "+strData );
        data = GsonUtil.getGson().fromJson(strData,FriendsArticlesBean.ArticlesBean.class);
    }

    private void initToolBar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
//        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(data.getArticle().getUsername());
        if (data !=null){
            Log.i(TAG, "initToolBar: icon "+data.getArticle().getIcon());
            RequestOptions options = new RequestOptions()
                    .override(SizeUtil.dip2px(this,40),SizeUtil.dip2px(this,40))//指定图片的尺寸
                    .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
            Glide.with(this)
                    .load(data.getArticle().getIcon())
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            toolbar.setOverflowIcon(resource);
                        }
                    });
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    private void initBanner(){
        Banner banner = findViewById(R.id.banner);
        List<String> images = GsonUtil.getGson().fromJson(data.getArticle().getImgs(),new TypeToken<List<String>>() {}.getType());
        if (images == null || images.size() == 0){
            banner.setVisibility(View.GONE);
        }else{
            //设置banner样式
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置图片加载器
            banner.setImageLoader(new MediaLoader());
            //设置图片集合
            banner.setImages(images);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(1500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.setOnBannerListener(position -> {

            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    private void setLike(){

        ImageView like = findViewById(R.id.like);
        like.setVisibility(View.VISIBLE);
        like.setImageResource(R.drawable.like);
        com.wx.goodview.GoodView mGoodView = new com.wx.goodview.GoodView(this);
        boolean flag = false;
        if (data.getLikes()!=null &&data.getLikes().size()>0){
            for (int i=0;i<data.getLikes().size();i++){
                if (data.getLikes().get(i).getBelongId() == Datas.getUserInfo().getId()){
                    like.setImageResource(R.drawable.like_checked);
                    like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            like.setImageResource(R.drawable.like_checked);
                            mGoodView.setText("+1");
                            mGoodView.show(like);
                        }
                    });
                    flag = true;
                }
            }
        }
        if (!flag){
            like.setOnClickListener(v -> {
                like.setImageResource(R.drawable.like_checked);
                mGoodView.setText("+1");
                mGoodView.show(like);
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                LikesBean likesBean = GsonUtil.getGson().fromJson(jb.getString("like"),LikesBean.class);
                                if (data.getLikes() == null)data.setLikes(new ArrayList<>());
                                data.getLikes().add(0,likesBean);
                                initUser();
                                like.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        like.setImageResource(R.drawable.like_checked);
                                        mGoodView.setText("+1");
                                        mGoodView.show(like);
                                    }
                                });
                                newLikes = likesBean;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                    }
                };
                ArticlePresenter.getInstance().addLike(data.getArticle().getId(),callback);
            });
        }
    }

    private void initUser(){
        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.logo)//加载错误之后的错误图
                .override(400,400)//指定图片的尺寸
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
        ImageView user1 = findViewById(R.id.user1);
        ImageView user2 = findViewById(R.id.user2);
        ImageView user3 = findViewById(R.id.user3);
        TextView like_count = findViewById(R.id.like_count);
        if (data.getLikes() == null || data.getLikes().size()==0){
            user1.setVisibility(View.GONE);
            user2.setVisibility(View.GONE);
            user3.setVisibility(View.GONE);
            like_count.setVisibility(View.GONE);
        }else {
            switch (data.getLikes().size()){
                case 1:
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.GONE);
                    user3.setVisibility(View.GONE);
                    like_count.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(data.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    String strLikeCount = data.getLikes().get(0).getUsername()+" 觉得很赞！";
                    like_count.setText(strLikeCount);
                    like_count.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.VISIBLE);
                    user3.setVisibility(View.GONE);
                    like_count.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(data.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    Glide.with(this)
                            .load(data.getLikes().get(1).getIcon())
                            .apply(options1)
                            .into(user2);
                    String strLikeCount2 = data.getLikes().get(0).getUsername()+","+data.getLikes().get(1).getUsername()+" 觉得很赞！";
                    like_count.setText(strLikeCount2);
                    like_count.setVisibility(View.VISIBLE);
                    break;
                default:
                    String strLikeCount3;
                    if (data.getLikes().size() == 3){
                        strLikeCount3 = data.getLikes().get(0).getUsername() + " , "+data.getLikes().get(1).getUsername()+" , "+data.getLikes().get(2).getUsername()+" 觉得很赞！ ";
                    }else{
                        strLikeCount3 = "等 "+data.getLikes().size()+"人 觉得很赞！ ";
                    }
                    like_count.setText(strLikeCount3);
                    like_count.setVisibility(View.VISIBLE);
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.VISIBLE);
                    user3.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(data.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    Glide.with(this)
                            .load(data.getLikes().get(1).getIcon())
                            .apply(options1)
                            .into(user2);
                    Glide.with(this)
                            .load(data.getLikes().get(2).getIcon())
                            .apply(options1)
                            .into(user3);
                    break;
            }
        }
    }

    private void initEmoji(){
        Handler handler = new Handler();
        ImageButton chat_emoji = findViewById(R.id.chat_emoji);
        GridView emoji_grid = findViewById(R.id.emoji_grid);
        chat_edit = findViewById(R.id.chat_edit);
        hideSoftKeyboard(this);
        chat_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emoji_grid.getVisibility() == View.VISIBLE) {
                    emoji_grid.setVisibility(View.GONE);
                }
            }
        });
        chat_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emoji_grid.getVisibility() == View.VISIBLE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emoji_grid.setVisibility(View.GONE);
                        }
                    }, 50);

                } else {
                    hideSoftKeyboard(ArticleActivity.this);
                    handler.postDelayed(() -> emoji_grid.setVisibility(View.VISIBLE), 50);
                }
            }
        });
        List<Integer> emojiList = new ArrayList<>();
        int[] emojiIds = new int[66];
        for (int i = 0; i < 66; i++) {
            if (i < 10) {
                int id = getResources().getIdentifier(
                        "f00" + i,
                        "mipmap", getPackageName());
                emojiIds[i] = id;
                emojiList.add(id);
            } else if (i < 66) {
                int id = getResources().getIdentifier(
                        "f0" + i,
                        "mipmap", getPackageName());
                emojiIds[i] = id;
                emojiList.add(id);
            }
        }
        EmojiAdapter emojiAdapter = new EmojiAdapter(ArticleActivity.this, emojiList);
        emoji_grid.setAdapter(emojiAdapter);
        emoji_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeResource(getResources(), emojiIds[i % emojiIds.length]);
                bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);//设置表情大小
                ImageSpan imageSpan = new ImageSpan(ArticleActivity.this, bitmap);
                String str = null;
                if (i < 10) {
                    str = "f00" + i;
                } else if (i < 66) {
                    str = "f0" + i;
                }
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                chat_edit.getText().insert(chat_edit.getSelectionStart(), spannableString);
            }
        });
    }

    private void initCommentList(){
        listview = findViewById(R.id.listview);
        NestedScrollView scrollView = findViewById(R.id.scrollview);
        commentAdapter = new CommentAdapter(data.getComments(),this,data.getReplys());
        listview.setAdapter(commentAdapter);
        listview.setTranscriptMode(TRANSCRIPT_MODE_NORMAL);
    }

    private void initSendComment(){
        findViewById(R.id.chat_send).setOnClickListener(v -> {
            if (!chat_edit.getText().toString().equals("")){
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                CommentsBean commentsBean = GsonUtil.getGson().fromJson(jb.getString("comment"), CommentsBean.class);
                                if (commentsBean!=null){
                                    if (data.getComments() == null)data.setComments(new ArrayList<>());
                                    data.getComments().add(0,commentsBean);
                                    commentAdapter.update(commentsBean);
                                    chat_edit.setText("");
                                    hideSoftKeyboard(ArticleActivity.this);
                                    listview.setSelection(commentAdapter.getCount()-1);
                                    newComments.add(commentsBean);
                                    setResultData(null);
                                }
                            }else{
                                ShowUtil.showToast(ArticleActivity.this,"评论失败，请稍后再试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                        ShowUtil.showToast(ArticleActivity.this,"评论失败，请检查网络状况后重试");
                    }
                };
                ArticlePresenter.getInstance().addComment(data.getArticle().getId(),chat_edit.getText().toString(),callback);
            }else {
                ShowUtil.showToast(this,"请输入评论内容哦！");
            }
        });
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
                ShowUtil.showToast(ArticleActivity.this,"打开个人名片页面");
                startUserInfo();
                break;
            case R.id.del:
                ShowUtil.showToast(ArticleActivity.this,"删除");
                delArticle();
                break;
            case R.id.feedback:
                ShowUtil.showToast(ArticleActivity.this,"举报");
                feedbackArticle();
        }
        return true;
    }

    private void startUserInfo(){

    }

    private void delArticle(){

    }

    private void feedbackArticle(){

    }

    public void setResultData(ReplysBean replysBean){
        if (replysBean!=null){
            newReplys.add(replysBean);
        }
        Intent resultIntent = new Intent();
        if (newLikes!=null) resultIntent.putExtra("like",GsonUtil.getGson().toJson(newLikes));
        if (newComments.size()>0)resultIntent.putExtra("comments",GsonUtil.getGson().toJson(newComments));
        if (newReplys.size()>0)resultIntent.putExtra("replys",GsonUtil.getGson().toJson(newReplys));
        setResult(1,resultIntent);
    }

    //隐藏软键盘
    private void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void startReplyActivity(String strData){
        Log.i(TAG, "startReplyActivity: "+strData);
        Intent intent = new Intent(this,ReplyActivity.class);
        intent.putExtra("data",strData);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            String strData = data.getStringExtra("data");
            if (strData!=null && !strData.equals("")){
                Log.e(TAG, "onActivityResult: "+strData );
                List<ReplysBean> replysBeanList = GsonUtil.getGson().fromJson(strData, new TypeToken<List<ReplysBean>>(){}.getType());
                newReplys.addAll(replysBeanList);
            }
            setResultData(null);
        }else {
            Log.i(TAG, "onActivityResult: result is null !");
        }
    }
}
