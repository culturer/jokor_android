package com.jokor.base.pages.main.main_page.square.actives;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.jokor.base.R;
import com.jokor.base.pages.util.video.VideoActivity;
import com.jokor.base.util.base.SizeUtil;

import java.util.ArrayList;
import java.util.List;

public class ActiveFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View contentView;
    private RecyclerView recycler_view;
    ActiveAdapter activeAdapter;

    private List<ActiveItem> datas = new ArrayList<>();

    public ActiveFragment() {
        // Required empty public constructor
    }

    public static ActiveFragment newInstance(String param1, String param2) {
        ActiveFragment fragment = new ActiveFragment();
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
        // Inflate the layout for this fragment.
        contentView = inflater.inflate(R.layout.fragment_active, container, false);
        initRecycleView();
        return contentView;
    }

    private void initData(){
        datas.add(new ActiveItem(R.mipmap.gif1));
        datas.add(new ActiveItem(R.mipmap.gif2));
        datas.add(new ActiveItem(R.mipmap.gif3));
        datas.add(new ActiveItem(R.mipmap.gif4));
        datas.add(new ActiveItem(R.mipmap.gif5));
        datas.add(new ActiveItem(R.mipmap.gif6));
        datas.add(new ActiveItem(R.mipmap.gif7));
        datas.add(new ActiveItem(R.mipmap.gif8));
        datas.add(new ActiveItem(R.mipmap.gif9));
        datas.add(new ActiveItem(R.mipmap.gif10));
        datas.add(new ActiveItem(R.mipmap.gif11));
        datas.add(new ActiveItem(R.mipmap.gif12));

    }

    private void initRecycleView(){
        //列表，后面用来加载Gif图
        recycler_view = contentView.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        //垂直方向的2列
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recycler_view.setLayoutManager(layoutManager);

        RecyclerViewDivider
                .with(getContext())
                .size(SizeUtil.dip2px(getContext(),2))
                .asSpace()
                .build()
                .addTo(recycler_view);

        ActiveAdapter activeAdapter = new ActiveAdapter(R.layout.item_active,datas);
        activeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getContext(), VideoActivity.class));
            }
        });
        recycler_view.setAdapter(activeAdapter);
    }

}
