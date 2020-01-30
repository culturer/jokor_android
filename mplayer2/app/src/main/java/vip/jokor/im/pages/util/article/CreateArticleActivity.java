package vip.jokor.im.pages.util.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.google.gson.Gson;
import com.jaygoo.widget.RangeSeekBar;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.ArticleBean;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.presenter.ArticlePresenter;
import vip.jokor.im.util.base.FileUtil;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.QiNiuUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.util.glide.MediaLoader;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static vip.jokor.im.base.Urls.QINIU_URL;

public class CreateArticleActivity extends AppCompatActivity {

    private String TAG = "CreateArticleActivity" ;

    List<Uri> datas = new ArrayList<>();
    Uri videoUri = null;
    PhotoAdapter adapter;
    long circle_id;
    boolean is_show_main = false;

    View contentView;
    View headerView ;
    View show_video;
    TextView progress_text;
    RangeSeekBar progress;
    RecyclerView gridView;
    EditText content;
    JzvdStd jzvdStd;
    GridView emoji_grid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = getLayoutInflater().inflate(R.layout.activity_create_article,null);
        setContentView(contentView);
        initData();
        initToolBar();
        initHeader();
        initGridView();
        initProgress();
        initVideo();
//        initEmoji();
        initAt();

    }

    private void initData(){
        Intent intent = getIntent();
        if (intent!=null){
            circle_id = intent.getLongExtra("circle_id",-1);
        }
        if (datas == null) datas = new ArrayList<>();
        datas.add(Uri.parse("new"));
    }

    private void initToolBar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
        LinearLayout container = findViewById(R.id.container);
        container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("发布动态");
        toolbar.inflateMenu(R.menu.article_create);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    private void initGridView(){
        gridView = findViewById(R.id.gridView);

        gridView.setHasFixedSize(true);
        //垂直方向的2列
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        gridView.setLayoutManager(layoutManager);

        RecyclerViewDivider
                .with(CreateArticleActivity.this)
                .size(SizeUtil.dip2px(CreateArticleActivity.this,2))
                .asSpace()
                .build()
                .addTo(gridView);
        adapter = new PhotoAdapter(R.layout.photo_grid_item,datas);
        adapter.addHeaderView(headerView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (position == 0){
                addPhoto();
            }
        });
        gridView.setAdapter(adapter);
    }

    private void initHeader(){
        headerView = getLayoutInflater().inflate(R.layout.activity_create_article_header,null);
        content = headerView.findViewById(R.id.content);
        show_video = headerView.findViewById(R.id.show_video);
        ImageView del = headerView.findViewById(R.id.del);
        RadioButton show_at_main = headerView.findViewById(R.id.show_at_main);
        show_at_main.setChecked(is_show_main);
        if (circle_id!=-1){
            show_at_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_show_main){
                        is_show_main = false;
                    }else {
                        is_show_main = true;
                    }
                    show_at_main.setChecked(is_show_main);
                }
            });
            show_at_main.setVisibility(View.VISIBLE);
        }
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas.size() == 0)datas.add(Uri.parse("new"));
                adapter.notifyDataSetChanged();
                videoUri = null;
                show_video.setVisibility(View.GONE);
                Jzvd.releaseAllVideos();
            }
        });
        ImageView emoji = headerView.findViewById(R.id.emoji);
        ImageView at = headerView.findViewById(R.id.at);
        ImageView video = headerView.findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(CreateArticleActivity.this)
                        .runtime()
                        .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                        .onGranted(permissions -> {
                            Matisse.from(CreateArticleActivity.this)
                                    .choose(MimeType.ofVideo())
                                    .spanCount(2)
                                    .countable(false)
                                    .maxSelectable(1)
                                    //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 fileProvider
                                    .capture(true)
                                    .captureStrategy(new CaptureStrategy(true,"vip.jokor.im.fileProvider"))
                                    .theme(R.style.Matisse_Dracula)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new MediaLoader())
                                    .showSingleMediaType(true)
                                    .forResult(REQUEST_CODE_CHOOSE_VIDEO);
                        })
                        .onDenied(permissions -> {
                            Toast.makeText(CreateArticleActivity.this,"请授权",Toast.LENGTH_LONG).show();
                        })
                        .start();

            }
        });
    }

    private void initVideo(){
        jzvdStd = headerView.findViewById(R.id.jz_video);
    }


    private void initAt(){

    }

    private void addPhoto(){
        AndPermission.with(CreateArticleActivity.this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                .onGranted(permissions -> {
                    Matisse.from(CreateArticleActivity.this)
                            .choose(MimeType.ofImage())
                            .spanCount(2)
                            .countable(true)
                            .maxSelectable(100)
                            //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 fileProvider
                            .capture(true)
                            .captureStrategy(new CaptureStrategy(true,"com.jokor.im.fileProvider"))
                            .theme(R.style.Matisse_Dracula)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new MediaLoader())
                            .showSingleMediaType(true)
                            .forResult(REQUEST_CODE_CHOOSE_IMG);
                })
                .onDenied(permissions -> {
                    Toast.makeText(CreateArticleActivity.this,"请授权",Toast.LENGTH_LONG).show();
                })
                .start();
    }

    private void initProgress(){
        progress = headerView.findViewById(R.id.progress);
        progress.setEnabled(false);
        progress_text = headerView.findViewById( R.id.progress_text );
        progress.setProgress(0);
        progress.setVisibility(View.INVISIBLE);
        progress_text.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:finish();break;
            case R.id.publish:
                ShowUtil.showToast(CreateArticleActivity.this,"正在发布动态！");
                publish();
                break;
        }
        return true;
    }

    private void editAble(boolean isEditable){
        if (isEditable){
            progress.setVisibility(View.INVISIBLE);
            progress_text.setVisibility(View.GONE);
            content.setFocusable(true);
            adapter.setOnItemClickListener((adapter1, view, position) -> {
                if (position<datas.size()){
                    //点击图片
                }
            });
        }else {
            content.setFocusable(false);
        }
    }

    private void publish(){
        editAble(false);
        progress.setVisibility(View.VISIBLE);
        progress_text.setVisibility(View.VISIBLE);
        List<String> keys = new ArrayList<>();
        if ( datas != null && datas.size()>1 ){
            List<Uri> datas1 = new ArrayList<>(datas);
            if (datas1.size()>0)datas1.remove(0);
            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Log.i(TAG, "onSuccess: "+t);
                    try {
                        JSONObject jb = new JSONObject(t);
                        int status = jb.getInt("status");
                        if (status == 200){
                            String token = jb.getString("token");
                            UploadOptions uploadOptions = new UploadOptions(
                                    null,
                                    null,
                                    false,
                                    new UpProgressHandler(){
                                        public void progress(String key, double percent){
                                            double fProgress =(float) keys.size()*100/datas.size()+100*percent/datas.size();
                                            progress.setProgress((float) fProgress);
                                        }},
                                    null
                            );
                            //有图片，先上传图片
                            UpCompletionHandler handler1 = new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    Log.i(TAG, "complete: key "+key);
                                    Log.i(TAG, "complete: info "+info);
                                    Log.i(TAG, "complete: response "+response);
                                    if (info.isOK()){
                                        Log.e(TAG, "complete: response " + info.response);
                                        keys.add(key);
                                        //文件上传进度
                                        String strProgress = "已上传"+keys.size()+"个图片，一共"+datas.size()+"个图片";
                                        progress_text.setText(strProgress);
                                        if (datas1.size()>0){
                                            datas1.remove(0);
                                        }
                                        if (datas1.size()>0){
                                            File file =  FileUtil.uri2File(getApplicationContext(),datas1.get(0));
                                            if (file!=null){
                                                Log.i(TAG, "onSuccess: file length --- "+file.length());
                                                QiNiuUtil.getInstance().upload(token,file,"a_"+Datas.getUserInfo().getId()+System.currentTimeMillis(),this,uploadOptions);
                                            }
                                        }else {
                                            editAble(true);
                                            //文件上传完成
                                            progress_text.setText("文件上传完成,正在发布动态...");
                                            publish(keys,"");
                                        }
                                    }
                                }
                            };
                            File file =  FileUtil.uri2File(getApplicationContext(),datas1.get(0));
                            Log.i(TAG, "onSuccess: file length --- "+file.length());
                            QiNiuUtil.getInstance().upload(token,file,"a_"+System.currentTimeMillis(),handler1,uploadOptions);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(VolleyError error) {
                    Log.i(TAG, "onFailure: "+error);
                }
            };
            QiNiuUtil.getInstance().getToken(callback);
        }else {
            //没有图片
            editAble(true);
            publish(keys,"");
        }
    }

    public void publish(List<String> keys,String video){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        ShowUtil.showToast(getApplicationContext(),"发布动态成功！");
                        ArticleBean articlesBean = GsonUtil.getGson().fromJson(jb.getString("article"),ArticleBean.class);
                        if (articlesBean!=null){
                            FriendsArticlesBean.ArticlesBean articlesBean1 = new FriendsArticlesBean.ArticlesBean();
                            articlesBean1.setArticle(articlesBean);
                            articlesBean1.setReplys(new ArrayList<>());
                            articlesBean1.setLikes(new ArrayList<>());
                            articlesBean1.setComments(new ArrayList<>());
                            ArticleEvent articleEvent = new ArticleEvent(TAG,ArticleEvent.ADD,-1,articlesBean1);
                            EventBus.getDefault().postSticky(articleEvent);
                        }
                        finish();
                    }else {
                        ShowUtil.showToast(getApplicationContext(),"发布动态失败，请稍后再试！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                Log.i(TAG, "onFailure: "+errorNo+","+strMsg);
            }
        };
        List<String> images = new ArrayList<>();
        if (keys!=null){
            for (String item : keys){
                //拼出文件的完整路径
                images.add(QINIU_URL+item);
            }
        }
        String strImgs = new Gson().toJson(images);
        Log.e(TAG, "publish: "+strImgs );
        ArticlePresenter.getInstance().publishArticle(strImgs,video,content.getText().toString(),callback);
    }

    int REQUEST_CODE_CHOOSE_IMG = 0x01;
    int REQUEST_CODE_CHOOSE_VIDEO = 0x02;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_IMG && resultCode == RESULT_OK) {
            datas.addAll(Matisse.obtainResult(data));
            Log.e(TAG, "onActivityResult: "+datas );
            adapter.notifyDataSetChanged();
        }

        if (requestCode == REQUEST_CODE_CHOOSE_VIDEO && resultCode == RESULT_OK) {
            videoUri = Matisse.obtainResult(data).get(0);
            if (videoUri!=null){
                show_video.setVisibility(View.VISIBLE);
                File file =  FileUtil.uri2File(getApplicationContext(),videoUri);
                jzvdStd.setUp(file.getAbsolutePath(), "饺子闭眼睛");
                Glide.with(this).load(videoUri).into(jzvdStd.thumbImageView);
                jzvdStd.startVideo();
                datas.clear();
                adapter.notifyDataSetChanged();
            }else {
                jzvdStd.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
