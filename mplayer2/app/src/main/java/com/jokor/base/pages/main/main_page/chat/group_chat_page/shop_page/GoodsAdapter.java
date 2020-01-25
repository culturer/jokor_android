package com.jokor.base.pages.main.main_page.chat.group_chat_page.shop_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jokor.base.R;

public class GoodsAdapter extends BaseAdapter {
	
	private Context context;
	
	public GoodsAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return 10;
	}
	
	@Override
	public Object getItem(int i) {
		return new Object();
	}
	
	@Override
	public long getItemId(int i) {
		return 0;
	}
	
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View contentView = LayoutInflater.from(context).inflate(R.layout.goods_item,null);
		TextView user = contentView.findViewById(R.id.user);
		user.setVisibility(View.VISIBLE);
		user.setText("用户"+i);
		return contentView;
	}
}
