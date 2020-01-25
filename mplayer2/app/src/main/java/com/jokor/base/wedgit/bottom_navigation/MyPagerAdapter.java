package com.jokor.base.wedgit.bottom_navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
	
	private final List<Fragment> mFragmentList = new ArrayList<>();
	private FragmentManager manager;

	public MyPagerAdapter(@NonNull FragmentManager fm, int behavior, FragmentManager manager) {
		super(fm, behavior);
		this.manager = manager;
	}

	public MyPagerAdapter(FragmentManager manager) {
		super(manager);
		this.manager=manager;
	}
	
	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}
	
	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	
	public void addFragment(Fragment fragment) {
		mFragmentList.add(fragment);
	}

}
