package com.zrt.kotlinapp.activity_view.custom_view_basic.view_basic

/**
 * @author：Zrt
 * @date: 2023/3/21
 * 自定义 view 绘图进阶
 *
 * 一、实现阴影效果
 * @see com.zrt.kotlinapp.activity_view.custom.paint_basic.PaintShadowLayerView
 *    Paint.setShadowLayer(float radius, float dx, float dy, @ColorInt int shadowColor): 设置阴影效果
 *        float radius ： 模糊半径，值越大越模糊，值越小越清晰，等于 0 则阴影小时
 *        float dx, float dy ： 阴影的偏移距离，正值向右和向下，负值向左和向上
 *        @ColorInt int shadowColor：绘制阴影的画笔颜色，即阴影的颜色（对图片阴影无需）
 *        文字和绘制的图形阴影会使用定义的阴影画笔颜色绘制。图片的阴影则是产生一张相同的图片，对图片边缘进行模糊
 *    Paint.clearShadowLayer() 清除阴影效果
 *    注：阴影效果不支持硬件加速
 * 二、文字添加阴影效果
 *   android:shadowRadius="2"
 *   android:shadowDx="10"
 *   android:shadowDy="10"
 *   android:shadowColor="@color/red"
 */