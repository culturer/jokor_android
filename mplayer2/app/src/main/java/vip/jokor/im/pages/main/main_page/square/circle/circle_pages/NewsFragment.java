package vip.jokor.im.pages.main.main_page.square.circle.circle_pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.pages.main.main_page.square.recommend.RecommendAdapter;
import vip.jokor.im.pages.util.article.ArticleEvent;
import vip.jokor.im.presenter.CirclePresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    String TAG = "NewsFragment" ;

    private static final String STRDATA = "strData";
    private static final String ARG_PARAM2 = "param2";

    private String strData;
    private String mParam2;

    private CircleData circleData;

    private View contentView;
    private RecyclerView listView;
    private List<FriendsArticlesBean.ArticlesBean> datas = new ArrayList<>();
    RecommendAdapter recommendAdapter;

    private int page_count = 0;

    public NewsFragment() { }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(STRDATA, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strData = getArguments().getString(STRDATA);
            mParam2 = getArguments().getString(ARG_PARAM2);
            circleData = GsonUtil.getGson().fromJson(strData, CircleData.class);
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_news, container, false);
        listView = contentView.findViewById(R.id.news_list);
        recommendAdapter = new RecommendAdapter(R.layout.recommends_list_item,datas);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //防止Item切换
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);
        RecyclerViewDivider
                .with(getContext())
                .size(SizeUtil.dip2px(getContext(),2))
                .asSpace()
                .build()
                .addTo(listView);
        listView.setAdapter(recommendAdapter);
        EventBus.getDefault().register(this);
        return contentView ;
    }

    private void initData(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.e(TAG, "onSuccess: "+t );
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        FriendsArticlesBean friendsArticlesBean = GsonUtil.getGson().fromJson(t,FriendsArticlesBean.class);
                        if (friendsArticlesBean.getArticles()!=null)page_count++;
                        datas.addAll(friendsArticlesBean.getArticles());
                        if (recommendAdapter!=null){
                            recommendAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        };
        CirclePresenter.getInstance().getCircleArticles(circleData.getCircle().getId(),callback,page_count);
    }

    @Subscribe
    public void update(ArticleEvent event){
        if (event.getOption() == ArticleEvent.ADD){
            if (event.getCircleIds() == circleData.getCircle().getId()){
                datas.add(0,event.getData());
                recommendAdapter.notifyDataSetChanged();
            }
        }
    }
}
