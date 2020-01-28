package vip.jokor.im.pages.main.main_page.square.circle.search_circle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.ihidea.multilinechooselib.MultiLineChooseLayout;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.square.circle.CircleActivity;
import vip.jokor.im.pages.main.main_page.square.circle.CircleEvent;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.Circle;
import vip.jokor.im.pages.main.main_page.square.circle.circle_create.CreateCircleActivity;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleUser;
import vip.jokor.im.presenter.CirclePresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.util.glide.GlideRoundTransform;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchCircleActivity extends AppCompatActivity {

    private String TAG = "SearchCircleActivity" ;

    private List<String> historys = new ArrayList<>();

    SearchView search;
    MultiLineChooseLayout flowLayout;
    ImageView close;

    View search_aricle_result;
    ImageView icon;
    TextView name;
    TextView desc;
    TextView focus;
    TextView null_result;

    View recommends;
    MultiLineChooseLayout search_fire_content;
    MultiLineChooseLayout search_yule_content;
    MultiLineChooseLayout search_tech_content;
    MultiLineChooseLayout search_live_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_circle);
        initToolBar();
        initBaseView();
        initRecommends();
//        setFlowLayout();
        initSearch();
    }


    private void initBaseView(){

        search_aricle_result = findViewById(R.id.search_aricle_result);
        search_aricle_result.setVisibility(View.GONE);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        focus = findViewById(R.id.focus);
        null_result = findViewById(R.id.null_result);

        recommends = findViewById(R.id.recommends);
        recommends.setVisibility(View.VISIBLE);

        search_fire_content = findViewById(R.id.search_fire_content);
        search_yule_content = findViewById(R.id.search_yule_content);
        search_tech_content = findViewById(R.id.search_tech_content);
        search_live_content = findViewById(R.id.search_live_content);

    }

    private void initRecommends(){

        List<String> fires = new ArrayList<>();
        fires.add("湖北4.7级地震");
        fires.add("湖北4.7级地震");
        fires.add("湖北4.7级地震");
        fires.add("湖北4.7级地震");
        List<String> yules = new ArrayList<>();
        yules.add("王者版本更新");
        yules.add("王者版本更新");
        yules.add("王者版本更新");
        yules.add("王者版本更新");
        yules.add("王者版本更新");
        List<String> techs = new ArrayList<>();
        techs.add("xx新机发售");
        techs.add("xx新机发售");
        techs.add("xx新机发售");
        techs.add("xx新机发售");
        techs.add("xx新机发售");
        List<String> lives = new ArrayList<>();
        lives.add("母猪上树拉");
        lives.add("母猪上树拉");
        lives.add("母猪上树拉");
        lives.add("母猪上树拉");
        lives.add("母猪上树拉");

        search_fire_content.setList(fires);
        search_yule_content.setList(yules);
        search_tech_content.setList(techs);
        search_live_content.setList(lives);

    }

    private void initToolBar(){
        StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        AppBarLayout app_bar_layout = findViewById(R.id.app_bar_layout);
        app_bar_layout.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
        setSupportActionBar(toolbar);
    }

    private void initSearch(){
        search = findViewById(R.id.search);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                //搜索到結果
                                String strCircle = jb.getString("circle");
                                Circle circle = GsonUtil.getGson().fromJson(strCircle,Circle.class);
                                search_aricle_result.setVisibility(View.VISIBLE);
                                recommends.setVisibility(View.GONE);
                                icon.setVisibility(View.VISIBLE);
                                name.setVisibility(View.VISIBLE);
                                desc.setVisibility(View.VISIBLE);
                                focus.setVisibility(View.VISIBLE);
                                null_result.setVisibility(View.GONE);
                                RequestOptions options = new RequestOptions()
                                        .error(R.mipmap.img_error)//加载错误之后的错误图
                                        .transform(new GlideRoundTransform(SearchCircleActivity.this,8))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
                                Glide.with(SearchCircleActivity.this)
                                        .load(circle.getIcon())
                                        .apply(options)
                                        .into(icon);
                                name.setText(circle.getName());
                                desc.setText(circle.getMsg());
                                focus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        HttpCallback callback2 = new HttpCallback() {
                                            @Override
                                            public void onSuccess(String t) {
                                                Log.e(TAG, "关注成功 onSuccess: "+t );
                                                try {
                                                    JSONObject jb = new JSONObject(t);
                                                    int status = jb.getInt("status");
                                                    if (status == 200){
                                                        //数据解析，包装
                                                        String strCuser = jb.getString("cuser");
                                                        CircleUser cuser = GsonUtil.getGson().fromJson(strCuser,CircleUser.class);
                                                        CircleData circleData = new CircleData(circle,cuser);
                                                        //消息总线发送消息
                                                        CircleEvent circleEvent = new CircleEvent(TAG,CircleEvent.OPTION_ADD,circleData);
                                                        EventBus.getDefault().post(circleEvent);
                                                        //弹出提示信息
                                                        ShowUtil.showToast(getApplicationContext(),"关注 "+circle.getName()+" 成功");
                                                        //启动对应的页面
                                                        Intent intent = new Intent(SearchCircleActivity.this, CircleActivity.class);
                                                        intent.putExtra("data",GsonUtil.getGson().toJson(circleData));
                                                        startActivityForResult(intent,0);
                                                        finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(VolleyError error) {
                                                Log.e(TAG, "关注失败 onFailure: "+error );
                                            }
                                        };
                                        CirclePresenter.getInstance().focus(circle.getId(),callback2);
                                    }
                                });
                            }else {
                                //沒有搜索到結果
                                search_aricle_result.setVisibility(View.VISIBLE);
                                recommends.setVisibility(View.GONE);
                                icon.setVisibility(View.GONE);
                                name.setVisibility(View.GONE);
                                desc.setVisibility(View.GONE);
                                focus.setVisibility(View.GONE);
                                null_result.setVisibility(View.VISIBLE);
                                null_result.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ShowUtil.showToast(SearchCircleActivity.this,"没有搜索到结果,在此要先校验是否，实名认证和手机号认证");
                                        Intent intent = new Intent(SearchCircleActivity.this, CreateCircleActivity.class);
                                        intent.putExtra("data",query);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Log.e(TAG, "onFailure: "+errorNo+strMsg );
                    }
                };
                CirclePresenter.getInstance().searchCircles(query,callback);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")){
                    recommends.setVisibility(View.VISIBLE);
                    search_aricle_result.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void setFlowLayout(){
        if (historys== null || historys.size()==0){
            close.setVisibility(View.GONE);
        }else {

            close.setVisibility(View.VISIBLE);
            close.setOnClickListener(v -> {
                historys.clear();
                flowLayout.setList(historys);
                close.setVisibility(View.GONE);
            });
            flowLayout.setList(historys);
            //单选
            flowLayout.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
                @Override
                public void onItemClick(int position, String text) {
                    search.setQuery(text,true);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_circle_menu, menu);
        return true;
    }

    //设置监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                finish();
                break;
        }
        return true;
    }
}
