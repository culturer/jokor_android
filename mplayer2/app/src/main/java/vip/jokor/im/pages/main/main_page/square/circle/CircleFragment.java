package vip.jokor.im.pages.main.main_page.square.circle;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.Circle;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleUser;
import vip.jokor.im.pages.main.main_page.square.circle.search_circle.SearchCircleActivity;
import vip.jokor.im.presenter.CirclePresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CircleFragment extends Fragment {

    private static final String TAG = "CircleFragment" ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View contentView;
    private RecyclerView rv_waterfall;
    private SmartRefreshLayout refreshlayout;

    private List<CircleData> datas = new ArrayList<>();
    CircleAdapter circleAdapter;


    public CircleFragment() {
        // Required empty public constructor
    }

    public static CircleFragment newInstance(String param1, String param2){
        CircleFragment fragment = new CircleFragment();
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
        contentView = inflater.inflate(R.layout.fragment_circle, container, false);
        initSearch();
//        initList();
//        initRefresh();
        initListView();
        EventBus.getDefault().register(this);
        return contentView ;
    }

    private void initSearch(){
        FloatingActionButton floatbutton = contentView.findViewById(R.id.floatbutton);
        floatbutton.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), SearchCircleActivity.class),0));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            //处理返回结果
            String strData = data.getStringExtra("data");
            Log.e(TAG, "onActivityResult: "+strData );
        }
    }

    private void initListView(){
        rv_waterfall = contentView.findViewById(R.id.rv_waterfall);
        circleAdapter = new CircleAdapter(R.layout.item_circle,datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //防止Item切换
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_waterfall.setLayoutManager(linearLayoutManager);
        rv_waterfall.setAdapter(circleAdapter);
        circleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(),CircleActivity.class);
                intent.putExtra("data", GsonUtil.getGson().toJson(datas.get(position)));
                startActivityForResult(intent,0);
            }
        });
        RecyclerViewDivider
                .with(getContext())
                .size(SizeUtil.dip2px(getContext(),2))
                .asSpace()
                .build()
                .addTo(rv_waterfall);
    }

    private void initData(){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
//                Log.e(TAG, "getCircles onSuccess: "+t );
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200) {
                        String strCircle = jb.getString("circle");
                        String strCusers = jb.getString("cusers");
                        List<Circle> circles = GsonUtil.getGson().fromJson(strCircle, new TypeToken<List<Circle>>() {}.getType());
                        List<CircleUser> circleUsers = GsonUtil.getGson().fromJson(strCusers, new TypeToken<List<CircleUser>>() {}.getType());
                        Log.e(TAG, "onSuccess: " +GsonUtil.getGson().toJson(circles));
                        Log.e(TAG, "onSuccess: "+GsonUtil.getGson().toJson(circleUsers) );
                        for (CircleUser item : circleUsers){
                            for (Circle item1 :circles){
                                if (item.getCircleId() == item1.getId()){
                                    datas.add(new CircleData(item1,item));
                                }
                            }
                        }
                        Datas.setCircles(datas);
                        if (circleAdapter!=null)circleAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                Log.e(TAG, "getCircles onFailure: "+errorNo+strMsg );
            }
        };
        CirclePresenter.getInstance().getCircles(callback);
    }

    @Subscribe
    public void update(CircleEvent event){
        if (event.option == CircleEvent.OPTION_ADD){
            if (datas!=null)datas.add(0,event.data);
            if (circleAdapter!=null)circleAdapter.notifyDataSetChanged();
        }
        if (event.option == CircleEvent.OPTION_UPDATE){
            Log.e(TAG, "update: "+GsonUtil.getGson().toJson(event.data));
            for (int i=0;i<datas.size();i++){
                if (datas.get(i).getCircle().getId() == event.data.getCircle().getId()){
                    datas.set(i,event.data);
                    circleAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

//    private ArrayList<Integer> imageIds = new ArrayList<>();
//    private int[] ids = {R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,R.mipmap.p6,R.mipmap.p7,
//            R.mipmap.p8,R.mipmap.p9,R.mipmap.p10,R.mipmap.p11,R.mipmap.p12};
//    private CircleAdapter adapter;
//

//    private void initList(){
//        rv_waterfall = contentView.findViewById(R.id.rv_waterfall);
//        rv_waterfall.setHasFixedSize(true);
//        rv_waterfall.setItemAnimator(null);
//        //垂直方向的2列
//        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        //防止Item切换
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        rv_waterfall.setLayoutManager(layoutManager);
//        final int spanCount = 2;
//        RecyclerViewDivider
//                .with(getContext())
//                .size(SizeUtil.dip2px(getContext(),3))
//                .asSpace()
//                .build()
//                .addTo(rv_waterfall);
//
//        //解决底部滚动到顶部时，顶部item上方偶尔会出现一大片间隔的问题
//        rv_waterfall.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                int[] first = new int[spanCount];
//                layoutManager.findFirstCompletelyVisibleItemPositions(first);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
//                    layoutManager.invalidateSpanAssignments();
//                }
//            }
//        });
//
//        //设置Adapter
//        for(int i = 0 ; i < ids.length;i++){
//            imageIds.add(ids[i]);
//        }
//        adapter = new CircleAdapter();
//        adapter.setOnitemClick(position -> {
//            startActivityForResult(new Intent(getContext(),CircleActivity.class),0);
//        });
//        rv_waterfall.setAdapter(adapter);
//        adapter.replaceAll(imageIds);
//
//    }

//    private void initRefresh(){
//        refreshlayout = contentView.findViewById(R.id.refreshlayout);
//        //设置下拉刷新和上拉加载监听
//        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.replaceAll(getData());
//                    }
//                },2000);
//            }
//        });
//
//        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore();
//                adapter.addData(adapter.getItemCount(),getData());
////                new Handler().postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////
////                    }
////                },2000);
//            }
//        });
//    }

//    private ArrayList<Integer>  getData() {
//        ArrayList<Integer> list = new ArrayList<>();
//        for(int i = 0 ; i < 6;i++){
//            list.add(ids[i]);
//        }
//        return list;
//    }
    
}
