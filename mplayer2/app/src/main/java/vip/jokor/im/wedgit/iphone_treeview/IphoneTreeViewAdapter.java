package vip.jokor.im.wedgit.iphone_treeview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.model.stores_bean.MGRelation;
import vip.jokor.im.model.stores_bean.MGroup;
import vip.jokor.im.model.stores_bean.MMGroup;
import vip.jokor.im.model.stores_bean.MRelation;
import vip.jokor.im.util.glide.GlideRoundTransform;

public class IphoneTreeViewAdapter extends BaseExpandableListAdapter
		implements IphoneTreeView.IphoneTreeHeaderAdapter {
	// Sample data set. children[i] contains the children (String[]) for
	// groups[i].
	private LayoutInflater mInflater;
	private IphoneTreeView iphoneTreeView;
	private HashMap<Integer, Integer> groupStatusMap;

	private List<MGroup> groups ;
	private List<List<MRelation>> relations;
	private MMGroup mmGroup;
	private List<MGRelation> mgRelations;
	
	private Activity activity;
	
	public IphoneTreeViewAdapter(
			Activity activity,
			IphoneTreeView iphoneTreeView,
			List<MGroup> groups,
			List<MRelation> mRelations,
			MMGroup mmGroup,
			List<MGRelation> mgRelations
	) {
		// TODO Auto-generated constructor stub
		 mInflater= activity.getLayoutInflater();
		this. iphoneTreeView=iphoneTreeView;
		groupStatusMap = new HashMap<Integer, Integer>();
		this.groups = groups;
		this.activity = activity;
		relations = new ArrayList<>();
		for (int i=0;i<groups.size();i++){
			relations.add(new ArrayList<>());
		}
		for (int i=0;i<mRelations.size();i++){
			for (int j=0;j<groups.size();j++){
				if (mRelations.get(i).getGroupId() == groups.get(j).getId()){
					relations.get(j).add(mRelations.get(i));
				}
			}
		}
		if (mmGroup!=null){
			this.mmGroup =mmGroup;
		}
		this.mgRelations = mgRelations;
	}

	public MRelation getChild(int groupPosition, int childPosition) {
		if (groupPosition<groups.size()){
			return relations.get(groupPosition).get(childPosition);
		}else {
			return null;
		}
	}

	public long getChildId(int groupPosition, int childPosition) {
		if (groupPosition<groups.size()){
			return getChild(groupPosition,childPosition).getId();
		}else {
			if (mmGroup!=null && childPosition == 0){
				return mmGroup.getId();
			}else {
				return mgRelations.get(childPosition-1).getId();
			}
		}
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition<groups.size()){
			return relations.get(groupPosition).size();
		}else {
			if (mgRelations!=null){
				if (mmGroup != null){
					return 1+mgRelations.size();
				}else {
					return mgRelations.size();
				}
			}else {
				if (mmGroup == null){
					return 0;
				}else {
					return 1;
				}
			}
		}
	}

	public MGroup getGroup(int groupPosition) {
		if (groupPosition<groups.size()){
			return groups.get(groupPosition);
		}else {
			return null;
		}
	}

	public int getGroupCount() {
		return groups.size()+1;
	}

	public long getGroupId(int groupPosition) {
		if (groupPosition<groups.size()){
			return getGroup(groupPosition).getId();
		}else {
			return -1;
		}
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.iphonetreeview_list_item_view, null);
		}
		TextView tv = convertView.findViewById(R.id.contact_list_item_name);
		TextView state = convertView.findViewById(R.id.cpntact_list_item_state);
		TextView label = convertView.findViewById(R.id.label);
		ImageView icon = convertView.findViewById(R.id.icon);
		if (groupPosition<groups.size()){
			label.setVisibility(View.VISIBLE);
			label.setText("VIP");
			tv.setText(getChild(groupPosition, childPosition).getUserName());
			state.setText("爱生活...爱Android...");
			RequestOptions options = new RequestOptions()
					.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
					.circleCrop()
//						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
			Glide.with(activity)
					.load(getChild(groupPosition, childPosition).getUserIcon())
					.apply(options)
					.into(icon);
		}else{
			if (mmGroup !=null && childPosition == 0){
				label.setVisibility(View.VISIBLE);
				label.setText("MY!");
				tv.setText(mmGroup.getName());
				state.setText(mmGroup.getMsg());
				RequestOptions options = new RequestOptions()
						.error(R.mipmap.logo)//加载错误之后的错误图
						.override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
						.transform(new GlideRoundTransform(activity, 4))
//						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
				Glide.with(activity)
						.load(mmGroup.getIcon())
						.apply(options)
						.into(icon);
			}else {
				label.setVisibility(View.GONE);
				MGRelation mgRelation = mgRelations.get(childPosition-1);
				tv.setText(mgRelation.getGroupName());
//				state.setText(mgRelation.get());
				RequestOptions options = new RequestOptions()
						.error(R.mipmap.logo)//加载错误之后的错误图
						.override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
						.transform(new GlideRoundTransform(activity, 4))
//						.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//　　                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
				Glide.with(activity)
						.load(mgRelation.getGroupIcon())
						.apply(options)
						.into(icon);
			}

		}
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition,
							 boolean isExpanded,
						View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.iphonetreeview_list_group_view, null);
		}
		TextView groupName = convertView.findViewById(R.id.group_name);
		ImageView indicator = convertView.findViewById(R.id.group_indicator);
		TextView onlineNum = convertView.findViewById(R.id.online_count);
		if (groupPosition<groups.size()){
			groupName.setText(getGroup(groupPosition).getName());
			onlineNum.setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
		}else {
			groupName.setText("群");
			int count;
			if (mmGroup == null){
				count = mgRelations.size();
			}else {
				count = mgRelations.size()+1;
			}
			onlineNum.setText(""+count+ "/"+count);
		}
		if (isExpanded) {
			indicator.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
		} else {
			indicator.setImageResource(R.drawable.ic_chevron_right_black_24dp);
		}
		return convertView;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1&& !iphoneTreeView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		if (groupPosition<groups.size()){
			((TextView) header.findViewById(R.id.group_name)).setText(getGroup(groupPosition).getName());
			((TextView) header.findViewById(R.id.online_count)).setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
		}else {
			((TextView) header.findViewById(R.id.group_name)).setText("群");
			((TextView) header.findViewById(R.id.online_count)).setText("5/10");
		}
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		// TODO Auto-generated method stub
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}
	
	public void addRelation(MRelation relation){
		for (int i=0;i<groups.size();i++){
			if (groups.get(i).getId() == relation.getGroupId()){
				relations.get(i).add(relations.get(i).size(),relation);
				break;
			}
		}
		notifyDataSetChanged();
	}
	
	public void updateRelation(List<MGroup> groups,List<MRelation> mRelations){
		this.groups = groups;
		relations = new ArrayList<>();
		for (int i=0;i<groups.size();i++){
			relations.add(new ArrayList<>());
		}
		for (int i=0;i<mRelations.size();i++){
			for (int j=0;j<groups.size();j++){
				if (mRelations.get(i).getGroupId() == groups.get(j).getId()){
					relations.get(j).add(mRelations.get(i));
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void updateGRelation(MMGroup mmGroup,List<MGRelation> mgRelations){
		this.mmGroup = mmGroup;
		this.mgRelations = mgRelations;
		notifyDataSetChanged();
	}
}