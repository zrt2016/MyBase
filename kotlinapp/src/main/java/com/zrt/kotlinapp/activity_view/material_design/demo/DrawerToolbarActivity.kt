package com.zrt.kotlinapp.activity_view.material_design.demo

import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.palette.graphics.Palette
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.mytoolbar.*

class DrawerToolbarActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_drawer_toolbar

    override fun initData() {
        setSupportActionBar(my_toolbar)
        supportActionBar?.let {
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            // 设置导航按钮图标
            it.setHomeAsUpIndicator(R.mipmap.ic_menu)
        }
        my_toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    // GravityCompat.START 保证与设置的第二个布局行为一致
                    android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
                    R.id.back_up -> ToastUtils.show( "You clicked BackUp")
                    R.id.delete -> ToastUtils.show(this@DrawerToolbarActivity, "You clicked Delete")
                    R.id.settings -> ToastUtils.show("You clicl Settings")
                }
                return false
            }
        })
//        添加DrawerLayout监听
        var drawerListener = ActionBarDrawerToggle(this, drawer_layout, my_toolbar,
                R.string.drawer_open, R.string.drawer_close)
        drawerListener.syncState()
        drawer_layout.setDrawerListener(drawerListener)
//        my_toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                when(item?.itemId){
//                    // GravityCompat.START 保证与设置的第二个布局行为一致
//                    android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START)
//                    R.id.back_up -> ToastUtils.show(this, "You clicked BackUp")
//                    R.id.delete -> ToastUtils.show(this, "You clicked Delete")
//                    R.id.settings -> ToastUtils.show("You clicl Settings")
//                }
//                return false
//            }
//        })
        val decodeResource = BitmapFactory.decodeResource(resources, R.mipmap.bg_bitmap1)
        Palette.from(decodeResource).generate {
            palette ->
            val vibrantSwatch = palette?.vibrantSwatch
            supportActionBar?.setBackgroundDrawable(ColorDrawable(vibrantSwatch!!.rgb))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}