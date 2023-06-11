package com.zrt.kotlinapp.activity_view.material_design


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.bean.Fruit
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_navigation_view.*
import kotlinx.android.synthetic.main.activity_navigation_view.drawer_layout
import kotlinx.android.synthetic.main.toolbar_title.*


/**
 * DrawerLayout的滑动菜单，一般与滑动功能连用
 * // 添加material库引用
 * implementation 'com.google.android.material:material:1.0.0'
 * // 添加开源项目CircleImageView的引用 轻松实现图片圆形化的功能
 * implementation 'de.hdodenhof:circleimageview:3.0.1'
 *
 * 使用NavigationView后：
 * 需要把Theme.AppCompat.Light.NoActionBar替换为Theme.MaterialComponents.Light.NoActionBar
 * 否则后续使用的一些控件会报错
 */
class NavigationViewActivity : BasicActivity() {
    val fruits = mutableListOf(Fruit("Apple", R.mipmap.apple), Fruit("Banana", R.mipmap.banana),
            Fruit("Orange", R.mipmap.orange), Fruit("Watermelon", R.mipmap.watermelon),
            Fruit("Pear", R.mipmap.pear), Fruit("Grape", R.mipmap.grape),
            Fruit("Pineapple", R.mipmap.pineapple), Fruit("Strawberry", R.mipmap.strawberry),
            Fruit("Cherry", R.mipmap.cherry), Fruit("Mango", R.mipmap.mango))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            // 设置导航按钮图标
            it.setHomeAsUpIndicator(R.mipmap.ic_menu)
        }
        // 设置默认选中项
        navView.setCheckedItem(R.id.navCall)
        // 设置菜单子项监听器
        navView.setNavigationItemSelectedListener {
            // 关闭菜单滑动
            drawer_layout.closeDrawers()
            true
        }
    }

    override fun getLayoutResID(): Int = R.layout.activity_navigation_view

    override fun initData() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // GravityCompat.START 保证与设置的第二个布局行为一致
            // 打开左侧滑动布局
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
            R.id.back_up -> ToastUtils.show(this, "You clicked BackUp")
            R.id.delete -> ToastUtils.show(this, "You clicked Delete")
//            R.id.settings -> ToastUtils.show("You clicl Settings")
            // 打开右侧滑动布局
            R.id.settings -> drawer_layout.openDrawer(GravityCompat.END)
        }
        return true
    }
}