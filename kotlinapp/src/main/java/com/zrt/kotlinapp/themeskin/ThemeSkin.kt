package com.zrt.kotlinapp.themeskin

import android.content.Context
import android.content.res.Configuration

/**
 * @author：Zrt
 * @date: 2022/8/14
 * 主题皮肤适配
 * Android10.0后添加 了Force Dack,他是一种能让程序快速适配深色主题并且不用额外编写代码的方式
 * 工作原理是系统会分析浅色主题应用下的 每一层view，并且绘制到屏幕前，自动将他们的颜色转换为更适合深色主题的颜色
 * 新创建一个value-29， 然后在创建一个styles文件
 * <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
 *      ..............此处省略......................
 *      <item name="android:forceDarkAllowed">true</item>
 *  </style>
 *  给AppTheme主题添加forceDarkAllowed属性，该属性是Android10.0后才有的
 *  表明允许系统使用Force Dark将应用强制转换成深色主题
 *
 *  浅色主题：Theme.AppCompat.Light.NoActionBar
 *  深色主题：Theme.AppCompat.NoActionBar
 *
 *  Theme.AppCompat.DatNight.NoActionBar根据系统设置选择显示的主题是深色还是浅色
 *  使用该主题删除value-29，修改values中的styles的AppTheme为Theme.AppCompat.DatNight.NoActionBar
 *  新建一个values-night目录，然后点击values-night目录新建一个colors文件，在文件中指定深色主题下的颜色值。
 *      <color name="colorPrimary">#303030</color>
        <color name="colorPrimaryDark">#232323</color>
        <color name="colorAccent">#008577</color>
 *  普通情况下会读取values/colors文件中的颜色，开起深色主题后系统会读取 values-night/colors文件中的颜色值
 *
 *  为了减少硬编码的方式指定控件的颜色，例如黑色主题适配白色文字，白色背景显示黑色文字
 *  可以在布局添加android:background="?android:attr/colorBackground"
 *  view中添加android:textColor="?android:attr/textColorPrimary"
 */
/**
 * 获取当前主题是深色主题还是浅色主题
 */
fun isDarkTheme(context:Context):Boolean{
    val flag = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}
fun printFruits() {
    val list: MutableList<String> = ArrayList()
    list.add("APP")
}