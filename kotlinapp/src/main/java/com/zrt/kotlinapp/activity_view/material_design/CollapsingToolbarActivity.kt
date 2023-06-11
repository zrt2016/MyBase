package com.zrt.kotlinapp.activity_view.material_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_collapsing_toolbar.*

/**
 * 可折叠式标题栏
 * <androidx.coordinatorlayout.widget.CoordinatorLayout
 *      android:fitsSystemWindows="true">
 *      <com.google.android.material.appbar.AppBarLayout
 *          android:layout_height="250dp">
 *          <com.google.android.material.appbar.CollapsingToolbarLayout
 *              android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
 *              app:contentScrim="@color/colorPrimary"
 *              app:layout_scrollFlags="scroll|exitUntilCollapsed">
 *              <ImageView
 *                  app:layout_collapseMode="parallax"/>
 *              <androidx.appcompat.widget.Toolbar
 *                  app:layout_collapseMode="pin"/>
 *          </com.google.android.material.appbar.CollapsingToolbarLayout>
 *      </com.google.android.material.appbar.AppBarLayout>
 *      <androidx.core.widget.NestedScrollView
 *          app:layout_behavior="@string/appbar_scrolling_view_behavior">
 *          <LinearLayout android:orientation="vertical">
 *              <com.google.android.material.card.MaterialCardView
 *                  app:cardCornerRadius="4dp">
 *                  <TextView android:layout_margin="10dp" />
 *              </com.google.android.material.card.MaterialCardView>
 *          </LinearLayout>
 *      </androidx.core.widget.NestedScrollView>
 * </androidx.coordinatorlayout.widget.CoordinatorLayout>
 * 一、CollapsingToolbarLayout:不能独立存在，只能作为AppBarLayout的直接子布局使用，
 * 而AppBarLayout又必须是CoordinatorLayout的子布局，因此需要嵌套2层后使用
 * CollapsingToolbarLayout折叠后就是一个普通的Toolbar
 *     1、app:contentScrim：用于指定CollapsingToolbarLayout在趋于折叠状态以及折叠之后的背景色
 *     2、app:layout_scrollFlags：
 *          A、scroll：随着水果内容详情滚动一起滚动
 *          B、exitUntilCollapsed:表示当CollapsingToolbarLayout随着滚动完成折叠之后就保留在界面上，不再移除屏幕之外
 *          C、enterAlways：表示随着滚动折叠后，会消失
 *          D、
 *     3、子布局指定app:layout_collapseMode：
 *          A、pin：表示折叠过程中位置始终保持不变
 *          B、parallax：折叠过程中产生一定的错位偏移
 *     4、app:expandedTitleGravity="left|bottom"
 *          展开式，title的位置
 *     5、app:collapsedTitleGravity="left"
 *          收缩后，title的位置
 * 二、NestedScrollView：与AppBarLayout平级，在scrollView的基础上增加了嵌套响应滚动事件的功能
 * 由于 CoordinatorLayout已经支持响应滚动事件，因此内部需要使用NestedScrollView或RecycleView这样的布局
 *
 *  app:layout_behavior="@string/appbar_scrolling_view_behavior"
 *
 * 三、充分利用系统状态栏空间。
 * 在CoordinatorLayout、AppBarLayout、CollapsingToolbarLayout以及需要显示在状态的的View，此处ImageView需要显示
 * 中添加android:fitsSystemWindows="true"，
 * 表示空间会出现在系统状态栏里.
 * 然后在主题中将<item name="android:statusBarColor">@android:color/transparent</item>设置为透明的。
 * 改配置只支持Android5.0 因为material design的概念是在5.0及之后出现的
 *
 */
class CollapsingToolbarActivity : BasicActivity() {
    companion object {
        const val FRUIT_NAME = "fruit_name"
        const val FRUIT_IMAGE_ID = "fruit_image_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_collapsing_toolbar

    override fun initData() {
        val fruitName = intent.getStringExtra(FRUIT_NAME) ?: ""
        val fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID, 0)
        setSupportActionBar(a_ct_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        a_ct_collapsing.title = fruitName
        Glide.with(this).load(fruitImageId).into(a_ct_image)
        a_ct_ContentText.text = generateFruitContent(fruitName)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateFruitContent(fruitName: String) = fruitName.repeat(500)
}