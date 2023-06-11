package com.zrt.mybase.activity.viewpagers;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author：Zrt
 * @date: 2022/3/2
 */
public class VPBasicAdapter<T extends View> extends PagerAdapter {
    private List<T> mViewList;
    public VPBasicAdapter(List<T> mViewList){
        this.mViewList = mViewList;
    }

    /**
     * 必须实现
     * 当前页面个数
     * @return
     */
    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }

    /**
     * 必须实现
     * 判断view是否来自于Object
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 选择实现
     * 设置ViewPager的标题
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    /**
     * 必须实现
     * 初始化ViewPager的页对象
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    /**
     * 必须实现
     * 销毁已划出屏幕的也对象
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(mViewList.get(position));
    }

}
