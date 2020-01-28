package vip.jokor.im.pages.main.main_page.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.MainActivity;
import vip.jokor.im.pages.main.main_page.square.circle.CircleActivity;
import vip.jokor.im.pages.util.web.WebActivity;
import vip.jokor.im.pages.main.main_page.main.shop.ShopActivity;
import vip.jokor.im.pages.main.main_page.main.smart.SmartActivity;
import vip.jokor.im.pages.main.main_page.main.topic.TopicActivity;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.wedgit.util.ShowUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;

import static java.sql.Types.NULL;


public class MainFragment extends Fragment {
	private String TAG = "MainFragment";
	
	private View contentView;
	
	public MainFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_main, container, false);
		initToolBar();
		initSearch();
		initGrid();
		return contentView;
	}

	private void initToolBar(){
		Toolbar toolbar = contentView.findViewById(R.id.toolbar);
		toolbar.setTitle("");
		AppBarLayout app_bar_layout = contentView.findViewById(R.id.app_bar_layout);
		app_bar_layout.setPadding(0, SizeUtil.getStatusBarHeight(getContext()),0,0);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), MainActivity.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		toggle.syncState();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
		Log.i(TAG, "onCreateOptionsMenu: ");
		menu.clear();
		inflater.inflate(R.menu.main, menu);
	}

	private void initSearch(){
		View search = contentView.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent(getContext(),SearchActivity.class);
				Intent intent = new Intent(getContext(),WebActivity.class);
				intent.putExtra("IsPlay",false);
				intent.putExtra("URL","https://m.sm.cn/");
				startActivity(intent);
			}
		});
		ImageView qrcode = contentView.findViewById(R.id.qrcode);
		qrcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				QrConfig qrConfig = new QrConfig.Builder()
						.setDesText("(识别二维码)")//扫描框下文字
						.setShowDes(false)//是否显示扫描框下面文字
						.setShowLight(true)//显示手电筒按钮
						.setShowTitle(true)//显示Title
						.setShowAlbum(true)//显示从相册选择按钮
						.setCornerColor(Color.WHITE)//设置扫描框颜色
						.setLineColor(Color.WHITE)//设置扫描线颜色
						.setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
						.setScanType(QrConfig.TYPE_QRCODE)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
						.setScanViewType(QrConfig.SCANVIEW_TYPE_QRCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
						.setCustombarcodeformat(QrConfig.BARCODE_I25)//此项只有在扫码类型为TYPE_CUSTOM时才有效
						.setPlaySound(true)//是否扫描成功后bi~的声音
						.setNeedCrop(true)//从相册选择二维码之后再次截取二维码
//						.setDingPath(R.raw.test)//设置提示音(不设置为默认的Ding~)
						.setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
						.setTitleText("扫描二维码")//设置Tilte文字
						.setTitleBackgroudColor(NULL)//设置状态栏颜色
						.setTitleTextColor(Color.WHITE)//设置Title文字颜色
						.create();
				QrManager.getInstance().init(qrConfig).startScan(getActivity(), new QrManager.OnScanResultCallback() {
					@Override
					public void onScanSuccess(String result) {
						ShowUtil.showToast(getActivity(), result);
					}
				});
			}
		});
	} 

	private void initGrid(){
		GridView gridView = contentView.findViewById(R.id.gridView);
		List<GridDataBean> items = new ArrayList<>();
		Intent intent = new Intent(getContext(),WebActivity.class);
		intent.putExtra("URL","http://v.sigu.me/");
		items.add(new GridDataBean(R.mipmap.ic_video,"影视",intent));
		Intent circle_intent = new Intent(getContext(), CircleActivity.class);
		items.add(new GridDataBean(R.mipmap.ic_quanzi,"圈子",circle_intent));
		Intent shop_intent = new Intent(getContext(),ShopActivity.class);
		items.add(new GridDataBean(R.mipmap.ic_shop,"商城",shop_intent));
		Intent topic_intent = new Intent(getContext(),TopicActivity.class);
		items.add(new GridDataBean(R.mipmap.ic_yule,"话题",topic_intent));
		Intent smart_intent = new Intent(getContext(),SmartActivity.class);
		items.add(new GridDataBean(R.mipmap.ic_smart,"智能",smart_intent));
		GridAdapter adapter = new GridAdapter(items,getContext());
		gridView.setAdapter(adapter);
	}
	
	public static class GridAdapter extends BaseAdapter{

		private List<GridDataBean> datas;
		private Context context;

		public GridAdapter(List<GridDataBean> datas,Context context) {
			this.datas = datas;
			this.context = context;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public GridDataBean getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View contentView = LayoutInflater.from(context).inflate(R.layout.grid_main_item,null);
			ImageView img = contentView.findViewById(R.id.img);
			TextView text = contentView.findViewById(R.id.text);
			text.setText(getItem(position).getTitle());
			img.setImageResource(getItem(position).getImg());
			contentView.setOnClickListener(v -> {
				Intent intent = getItem(position).getIntent();
				if (intent!=null){
					context.startActivity(intent);
				}else {
					Snackbar.make(contentView, "敬请期待！", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
				}
			});
			return contentView;
		}
	}

	public static class GridDataBean{

		private int img;
		private String title;
		private Intent intent;

		public GridDataBean(int img, String title, Intent intent) {
			this.img = img;
			this.title = title;
			this.intent = intent;
		}

		public int getImg() {
			return img;
		}

		public void setImg(int img) {
			this.img = img;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Intent getIntent() {
			return intent;
		}

		public void setIntent(Intent intent) {
			this.intent = intent;
		}
	}
}
