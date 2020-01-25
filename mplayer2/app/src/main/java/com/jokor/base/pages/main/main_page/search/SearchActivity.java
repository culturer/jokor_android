package com.jokor.base.pages.main.main_page.search;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.jokor.base.R;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.util.base.StatusBarUtil;

public class SearchActivity extends AppCompatActivity {
	
	String TAG = "SearchActivity";
	
//	SearchAdapter searchAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initToolBar();
		initList();
		initSearch();
	}
	
	private void initList(){
		ListView list = findViewById(R.id.list);
//		searchAdapter = new SearchAdapter(userRelation,mgRelations);
//		list.setAdapter(searchAdapter);
	}
	
	//1.搜索用户 2.搜索群
	//输入时进行本地搜索
	//点击搜索时进行服务器搜索
	private void initSearch(){
		SearchView search = findViewById(R.id.search);
		search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String s) {
				
				return false;
			}
		});
	}
	
	private void initToolBar(){
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		LinearLayout container = findViewById(R.id.container);
		container.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
		setSupportActionBar(toolbar);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
		finish();
		return false;
	}
	
//	private class SearchAdapter extends BaseAdapter{
//
//
//		public SearchAdapter(List<MRelation> userRelation, List<MGRelation> mgRelations) {
//			if (userRelation!=null)
//				this.userRelation = userRelation;
//			if (mgRelations!=null)
//				this.mgRelations = mgRelations;
//		}
//
//		@Override
//		public int getCount() {
//			int count = 0;
//			if (userBean!=null)
//				count+=1;
//			if (userRelation!=null)
//				count+=userRelation.size();
//			if (mgRelations!=null)
//				count+=mgRelations.size();
//			if (netGroups!=null)
//				count+=netGroups.size();
//			return count;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			int userBeanCount =0;
//			if (userBean != null &&position == 0){
//				return userBean;
//			}
//			if (userBean != null){
//				userBeanCount = 1;
//			}else {
//				userBeanCount = 0;
//			}
//			if (position<userRelation.size()+userBeanCount){
//				return userRelation.get(position-userBeanCount);
//			} else {
//				if (position<userRelation.size()+mgRelations.size()+userBeanCount){
//					return mgRelations.get(position-userRelation.size()-userBeanCount);
//				}else {
//					return netGroups.get(position-userRelation.size()-mgRelations.size()-userBeanCount);
//				}
//			}
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 1l;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(R.layout.iphonetreeview_list_item_view, null);
//			}
//			TextView tv = (TextView) convertView.findViewById(R.id.contact_list_item_name);
//			TextView state = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);
//			TextView label = convertView.findViewById(R.id.label);
//			ImageView icon = convertView.findViewById(R.id.icon);
//			Object item = getItem(position);
//			if (item instanceof UserBean){
//				UserBean userBean = (UserBean)getItem(position);
//				label.setVisibility(View.VISIBLE);
//				label.setText("VIP");
//				tv.setText(userBean.getName());
//				state.setText("爱生活...爱Android...");
//				RequestOptions options1 = new RequestOptions()
//						.placeholder(R.mipmap.img_error)//加载成功之前占位图
//						.error(R.mipmap.img_error)//加载错误之后的错误图
//						.override(400,400)//指定图片的尺寸
//						.circleCrop()
//						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
//				Glide.with(SearchActivity.this)
//						.load(userBean.getIcon())
//						.apply(options1)
//						.into(icon);
//				convertView.setOnClickListener(v -> {
//					Intent intent = new Intent(SearchActivity.this,UserInfoActivity.class);
//					Bundle datas = new Bundle();
//					datas.putInt(UserInfoActivity.USERINFO_TYPE,UserInfoActivity.STRANGER);
//					datas.putString(UserInfoActivity.USERINFO,GsonUtil.getGson().toJson(userBean));
//					intent.putExtras(datas);
//					startActivity(intent);
//				});
//			}
//			if (item instanceof MRelation){
//				MRelation relation = (MRelation)getItem(position);
//				label.setVisibility(View.VISIBLE);
//				label.setText("VIP");
//				tv.setText(relation.getUserName());
//				state.setText("爱生活...爱Android...");
//				RequestOptions options1 = new RequestOptions()
//						.placeholder(R.mipmap.img_error)//加载成功之前占位图
//						.error(R.mipmap.img_error)//加载错误之后的错误图
//						.override(400,400)//指定图片的尺寸
//						.circleCrop()
//						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
//				Glide.with(SearchActivity.this)
//						.load(relation.getUserIcon())
//						.apply(options1)
//						.into(icon);
//				convertView.setOnClickListener(v -> {
//					Intent intent = new Intent(SearchActivity.this,ChatActivity.class);
//					String strRelation = GsonUtil.getGson().toJson(relation);
//					intent.putExtra("relation",strRelation);
//					startActivity(intent);
//				});
//			}
//			if (item instanceof MGRelation ){
//				MGRelation mgRelation = (MGRelation)getItem(position);
//				label.setVisibility(View.GONE);
//				tv.setText(mgRelation.getGroupName()+"("+mgRelation.getGroupCount()+"人)");
//				state.setText("测试群！");
//				RequestOptions options1 = new RequestOptions()
//						.placeholder(R.mipmap.img_error)//加载成功之前占位图
//						.error(R.mipmap.logo)//加载错误之后的错误图
//						.override(400,400)//指定图片的尺寸
////						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//						.transform(new GlideRoundTransform(SearchActivity.this, 4))
////						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
////　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
////　　                .skipMemoryCache(true)//跳过内存缓存
////　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
////　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
////　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
//				Glide.with(SearchActivity.this)
//						.load(mgRelation.getGroupIcon())
//						.apply(options1)
//						.into(icon);
//				convertView.setOnClickListener(v -> {
//					Intent intent = new Intent(SearchActivity.this,GroupsActivity.class);
//					String strMGRelation = GsonUtil.getGson().toJson(mgRelation);
//					intent.putExtra("strMGRelation",strMGRelation);
//					startActivity(intent);
//				});
//			}
//			if(item instanceof MMGroup){
//				MMGroup mmGroup = (MMGroup)getItem(position);
//				label.setVisibility(View.GONE);
//				tv.setText(mmGroup.getName()+"("+mmGroup.getCount()+"人)");
//				state.setText("测试群！");
//				RequestOptions options1 = new RequestOptions()
//						.placeholder(R.mipmap.img_error)//加载成功之前占位图
//						.error(R.mipmap.logo)//加载错误之后的错误图
//						.override(400,400)//指定图片的尺寸
////						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//						.transform(new GlideRoundTransform(SearchActivity.this, 4))
////						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
////　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
////　　                .skipMemoryCache(true)//跳过内存缓存
////　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
////　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
////　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
//				Glide.with(SearchActivity.this)
//						.load(mmGroup.getIcon())
//						.apply(options1)
//						.into(icon);
//				convertView.setOnClickListener(v -> {
//					Intent intent = new Intent(SearchActivity.this,GroupInfoActivity.class);
//					String strMMGroup = GsonUtil.getGson().toJson(mmGroup);
//					intent.putExtra(GroupInfoActivity.GROUP_INFO,strMMGroup);
//					startActivity(intent);
//				});
//			}
//			return convertView;
//		}
//
//		public void update(UserBean userBean,List<MRelation> userRelation,List<MGRelation> mgRelations,List<MMGroup> netGroups ){
//			if (userBean != null){
//				this.userBean = userBean;
//			}
//			if (userRelation!=null){
//				this.userRelation = userRelation;
//			}
//			if (mgRelations!=null){
//				this.mgRelations = mgRelations;
//			}
//			if (netGroups !=null){
//				this.netGroups = netGroups;
//			}
//			notifyDataSetChanged();
//		}
//	}

}
