package vip.jokor.im.wedgit.iphone_treeview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.GetFriendBean;
import vip.jokor.im.model.bean.CategorysBean;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.GroupBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vip.jokor.im.model.db.Msg;

public class TreeViewAdapter extends BaseExpandableListAdapter implements IphoneTreeView.IphoneTreeHeaderAdapter {

	private static final String TAG = "TreeViewAdapter" ;

	private LayoutInflater mInflater;
	private IphoneTreeView iphoneTreeView;
	private HashMap<Integer, Integer> groupStatusMap;

	private Context context;
	private Fragment fragment;

	private List<DataItem> datas = new ArrayList<>();
	private List<GroupBean> groups = new ArrayList<>();

	public TreeViewAdapter(
			Context context,
			Fragment fragment,
			IphoneTreeView iphoneTreeView,
			GetFriendBean getFriendBean,
			List<GroupBean> groups
	){
		mInflater= LayoutInflater.from(context);
		this. iphoneTreeView=iphoneTreeView;
		groupStatusMap = new HashMap<Integer, Integer>();

		if (getFriendBean.getFriends()!=null && getFriendBean.getCategorys()!=null){
			for (int i=0 ; i<getFriendBean.getCategorys().size();i++){

				DataItem data = new DataItem(getFriendBean.getCategorys().get(i),new ArrayList<>());
				for (int j=0;j<getFriendBean.getFriends().size();j++){
					if (getFriendBean.getFriends().get(j).getCategoryId() == getFriendBean.getCategorys().get(i).getId()){
						data.friends.add(getFriendBean.getFriends().get(j));
					}

				}
				datas.add(data);
			}
		}else {
			getFriendBean.setCategorys(new ArrayList<>());
			getFriendBean.setFriends(new ArrayList<>());
		}
		if (groups!=null &&groups.size()>0){
			this.groups = groups;
		}else {
			this.groups = new ArrayList<>();
		}
		this.fragment = fragment;
	}

	public void update(FriendsBean friendsBean){
		for (DataItem item :datas){
			if (item.categorysBean.getId() == friendsBean.getCategoryId()){
				item.friends.add(friendsBean);
				break;
			}
		}
		notifyDataSetChanged();
	}

	public void update(GroupBean groupBean){
		boolean flag = false;
		for (int i=0;i<groups.size();i++){
			GroupBean item = groups.get(i);
			if (item.getId() == groupBean.getId()){
				groups.remove(item);
				groups.add(i,groupBean);
				flag = true;
				break;
			}
		}
		if (!flag){
			groups.add(groupBean);
		}
		notifyDataSetChanged();
	}

	public void update(List<GroupBean> groups){
		if (groups!=null){
			this.groups = groups;
			notifyDataSetChanged();
		}
	}

	public int getChildType(int groupPosition){
		if (groupPosition<datas.size()) return Msg.MSG_FROM_FRIEND;
		if (groupPosition == datas.size()) return Msg.MSG_FROM_GROUP;
		return Msg.MSG_FROM_FRIEND;
	}

	public GroupBean getChatGroup(int childPosition){
		return groups.get(childPosition);
	}

	public FriendsBean getChild(int groupPosition, int childPosition) {
		return datas.get(groupPosition).friends.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		if (groupPosition<datas.size()){
			return datas.get(groupPosition).friends.get(childPosition).getId();
		}else {
			return 0;
		}
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition<datas.size()){
			if (groupPosition<0) return 0;
			if (datas.get(groupPosition) == null || datas.get(groupPosition).friends == null ){
				return 0;
			}
			return datas.get(groupPosition).friends.size();
		}else if (groupPosition == datas.size()){
			return groups.size();
		}else {
			return 0;
		}
	}

	public CategorysBean getGroup(int groupPosition) {
		if (groupPosition<datas.size()) return datas.get(groupPosition).categorysBean;
		return null;
	}

	public int getGroupCount() {
		return datas.size()+1;
	}

	public long getGroupId(int groupPosition) {
		Log.i(TAG, "getGroupId: "+groupPosition);
		if (groupPosition<datas.size()){
			return datas.get(groupPosition).categorysBean.getId();
		}else {
			return 0;
		}
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.iphonetreeview_list_item_view, null);
		}
		TextView tv = convertView.findViewById(R.id.contact_list_item_name);
		TextView state = convertView.findViewById(R.id.cpntact_list_item_state);
		TextView label = convertView.findViewById(R.id.label);
		ImageView icon = convertView.findViewById(R.id.icon);
		label.setVisibility(View.VISIBLE);

		if (groupPosition<datas.size()){
			label.setText("VIP");
			tv.setText(getChild(groupPosition, childPosition).getFriend().getUserName());
			state.setText(getChild(groupPosition, childPosition).getFriend().getSign());
			RequestOptions options = new RequestOptions()
//				.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
	                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//                .transform(new GlideRoundTransform(context, 8))
					.optionalCircleCrop()
					.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
			if(fragment!=null){
				Glide.with(fragment)
						.load(getChild(groupPosition, childPosition).getFriend().getIcon())
						.apply(options)
						.into(icon);
			}else {
				Log.i(TAG, "getChildView: 加载头像失败");
			}
		}else {
			label.setText(""+groups.get(childPosition).getMaxCount()+"人");
			tv.setText(groups.get(childPosition).getName());
			state.setText(groups.get(childPosition).getSign());
			RequestOptions options = new RequestOptions()
					.error(R.mipmap.logo)//加载错误之后的错误图
					.override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
					.circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//                .transform(new GlideRoundTransform(context, 8))
					.optionalCircleCrop()
					.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
			if(fragment!=null){
				Glide.with(fragment)
						.load(groups.get(childPosition).getIcon())
						.apply(options)
						.into(icon);
			}else {
				Log.i(TAG, "getChildView: 加载头像失败");
			}
		}
		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.iphonetreeview_list_group_view, null);
		}
		TextView groupName = convertView.findViewById(R.id.group_name);
		ImageView indicator = convertView.findViewById(R.id.group_indicator);
		TextView onlineNum = convertView.findViewById(R.id.online_count);
		if (isExpanded) {
			indicator.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
		} else {
			indicator.setImageResource(R.drawable.ic_chevron_right_black_24dp);
		}
		if (groupPosition<datas.size()){
			groupName.setText(getGroup(groupPosition).getName());
			onlineNum.setText(""+getChildrenCount(groupPosition));
		}else if (groupPosition == datas.size()){
			groupName.setText("群聊");
			onlineNum.setText(""+groups.size());
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
	public void configureTreeHeader(View header, int groupPosition, int childPosition, int alpha) {
		if (groupPosition<datas.size()){
			((TextView) header.findViewById(R.id.group_name)).setText(getGroup(groupPosition).getName());
			((TextView) header.findViewById(R.id.online_count)).setText(""+getChildrenCount(groupPosition));
		}

	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
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

	private class DataItem{
		CategorysBean categorysBean;
		List<FriendsBean> friends;

		public DataItem(CategorysBean categorysBean, List<FriendsBean> friends) {
			this.categorysBean = categorysBean;
			this.friends = friends;
		}
	}
}