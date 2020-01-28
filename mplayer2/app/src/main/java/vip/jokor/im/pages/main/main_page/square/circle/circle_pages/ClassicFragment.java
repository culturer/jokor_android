package vip.jokor.im.pages.main.main_page.square.circle.circle_pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;

import java.util.ArrayList;
import java.util.List;

public class ClassicFragment extends Fragment {

    private static final String STR_DATA = "str_data";
    private static final String ARG_PARAM2 = "param2";

    private String strData;
    private String mParam2;

    List<FriendsArticlesBean.ArticlesBean> datas = new ArrayList<>();

    private CircleData circleData;

    private View contentView;
    private RecyclerView listView;

    public ClassicFragment() {
        // Required empty public constructor
    }

    public static ClassicFragment newInstance(String param1, String param2){
        ClassicFragment fragment = new ClassicFragment();
        Bundle args = new Bundle();
        args.putString(STR_DATA, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strData = getArguments().getString(STR_DATA);
            mParam2 = getArguments().getString(ARG_PARAM2);
            circleData = GsonUtil.getGson().fromJson(strData, CircleData.class);
        }
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_classic, container, false);
        listView = contentView.findViewById(R.id.listview);

        ClassicAdapter classicAdapter = new ClassicAdapter(R.layout.focus_item,datas);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //防止Item切换
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);
        RecyclerViewDivider
                .with(getContext())
                .size(SizeUtil.dip2px(getContext(),2))
                .asSpace()
                .build()
                .addTo(listView);

        listView.setAdapter(classicAdapter);
        return contentView;
    }

    private void initData(){
        for (int i=0 ;i<20;i++){
            datas.add(new FriendsArticlesBean.ArticlesBean());
        }
    }

}
