package vip.jokor.im.pages.main.main_page.square.focus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.ArticleBean;
import vip.jokor.im.model.bean.CommentsBean;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.model.bean.LikesBean;
import vip.jokor.im.model.bean.ReplysBean;
import vip.jokor.im.pages.util.article.ArticleActivity;
import vip.jokor.im.pages.util.article.ArticleEvent;
import vip.jokor.im.presenter.ArticlePresenter;
import vip.jokor.im.pages.util.article.CreateArticleActivity;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FocusFragment extends Fragment {

    private static final String TAG = "FocusFragment" ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View contentView;

    private ListView list;
    private  List<FriendsArticlesBean.ArticlesBean> datas = new ArrayList<>();
    private FocusAdapter adapter;

    private int page = 0;

    public FocusFragment() {
        // Required empty public constructor
    }

    public static FocusFragment newInstance(String param1, String param2) {
        FocusFragment fragment = new FocusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_focus, container, false);
        initList();
        initFloatButton();
        EventBus.getDefault().register(this);
        return contentView;
    }

    private void initData(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "getFriendsArticles onSuccess: "+t);
                page++;
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if ( status == 200 ){
                        FriendsArticlesBean friendsArticlesBean = GsonUtil.getGson().fromJson(t,FriendsArticlesBean.class);
                        Datas.setFriends_articles(friendsArticlesBean.getArticles());
                        adapter.update(friendsArticlesBean.getArticles());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                Log.i(TAG, "getFriendsArticles onFailure: "+errorNo+" , "+strMsg);
                ShowUtil.showToast(getContext(),"加载数据失败！");
            }
        };
        ArticlePresenter.getInstance().getFriendsArticles(page,callback);
    }

    @Subscribe
    public void update(ArticleEvent event){
        if (event.getOption() == ArticleEvent.ADD){
            adapter.update(event.getData());
        }
    }

    private void initList(){
        list = contentView.findViewById(R.id.list);
        adapter = new FocusAdapter(this,datas,getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入详情
                FriendsArticlesBean.ArticlesBean item = adapter.getItem(position);
                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("data", GsonUtil.getGson().toJson(item));
                startActivity(intent);
            }
        });
    }

    private void initFloatButton(){
        FloatingActionButton floatingActionButton = contentView.findViewById(R.id.floatbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateArticleActivity.class);
                startActivityForResult(intent,0x01);
            }
        });
    }

    public void startArticlePage(String strData){
        Intent intent = new Intent(getContext(), ArticleActivity.class);
        intent.putExtra("data", strData);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.e(TAG, "onActivityResult: " );
        if (data!=null){
            String strArticle = data.getStringExtra("data");
            Log.e(TAG, "onActivityResult: article "+strArticle );
            if (strArticle!=null && !strArticle.equals("")){
                ArticleBean articleBean = GsonUtil.getGson().fromJson(strArticle,ArticleBean.class);
                if (articleBean!=null){
                    FriendsArticlesBean.ArticlesBean articlesBean = new FriendsArticlesBean.ArticlesBean();
                    articlesBean.setArticle(articleBean);
                    articlesBean.setComments(new ArrayList<>());
                    articlesBean.setLikes(new ArrayList<>());
                    articlesBean.setReplys(new ArrayList<>());
                    adapter.update(articlesBean);
                }
            }

            String strLike = data.getStringExtra("like");
            Log.e(TAG, "onActivityResult: like "+strLike );
            if (strLike!=null && !strLike.equals("")){
                LikesBean likesBean = GsonUtil.getGson().fromJson(strLike,LikesBean.class);
               adapter.update(likesBean);
            }

            String strComments = data.getStringExtra("comments");
            Log.e(TAG, "onActivityResult: comment "+strComments );
            if (strComments!=null && !strComments.equals("")){
                List<CommentsBean> commentsBeans = GsonUtil.getGson().fromJson(strComments, new TypeToken<List<CommentsBean>>(){}.getType());
                adapter.update1(commentsBeans);
            }

            String strReplys = data.getStringExtra("replys");
            Log.e(TAG, "onActivityResult: replys "+strReplys );
            if (strReplys!=null && !strReplys.equals("")){
                List<ReplysBean> replysBeans = GsonUtil.getGson().fromJson(strReplys, new TypeToken<List<ReplysBean>>(){}.getType());
                adapter.update2(replysBeans);
            }
        }
        
    }
}
