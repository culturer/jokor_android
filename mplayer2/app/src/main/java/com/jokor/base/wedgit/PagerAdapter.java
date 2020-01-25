package com.jokor.base.wedgit;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class PagerAdapter extends FragmentPagerAdapter {
    //标签
    List<String> pagerList = new ArrayList<>();
    //内容
    List<Fragment> fragmentList= new ArrayList<>();

    public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> pagerList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pagerList = pagerList;
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerList.get(position);
    }
}
