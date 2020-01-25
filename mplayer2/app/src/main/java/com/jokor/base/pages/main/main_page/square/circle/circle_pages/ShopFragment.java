package com.jokor.base.pages.main.main_page.square.circle.circle_pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jokor.base.R;
import com.jokor.base.pages.main.main_page.main.MainFragment;
import com.jokor.base.util.glide.MediaLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View contentView;
    private View headerView;

    Banner banner;

    public ShopFragment() {
        // Required empty public constructor
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_shop2, container, false);
        headerView = inflater.inflate(R.layout.shop_header,null);
//        List<String> imgs = new ArrayList<String>(){
//            {
//                this.add("http://img5.imgtn.bdimg.com/it/u=3246630332,3044294395&fm=26&gp=0.jpg");
//                this.add("http://img0.imgtn.bdimg.com/it/u=2849768792,1404089788&fm=26&gp=0.jpg");
//                this.add("http://img0.imgtn.bdimg.com/it/u=2849768792,1404089788&fm=26&gp=0.jpg");
//            }
//        };
//        initBanner(imgs);
//        initGrid();
        return contentView ;

    }
    private void initBanner( List<String> images){
        banner = contentView.findViewById(R.id.banner);
        if (images == null || images.size() == 0){
            banner.setVisibility(View.GONE);
        }else {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置图片加载器
            banner.setImageLoader(new MediaLoader());
            //设置图片集合
            banner.setImages(images);
            //设置自动轮播，默认为true`
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3*1000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.setOnBannerListener(position -> {

            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    private void initGrid(){
        GridView gridView = contentView.findViewById(R.id.gridView);
        List<MainFragment.GridDataBean> items = new ArrayList<>();
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_video,"商品",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_quanzi,"订单",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_shop,"会员",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_yule,"代理",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_smart,"秒杀",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_smart,"砍价",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_smart,"拼团",null));
        items.add(new MainFragment.GridDataBean(R.mipmap.ic_smart,"预售",null));
        MainFragment.GridAdapter adapter = new MainFragment.GridAdapter(items,getContext());
        gridView.setAdapter(adapter);
    }

}
