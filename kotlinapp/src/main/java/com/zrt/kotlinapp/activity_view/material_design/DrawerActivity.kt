package com.zrt.kotlinapp.activity_view.material_design

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.material_design.demo.DrawerToolbarActivity
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_drawer.*

/**
 * <TextView
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * android:gravity="start"
 * android:background="#FFF"
 * android:text="this is menu"
 * android:textSize="30sp"/>
 *
 * android:gravity="start":告诉DrawerLayout滑动菜单是在屏幕左边还是右边
 * left：标识滑动菜单在左边
 * right：标识滑动菜单在右边
 * start：根据系统语言判断，例如英语、汉语 滑动菜单就在左边
 */
class DrawerActivity : BasicActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(a_d_toolbar)
        supportActionBar?.let {
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            // 设置导航按钮图标
            it.setHomeAsUpIndicator(R.mipmap.ic_menu)
        }
    }

    override fun getLayoutResID(): Int = R.layout.activity_drawer

    override fun initData() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // GravityCompat.START 保证与设置的第二个布局行为一致
            android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
            R.id.back_up -> ToastUtils.show(this, "You clicked BackUp")
            R.id.delete -> ToastUtils.show(this, "You clicked Delete")
            R.id.settings -> ToastUtils.show("You clicl Settings")
        }
        return true
    }
}