package com.zrt.mybase.activity.viewpagers;

import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends BaseActivity {

    ViewPager viewpager_basic;
    ViewPager viewpager_basic2;
    ViewPager viewpager_basic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView() {
        viewpager_basic = findViewById(R.id.viewpager_basic);
        viewpager_basic2 = findViewById(R.id.viewpager_basic2);
        viewpager_basic3 = findViewById(R.id.viewpager_basic3);
    }

    @Override
    protected void initData() {
        setBasicViewPager(viewpager_basic, 0);
        setBasicViewPager(viewpager_basic2, 1);
        setBasicViewPager(viewpager_basic3, 2);
        /**---------------------------------------分割线--------------------------------- */
    }

    public void setBasicViewPager(ViewPager viewpager, int transformerType){
        List<View> mListBasic = new ArrayList<>();
        /**
         * 一个普通的ViewPager
         */
        for (int i=0; i< 5; i++){
            View view = getLayoutInflater().inflate(R.layout.view_pager_basic_item, null, false);
            TextView item_title = view.findViewById(R.id.item_title);
            item_title.setText("Title="+i);
            item_title.setBackgroundColor(getResources().getColor(R.color.red_1));
            view.setTag(i);
            mListBasic.add(view);
        }
        // 创建适配器
        VPAdapter vpAdapter = new VPAdapter(mListBasic);
        // 绑定适配器
        viewpager.setAdapter(vpAdapter);
        // 添加页面滑动监听器 ，setOnPageChangeListener
        // 推荐使用addOnPageChangeListener。 或者使用setOnPageChangeListener（注：该方法已过时）
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设置ViewPager滑动效果
        viewpager.setPageTransformer(true, new BasicPagerTransformer(transformerType));
        /**
         * 这个方法是用来控制fragment不重新走生命周期的个数的，打个比方一共4个fragment页面，
         * 如果mViewPager.setOffscreenPageLimit(3)，那么所有的fragment都只走一次生命周期，
         * 如果是mViewPager.setOffscreenPageLimit(2)，那么其中有一个fragment会在切换的时候重新走一遍生命周期
         * ，FragmentStatePagerAdapter和FragmentPagerAdapter都是这样，
         * 但是FragmentPagerAdapter设置setOffscreenPageLimit不影响fragment缓存的个数,
         * 而FragmentStatePagerAdapter缓存的fragment实例个数就是setOffscreenPageLimit设置的值+1。
         * 另外setOffscreenPageLimit的缺省值是1，设置0是无效的会被强制赋值成1。
         */
//        viewpager_basic.setOffscreenPageLimit(4);
    }
}