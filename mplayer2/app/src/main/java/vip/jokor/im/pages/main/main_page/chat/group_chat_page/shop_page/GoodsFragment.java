package vip.jokor.im.pages.main.main_page.chat.group_chat_page.shop_page;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import vip.jokor.im.R;


public class GoodsFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	private View contentView;
	
	public GoodsFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment GoodsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static GoodsFragment newInstance(String param1, String param2) {
		GoodsFragment fragment = new GoodsFragment();
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
		contentView = inflater.inflate(R.layout.fragment_goods, container, false);
		initList();
		return contentView;
	}
	
	private void initList(){
		ListView goods_list = contentView.findViewById(R.id.goods_list);
		GoodsAdapter adapter = new GoodsAdapter();
		goods_list.setAdapter(adapter);
		goods_list.setVerticalScrollBarEnabled(false);
	}
	
	private class GoodsAdapter extends BaseAdapter{
		
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
			View contentView = LayoutInflater.from(getContext()).inflate(R.layout.goods_item,null);
			contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(),GoodsActivity.class);
					startActivity(intent);
				}
			});
			return contentView;
		}
	}
}
