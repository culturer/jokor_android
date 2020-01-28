package vip.jokor.im.pages.main.main_page.square;

import android.os.Bundle;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.square.actives.ActiveFragment;
import vip.jokor.im.pages.main.main_page.square.circle.CircleFragment;
import vip.jokor.im.pages.main.main_page.square.focus.FocusFragment;
import vip.jokor.im.pages.main.main_page.square.recommend.RecommendFragment;
import vip.jokor.im.util.base.SizeUtil;

import java.util.ArrayList;
import java.util.List;


public class SquareFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View contentView;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public SquareFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_square, container, false);
        contentView.setPadding(0, SizeUtil.getStatusBarHeight(getContext()),0,0);
        initPager();
        return contentView;
    }

    private void initData(){
        fragments.add(new FocusFragment());
        fragments.add(new CircleFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new ActiveFragment());
        titles.add("关注");
        titles.add("圈子");
        titles.add("推荐");
        titles.add("视频");
    }

    private void initPager(){
        TabLayout tabLayout = contentView.findViewById(R.id.tab);
        ViewPager viewPager = contentView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        View view = getLayoutInflater().inflate(R.layout.tab_item,null);
        TextView tab_item_textview = view.findViewById(R.id.tab_item_textview);
        tab_item_textview.setTextColor(getContext().getColor(R.color.red));
        tab_item_textview.setTextSize(19);
        tab_item_textview.setGravity(Gravity.CENTER);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_item_textview.setText(tab.getText());
                viewPager.setCurrentItem(tab.getPosition());
                tab.setCustomView(tab_item_textview);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_item_textview.setText(titles.get(0));
        tabLayout.getTabAt(0).setCustomView(tab_item_textview);
        tabLayout.getTabAt(0).select();
    }

}
