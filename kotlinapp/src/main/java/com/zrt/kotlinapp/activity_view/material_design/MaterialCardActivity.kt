package com.zrt.kotlinapp.activity_view.material_design

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import kotlinx.android.synthetic.main.activity_material_card.*
import java.util.ArrayList

/**
 *  卡片布局 和 AppBarLayout
 *  MaterialCardView:也是一个FrameLayout，提供了额外的圆角和阴影效果
 *  <com.google.android.material.card.MaterialCardView
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      xmlns:app="http://schemas.android.com/apk/res-auto"
 *      android:layout_margin="5dp"
 *      app:cardCornerRadius="4dp">
 *  </com.google.android.material.card.MaterialCardView>
 *  app:cardCornerRadius="4dp"：指定卡片圆角弧度
 *  android:elevation="2dp":指定卡片高度，高度值越大，投影范围越大，但是投影效果越淡
 *  app:cardElevation="4dp": 阴影半径
 *  app:cardBackgroundColor="@color/":设置背景色
 *
 * 由于使用CoordinatorLayout布局包裹Toolbar和RecycleView,会导致RecycleView把Toolbar给覆盖，
 * 原因CoordinatorLayout为FrameLayout，无法明确空间位置，因此使用如下方法：
 *      1、使用 AppBarLayout包裹Toolbar
 *      2、RecycleView指定一个布局行为
 *  例：
 *  <com.google.android.material.appbar.AppBarLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content">
 *      <androidx.appcompat.widget.Toolbar
 *          app:layout_scrollFlags="scroll|enterAlways|snap"/>
 *  </com.google.android.material.appbar.AppBarLayout>
 *  <androidx.recyclerview.widget.RecyclerView
 *      app:layout_behavior="@string/appbar_scrolling_view_behavior"/>
 *
 *  AppbarLayout的滚动事件：
 *  <com.google.android.material.appbar.AppBarLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content">
 *      <androidx.appcompat.widget.Toolbar
 *          app:layout_scrollFlags="scroll|enterAlways|snap"/>
 *  </com.google.android.material.appbar.AppBarLayout>
 *  通过添加app:layout_scrollFlags="scroll|enterAlways|snap"实现
 *  scroll：RecycleView向上滑动时，toolbar会一起向上滚动并隐藏
 *  enterAlways：表示向下滚动时，toolbar会一起向上滚动并重新显示
 *  snap：表示toolbar还没有完全隐藏或显示的时候，会根据滑动的距离，自动选择隐藏或显示
 *
 *
 *  error ：
 *  1、MarterialCardView使用报错： Error inflating class com.google.android.material.card.MaterialCardView
 *  原因：
 *  Material库版本的问题，升级到1.1.0之后，在使用MarterialCardView控件的时候，
 *  需要加上一个属性：android:theme="@style/Theme.MaterialComponents"，
 *  表示用的是Material主题的，使用在默认情况下，都是使用AppCompat这个主题的。
 */
class MaterialCardActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

    override fun getLayoutResID(): Int = R.layout.activity_material_card

    override fun initData() {
        initFruits()
        val layoutManager = GridLayoutManager(this,2)
        a_mc_recycle.layoutManager = layoutManager
        val adapter = MCFruitRVAdapter(this, mListData)
        a_mc_recycle.adapter = adapter
        a_mc_start_card_1.setOnClickListener {
            startActivity(Intent(this, MCardTwoActivity::class.java))
        }
    }
    fun initFruits(){
        // repeat：把Lambda直线n遍
        repeat(3){
            mListData.add(Fruit("Apple=$it", R.mipmap.apple_pic))
            mListData.add(Fruit("Banana=$it", R.mipmap.banana_pic))
            mListData.add(Fruit("Orange=$it", R.mipmap.orange_pic))
            mListData.add(Fruit("Watermelon=$it", R.mipmap.watermelon_pic))
            mListData.add(Fruit("Pear=$it", R.mipmap.pear_pic))
            mListData.add(Fruit("Grape=$it", R.mipmap.grape_pic))
            mListData.add(Fruit("Pineapple=$it", R.mipmap.pineapple_pic))
            mListData.add(Fruit("Strawberry=$it", R.mipmap.strawberry_pic))
            mListData.add(Fruit("Cherry=$it", R.mipmap.cherry_pic))
            mListData.add(Fruit("Mango=$it", R.mipmap.mango_pic))
        }
    }
}