package com.jokor.base.pages.main.main_page.chat.group_chat_page.chat_page;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.jokor.base.R;
import com.jokor.base.model.stores_bean.MSession;
import com.jokor.base.util.audio.AudioRecoder;
import com.jokor.base.wedgit.BarChartView;

public class GroupChatFragment extends Fragment {

	private static final String TAG = "GroupChatFragment";
	
	private static final String STR_SESSION = "session";
	
	private MSession session = null;
	
	private View contentView;
	private ImageButton chat_audio;
	private BarChartView audio_bar;
	private ImageButton chat_emoji;
	EditText chat_edit;
	GridView emoji_grid;
	
	ListView chat_list;
	
	AudioRecoder audioRecoder;
	
	int statusBarHeight;
	//监听手势
	GestureDetector mDetector;
	
	public GroupChatFragment() {
		// Required empty public constructor
	}
	
	public static GroupChatFragment newInstance(String strSession, String param2) {
		GroupChatFragment fragment = new GroupChatFragment();
		Bundle args = new Bundle();
		args.putString(STR_SESSION, strSession);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			String strSession = getArguments().getString(STR_SESSION);
			Log.i(TAG, "strSession: "+strSession);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		contentView = inflater.inflate(R.layout.fragment_chat_group, container, false);
		return contentView;
	}
	
}
