package com.zrt.kotlinapp.activity_view.material_design.utils

import android.os.Build
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @author：Zrt
 * @date: 2022/9/8
 */
class VPTransformer(var transforType:Int = 0) : ViewPager2.PageTransformer{
    // 默认ViewPager滑动淡入淡出效果
//    var transforType = 0
//    fun BasicPagerTransformer(transforType: Int) {
//        this.transforType = transforType
//    }
    companion object{
        val TYPE_DEPTH = 0
        val TYPE_ZOOM = 1
        val TYPE_ZOOM2 = 2
        val TYPE_SCALE = 3
    }
    val MIN_SCALE = 0.75f
    val MIN_ALPHA = 0.5f
    val DEFAULT_CENTER = 0.5f

    override fun transformPage(page: View, position: Float) {
        when (transforType) {
            // 设置ViewPager滑动动画效果，滑动缩小，平移进入
            TYPE_ZOOM -> zoomOutPageTransformer(page, position)
            // 淡入淡出加滑动效果
            TYPE_ZOOM2 -> zoomOutPageTransformer2(page, position)
            // 缩放效果
            TYPE_SCALE -> scaleInTransformer(page, position)
            // 设置ViewPager滑动动画效果，淡入淡出
            else -> depthPageTransFormer(page, position)
        }
    }

    /**
     * transforType == 0
     * ViewPager淡入淡出
     * @param page
     * @param position
     */
    private fun depthPageTransFormer(page: View, position: Float) {
        val width = page.width
        if (position < -1){// [-Infinity,-1)
            // 这一页在屏幕的左边。 设置透明度为0
            page.alpha = 0f
        }else if(position <= 0){// [-1, 0]
            //移动到左侧页面时使用默认的幻灯片过渡
            page.alpha = 1f
            page.translationX = 0f
            // 设置缩放保持原图不变
            page.scaleX = 1f
            page.scaleY = 1f
        }else if(position <= 1){// (0,1]
            // 淡出这一页。
            page.alpha = 1 - position
            // 抵消默认的幻灯片过渡
            page.translationX = width * -position
            // 向下缩放页面（在MIN_Scale和1之间）
            val scaleFactor: Float = (MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position)))
            Log.i("", "##scaleFactor=" + scaleFactor + "##(1 - Math.abs(position)=" + (1 - Math.abs(position)))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }else { // (1,+Infinity]
            // 这一页在屏幕的右边。
            page.alpha = 0f
        }

    }
    /**
     * transforType = 1
     * 点击滑动ViewPager缩小，淡化 平移
     * @param page
     * @param position
     */
    private fun zoomOutPageTransformer(page: View, position: Float) {

        val pageWidth = page.width
        val pageHeight = page.height
        Log.i("","##pageWidth=$pageWidth##position=$position")
        if (position < -1) {// [-Infinity,-1)
            page.alpha = 0f
        }else if (position <=1){
            // 随着position的绝对值越来越小，缩放值也越来越小，即page越来越大
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            // scaleFactor越来越大，则margin越来越小
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                page.translationX = horzMargin - vertMargin / 2
            } else {
                page.translationX = -horzMargin + vertMargin / 2
            }
            // Scale the page down (between MIN_SCALE and 1)
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            // Fade the page relative to its size.
             // 0 → -1 淡化   -1 → 0 加深
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) 获取缩小百分比
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA) 获取淡化程度
            page.alpha = (MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA))
        }else{
            page.alpha = 0f
        }
    }

    /**
     * 缩小
     */
    private fun zoomOutPageTransformer2(page: View, position: Float) {
        val width = page.width
        val height = page.height
        if (position < -1) {// [-Infinity,-1)
            page.alpha = 0f
        }else if (position <=0){
            // 1 → 0 缩小   0 → 1 放大
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            // // 0 → -1 淡化   -1 → 0 加深
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) 获取缩小百分比
//            (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA) 获取淡化程度
            page.alpha = MIN_ALPHA + (scaleFactor-MIN_SCALE) / (1-MIN_SCALE) * (1-MIN_ALPHA)
        }else if (position <=1){
            // 1 → 0 放大   0 → 1 缩小
            // 当前显示page，从左到右，滑动到最后时缩小
            val scaleFactor: Float = MIN_SCALE + (1 - Math.max(MIN_SCALE, Math.abs(position)))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.alpha = MIN_ALPHA + (scaleFactor-MIN_SCALE) / (1-MIN_SCALE) * (1-MIN_ALPHA)
        }else{
            page.alpha = 0f
        }

    }

    private fun scaleInTransformer(page: View, position: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            page.elevation = -abs(position)
        }
        val width = page.width
        val height = page.height

        page.pivotX = width/2f
        page.pivotY = height/2f
        if (position < -1){
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
            page.pivotX = width.toFloat()
        }else if (position < 0) {
            val scaleFactor: Float = (1 + position) * (1 - MIN_SCALE) + MIN_SCALE
            page.setScaleX(scaleFactor)
            page.setScaleY(scaleFactor)
            page.setPivotX(width * (DEFAULT_CENTER + DEFAULT_CENTER * -position))
        }else if (position <= 1){
            val scaleFactor: Float = (1 - position) * (1 - MIN_SCALE) + MIN_SCALE
            page.setScaleX(scaleFactor)
            page.setScaleY(scaleFactor)
            page.setPivotX(width * ((1 - position) * DEFAULT_CENTER))
        }else {
            page.setPivotX(0f);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
    }


}