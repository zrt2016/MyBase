package com.zrt.ydhl_phz.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerItemDecoration
import com.zrt.ydhl_phz.BasicActivity
import com.zrt.ydhl_phz.R
import com.zrt.ydhl_phz.logic.model.MainMenuMode
import com.zrt.ydhl_phz.tools.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_left_user.*
import kotlinx.android.synthetic.main.main_left_yizhu.*
import kotlinx.android.synthetic.main.toolbar_title.*

class MainActivity : BasicActivity() {
    companion object{
        fun actionIntent(context: Context){
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("", "")
            }
            context.startActivity(intent)
        }
    }
    var mList: MutableList<MainMenuMode> = ArrayList<MainMenuMode>()
    lateinit var mainAdapter:MainRecycleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_main
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        setSupportActionBar(main_toolbar)
        supportActionBar?.let {
            // 显示导航按钮
            it.setDisplayHomeAsUpEnabled(true)
            // 设置导航按钮图标
            it.setHomeAsUpIndicator(R.mipmap.icon_sidebar)
            it.setTitle("")
//            main_toolbar_title.setText("陪护证")
        }
        val titleView = layoutInflater.inflate(R.layout.toolbar_title, null)
        val title:TextView = titleView.findViewById<TextView>(R.id.toolbar_title)
        title.setText("陪护证2222")
        main_toolbar.setTitle(titleView, title)
        /**
         * 监听menu滑动时间
         */
        main_drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                if (main_left_user_layout.visibility == View.VISIBLE) {
                    settingLeftUser()
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                // 保证左侧下次滑动打开时，显示的User界面
                main_left_user_layout.visibility = View.VISIBLE
                main_left_yizhu_layout.visibility = View.GONE
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        mList.clear()
        val menuMode:MainMenuMode = MainMenuMode()
        menuMode.group_name = "录入"
        menuMode.is_open = true
        val menuItem: com.zrt.ydhl_phz.logic.model.MenuItem = com.zrt.ydhl_phz.logic.model.MenuItem()
        menuItem.mokuai_name = "陪护证"
        menuItem.mokuai_icon = "phz"
        menuItem.is_html5 = "0"
        menuMode.menuList.add(menuItem)
        mList.add(menuMode)
        // 初始化模块menu
        main_recycle.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        main_recycle.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        mainAdapter = MainRecycleAdapter(this, mList)
        main_recycle.adapter = mainAdapter
//        main_recycle.setOnScrollChangeListener(@RequiresApi(Build.VERSION_CODES.M)
//        object : View.OnScrollChangeListener{
//            override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
//                TODO("Not yet implemented")
//            }
//        })
    }

    /**
     * 在onResume生命周期后调用
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        // 隐藏menu中的医嘱按钮
        menu?.findItem(R.id.left_yizhu)?.setVisible(false)
        return true
    }

    /**
     * 监听menu选中情况
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        main_drawer.closeDrawers()
        when(item.itemId){
            // GravityCompat.START 保证与设置的第二个布局行为一致
            android.R.id.home -> {
                main_drawer.openDrawer(GravityCompat.START)
                main_left_user_layout.visibility = View.VISIBLE
                main_left_yizhu_layout.visibility = View.GONE
            }
            R.id.right_drawer -> main_drawer.openDrawer(GravityCompat.END)
            R.id.left_yizhu -> {
                main_drawer.openDrawer(GravityCompat.START)
                main_left_user_layout.visibility = View.GONE
                main_left_yizhu_layout.visibility = View.VISIBLE
            }
//            R.id.settings -> ToastUtils.show("You clicl Settings")
        }
        return true
    }

    /**
     * 退出登录
     */
    fun goExit(view: View){
        finish();
    }

    /**
     * 系统设置
     */
    fun jumpToAppSetting(view: View){

    }

    fun jumpToMokuai(menuItem: com.zrt.ydhl_phz.logic.model.MenuItem) {
        if ("1".equals(menuItem.is_html5)){
            ToastUtils.show("H5正在开发中,敬请期待 ！")
        }else {
            when(menuItem.mokuai_icon){
                "phz" -> {ToastUtils.show("进入陪护证")}
                else -> {ToastUtils.show("正在开发中,敬请期待 ！")}
            }
        }

    }

    fun settingLeftUser(){
        user_id.setText(currentApplication.currentNurseUser.user_number)
        user_name.setText(currentApplication.currentNurseUser.user_name)
        suoshukeshi.setText(currentApplication.currentNurseUser.current_bingqu_name)
        suoshuzhiwu.setText(currentApplication.currentNurseUser.user_suoshu_bingqu_position)
        app_version.setText(currentApplication.currentNurseUser.api_version)
        shebeihao.setText(currentApplication.currentNurseUser.shebei_id)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
