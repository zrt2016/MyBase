package com.zrt.mybase.activity.viewpagers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.zrt.mybase.utils.MyLogger;

/**
 * @author：Zrt
 * @date: 2022/3/3
 *
 * 实现翻页动画的关键就是重写transformPage方法，方法里有两个参数view和position,理解这两个参数非常重要。
 * 假设有三个页面view1，view2，view3从左至右在viewPager中显示
 *
 * 往左滑动时：view1，view2，view3的position都是不断变小的。
 * view1的position: 0 → -1 → 负无穷大
 * view2的position: 1 → 0 → -1
 * view3的position: 1 → 0
 * 往右滑动时：view1，view2，view3的position都是不断变大的。
 * view1的position: -1 → 0
 * view2的position: -1 → 0 → 1
 * view3的position: 0 → 1→ 正无穷大
 * 当position是正负无穷大时view就离开屏幕视野了。因此最核心的控制逻辑是在[-1,0]和(0,1]这两个区间，通过设置透明度，平移，旋转，缩放等动画组合可以实现各式各样的页面变化效果。
 *
 */
public class BasicPagerTransformer implements ViewPager.PageTransformer {
    // 默认ViewPager滑动淡入淡出效果
    int transforType = 0;
    public BasicPagerTransformer(int transforType){
        this.transforType = transforType;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        switch (transforType){
            case 1:
                // 设置ViewPager滑动动画效果，滑动缩小，平移进入
                zoomOutPageTransformer(page, position);
                break;
            case 2:
                zoomOutPageTransformer2(page, position);
                break;
            default:
                // 设置ViewPager滑动动画效果，淡入淡出
                depthPageTransFormer(page, position);
                break;
        }
    }

    private static final float MIN_SCALE = 0.75f;
//    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    /**
     * transforType == 0
     * ViewPager淡入淡出
     * @param page
     * @param position
     */
    public void depthPageTransFormer(@NonNull View page, float position){
        int pageWidth = page.getWidth();
        MyLogger.Log().i("##pageTag="+page.getTag());
        MyLogger.Log().i("##pageWidth="+pageWidth+"##position="+position);
        if (position < -1){// [-Infinity,-1)
            // 这一页在屏幕的左边。 设置透明度为0
            page.setAlpha(0);
        } else if (position <= 0){// [-1, 0]
            //移动到左侧页面时使用默认的幻灯片过渡
            page.setAlpha(1);
            page.setTranslationX(0);
            // 设置缩放保持原图不变
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) { // (0,1]
            // 淡出这一页。
            page.setAlpha(1 - position);

            // 抵消默认的幻灯片过渡
            page.setTranslationX(pageWidth * -position);

            // 向下缩放页面（在MIN_Scale和1之间）
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            MyLogger.Log().i("##scaleFactor="+scaleFactor+"##(1 - Math.abs(position)="+(1 - Math.abs(position)));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else { // (1,+Infinity]
            // 这一页在屏幕的右边。
            page.setAlpha(0);
        }
    }

    /**
     * transforType = 1
     * 点击滑动ViewPager缩小，淡化 平移
     * @param page
     * @param position
     */
    public void zoomOutPageTransformer(@NonNull View page, float position){
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
        if (position < -1){
            page.setAlpha(0);
//        }else if (position <= 0){
        }else if (position <= 1){

            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            // scaleFactor越大  margin越小
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }else{
            page.setAlpha(0);
        }
    }

    public void zoomOutPageTransformer2(@NonNull View page, float position){
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
        if (position < -1){
            page.setAlpha(0);
        }else if (position <= 0){
            // 0 → -1 缩小   -1 → 0 放大
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            if (0 == ((int)page.getTag())) {
                MyLogger.Log().i("##scaleFactor="+scaleFactor);
            }
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            // // 0 → -1 淡化   -1 → 0 加深
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) 获取缩小百分比
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA) 获取淡化程度
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }else if (position <= 1){
            // 1 → 0 放大   0 → 1 缩小
            float scaleFactor = MIN_SCALE + (1 - Math.max(MIN_SCALE, Math.abs(position)));
//            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//            if (position < 0) {
//                page.setTranslationX(horzMargin - vertMargin / 2);
//            } else {
//                page.setTranslationX(-horzMargin + vertMargin / 2);
//            }
            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            // 1 → 0 加深   0 → 1 淡化
            // Fade the page relative to its size.
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }else{
            page.setAlpha(0);
        }
    }
}
