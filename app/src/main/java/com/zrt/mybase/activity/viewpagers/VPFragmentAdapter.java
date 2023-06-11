package com.zrt.mybase.activity.viewpagers;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author：Zrt
 * @date: 2022/2/28
 * 实现一个最基本的FragmentPagerAdapter
 * 适用于页面较少的情况
 */
public class VPFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public VPFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    /**
     * 必须实现
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    /**
     * 必须实现
     * @return
     */
    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    /**
     * 选择性实现
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getClass().getSimpleName();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

}
