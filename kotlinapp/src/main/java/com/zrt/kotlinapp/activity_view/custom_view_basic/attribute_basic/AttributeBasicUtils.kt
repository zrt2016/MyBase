package com.zrt.kotlinapp.activity_view.custom_view_basic.attribute_basic

import android.content.Context
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/5/16
 * 控件封装：
 * 一、自定义属性标签：declare-styleable
 *   attrs.xml该文件下的 name 属性名不能有重复，否则会编译报错
 *      1. reference：参考某一资源ID。例：android:background = "@drawable/图片ID" @color/xxx等
 *      2. color：颜色值。例：android:textColor = "#00FF00"
 *      3. boolean：布尔值。例：android:focusable = "true"
 *      4. dimension：尺寸值。例：android:layout_height = "42dip"
 *      5. float：浮点值。例：android:toAlpha = "0.7"
 *      6. integer：整型值。例：android:frameDuration = "100"
 *      7. string：字符串。例：android:apiKey = "0jOkQ"
 *      8. fraction：百分数。例： android:pivotX = "200%"
 *      9. enum：枚举值。例： android:orientation = "vertical"
 *          <declare-styleable name="名称">
 *              <attr name="orientation">
 *                  <enum name="horizontal" value="0" />
 *                  <enum name="vertical" value="1" />
 *              </attr>
 *          </declare-styleable>
 *      10. flag：位或运算。例： android:windowSoftInputMode = "stateUnspecified | stateUnchanged"
 *      自己定义，类似于android:gravity="top"
 *          <attr name="age">
 *              <flag name="child" value="10"/>
 *              <flag name="young" value="18"/>
 *              <flag name="old" value="60"/>
 *          </attr>
 *      属性定义：支持多种类型定义，例：<attr name = "background" format = "reference|color" /> android:background = "@drawable/图片ID|#00FF00"
 * 二、自定义属性集导入：
 *      在根布局添加：
 *          方式一：xmlns:app="http://schemas.android.com/apk/res/com.harvic.com.trydeclarestyle"
 *          方式二：xmlns:app="http://schemas.android.com/apk/res-auto"
 *      xmlns:app:这里的app是自定义的，但是在定义xml控件属性时，也要通过该标识符访问
 *          例：app:header=""
 * 三、代码中获取自定义属性的值：
 *      通过 TypedArray 获取某个属性值的，使用完后必须调用TypedArray类的 recycle()函数释放资源
 *          getInt(@StyleableRes int index, int defValue)
 *          getDimension(@StyleableRes int index, float defValue) ：尺寸值
 *          getBoolean(@StyleableRes int index, boolean defValue)
 *          getColor(@StyleableRes int index, @ColorInt int defValue)：获取颜色值
 *          getString(@StyleableRes int index)
 *          getDrawable(@StyleableRes int index)
 *          getResources()
 *          参数一：@StyleableRes int index，通过获取attrs资源文件中attr属性的name 。
 *              例：R.styleable.PaintTextBasicView_textPWith
 *          参数二：float defValue则为默认值
 */

fun getTypeArray() {
    val context:Context? = null
    val typeArray = context?.obtainStyledAttributes(0, R.styleable.ShapeView)

}