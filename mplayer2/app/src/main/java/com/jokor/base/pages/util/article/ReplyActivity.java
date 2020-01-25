package com.jokor.base.pages.util.article;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jokor.base.R;
import com.jokor.base.model.bean.ReplysBean;
import com.jokor.base.pages.main.main_page.chat.chat_page.EmojiAdapter;
import com.jokor.base.presenter.ArticlePresenter;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;
import com.jokor.base.util.base.TimeUtil;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReplyActivity extends AppCompatActivity {

    private String TAG = "ReplyActivity" ;

    private Comment data;
    private EditText chat_edit;
    private TextView chat_send;
    private ReplyAdapter replyAdapter;

    private  List<ReplysBean> newReplys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        initData();
        initToolbar();
        initView();
        initList();
        initEmoji();
        sendReply();
    }

    private void initData(){
        Intent intent = getIntent();
        String strData = intent.getStringExtra("data");
        if (!strData.equals("")){
            data = GsonUtil.getGson().fromJson(strData,Comment.class);
        }
    }

    private void initToolbar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
//        View container = findViewById(R.id.container);
//        container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
    }

    private void initView(){
        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.add).setOnClickListener(v -> {
            ShowUtil.showToast(ReplyActivity.this,"添加好友");
        });
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        TextView username = findViewById(R.id.username);
        TextView content = findViewById(R.id.content);
        TextView time = findViewById(R.id.time);
        ImageView icon = findViewById(R.id.icon);
        ImageView icon1 = findViewById(R.id.icon1);
        chat_edit = findViewById(R.id.chat_edit);

        String strTitle;
        if (data.getReplys()!=null && data.getReplys().size()!=0){
            strTitle = "评论("+data.getReplys().size()+")";
        }else {
            strTitle = "评论";
        }
        toolbar_title.setText(strTitle);
        username.setText(data.commentsBean.getUsername());
        content.setText(data.commentsBean.getContent());
        time.setText(TimeUtil.date2Str(TimeUtil.String2Date(data.commentsBean.getCreateTime())));
        RequestOptions options = new RequestOptions()
                .override(SizeUtil.dip2px(this,40),SizeUtil.dip2px(this,40))//指定图片的尺寸
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
        Glide.with(this)
                .load(data.getCommentsBean().getIcon())
                .apply(options)
                .into(icon);
        Glide.with(this)
                .load(data.getCommentsBean().getIcon())
                .apply(options)
                .into(icon1);

    }

    private void initList(){
        ListView listview = findViewById(R.id.listview);
        replyAdapter = new ReplyAdapter(this,data.replys);
        listview.setAdapter(replyAdapter);
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
                    hideSoftKeyboard(ReplyActivity.this);
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
        EmojiAdapter emojiAdapter = new EmojiAdapter(ReplyActivity.this, emojiList);
        emoji_grid.setAdapter(emojiAdapter);
        emoji_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeResource(getResources(), emojiIds[i % emojiIds.length]);
                bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);//设置表情大小
                ImageSpan imageSpan = new ImageSpan(ReplyActivity.this, bitmap);
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

    private void sendReply(){
        chat_send = findViewById(R.id.chat_send);
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strData = chat_edit.getText().toString();

                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "add reply onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                String strData = jb.getString("reply");
                                ReplysBean replysBean = GsonUtil.getGson().fromJson(strData,ReplysBean.class);
                                replyAdapter.update(replysBean);
                                chat_edit.setText("");
                                hideSoftKeyboard(ReplyActivity.this);
                                newReplys.add(replysBean);
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("data",GsonUtil.getGson().toJson(newReplys));
                                setResult(1,resultIntent);
                            }else{
                                ShowUtil.showToast(getApplicationContext(),"回复失败，请稍后再试");
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
                ArticlePresenter.getInstance().addReply(ReplyActivity.this.data.getCommentsBean().getArticleId(),ReplyActivity.this.data.getCommentsBean().getId(),strData,callback);
            }
        });
    }

    //隐藏软键盘
    private void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
