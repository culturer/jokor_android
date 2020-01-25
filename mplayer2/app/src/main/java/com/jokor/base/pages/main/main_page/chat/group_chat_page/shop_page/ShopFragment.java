package com.jokor.base.pages.main.main_page.chat.group_chat_page.shop_page;

import android.content.Context;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jokor.base.R;
import com.jokor.base.wedgit.PagerAdapter;

import com.jokor.base.util.glide.MediaLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private String mParam1;
	private String mParam2;
	
	private View contentView;
	private GridView gridView;
	
	private TabLayout shop_tab;
	private ViewPager shop_pager;
	private PagerAdapter pagerAdapter;
	
	private List<String> pagerList = new ArrayList<>();
	private List<Fragment> fragmentList = new ArrayList<>();
	
	public ShopFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ShopFragment.
	 */
	// TODO: Rename and change types and number of parameters
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		contentView = inflater.inflate(R.layout.fragment_shop, container, false);
		initBanner();
		initGrid();
		initPager();
		return contentView;
	}
	
	private void initBanner(){
		Banner banner = contentView.findViewById(R.id.banner);
		List<Integer> imgs = new ArrayList<>();
		imgs.add(R.mipmap.video_adv1);
		imgs.add(R.mipmap.video_adv2);
		imgs.add(R.mipmap.video_adv3);
		
		//设置图片加载器
		banner.setImageLoader(new MediaLoader());
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		banner.setIndicatorGravity(BannerConfig.RIGHT);
		banner.setImages(imgs);
		banner.setOnBannerListener(new OnBannerListener() {
			@Override
			public void OnBannerClick(int position) {
//				Toast.makeText(getContext(),"点击了第"+position+"个广告",Toast.LENGTH_LONG).show();
			}
		});
		//banner设置方法全部调用完毕时最后调用
		banner.start();
	}
	
	private void initGrid(){
		gridView = contentView.findViewById(R.id.gridView);
		List<GridBean> datas = new ArrayList<>();
		datas.add(new GridBean(R.mipmap.logo_paimai,"拍卖"));
		datas.add(new GridBean(R.mipmap.logo_manjian,"满减"));
		datas.add(new GridBean(R.mipmap.logo_zhekou,"折扣"));
		datas.add(new GridBean(R.mipmap.logo_miaosha,"秒杀"));
		GridAdapter adapter = new GridAdapter(datas,getContext());
		gridView.setAdapter(adapter);
	}
	
	private void initPager(){
		shop_pager = contentView.findViewById(R.id.shop_pager);
		shop_tab = contentView.findViewById(R.id.shop_tab);
		//MODE_SCROLLABLE可滑动的展示
		//MODE_FIXED固定展示
		if (pagerList.size()<=5){
			shop_tab.setTabMode(TabLayout.MODE_FIXED);
		}else {
			shop_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
		}
		pagerList.add("美食");
		pagerList.add("水果");
		pagerList.add("饮品");
		pagerList.add("零食");
		fragmentList.add(GoodsFragment.newInstance("",""));
		fragmentList.add(GoodsFragment.newInstance("",""));
		fragmentList.add(GoodsFragment.newInstance("",""));
		fragmentList.add(GoodsFragment.newInstance("",""));
		shop_tab.removeAllTabs();
		shop_tab.setSelectedTabIndicatorColor(getContext().getColor(R.color.black));
		for (int i=0;i<pagerList.size();i++){
			shop_tab.addTab(shop_tab.newTab().setText(pagerList.get(i)));
		}
		pagerAdapter = new PagerAdapter(getChildFragmentManager(),fragmentList,pagerList);
		shop_pager.setAdapter(pagerAdapter);
		shop_tab.setupWithViewPager(shop_pager);
	}
	
	public class GridAdapter extends BaseAdapter {
		
		private List<GridBean> datas;
		private Context context;
		
		public GridAdapter(List<GridBean> datas, Context context) {
			this.datas = datas;
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return datas.size();
		}
		
		@Override
		public GridBean getItem(int position) {
			return datas.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View contentView = LayoutInflater.from(context).inflate(R.layout.grid_item,null);
			ImageView img = contentView.findViewById(R.id.img);
			TextView text = contentView.findViewById(R.id.text);
			img.setImageResource(getItem(position).getLogo());
			text.setText(getItem(position).getName());
			contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			return contentView;
		}
	}
	
	public class GridBean {
		
		private int logo;
		private String name;
		
		public GridBean() {
		}
		
		public GridBean(int logo, String name) {
			this.logo = logo;
			this.name = name;
		}
		
		public int getLogo() {
			return logo;
		}
		
		public void setLogo(int logo) {
			this.logo = logo;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
