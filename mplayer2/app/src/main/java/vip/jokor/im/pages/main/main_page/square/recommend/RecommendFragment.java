package vip.jokor.im.pages.main.main_page.square.recommend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.pages.util.article.ArticleActivity;
import vip.jokor.im.presenter.RecommendPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends Fragment {

    String TAG = "RecommendFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View contentView;
    private View headerView;

    private RecyclerView recommends_list;
    RecommendAdapter recommendAdapter;

    List<FriendsArticlesBean.ArticlesBean> datas = new ArrayList<>();

    public RecommendFragment() {
        // Required empty public constructor
    }

    public static RecommendFragment newInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
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
        contentView = inflater.inflate(R.layout.fragment_recommend, container, false);

        recommends_list = contentView.findViewById(R.id.recommends_list);

        recommendAdapter = new RecommendAdapter(R.layout.recommends_list_item,datas);
        recommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            //点击进入详情
            FriendsArticlesBean.ArticlesBean item = recommendAdapter.getItem(position);
            Intent intent = new Intent(getContext(), ArticleActivity.class);
            intent.putExtra("data", GsonUtil.getGson().toJson(item));
            startActivity(intent);
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //防止Item切换
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommends_list.setLayoutManager(linearLayoutManager);
        RecyclerViewDivider
                .with(getContext())
                .size(SizeUtil.dip2px(getContext(),2))
                .asSpace()
                .build()
                .addTo(recommends_list);
        recommends_list.setAdapter(recommendAdapter);
        return contentView;
    }

    private void initData(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        FriendsArticlesBean friendsArticlesBean = GsonUtil.getGson().fromJson(t,FriendsArticlesBean.class);
                        Datas.setRecommends(friendsArticlesBean.getArticles());
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
        RecommendPresenter.getInstance().getRecommends(callback);
    }
}
