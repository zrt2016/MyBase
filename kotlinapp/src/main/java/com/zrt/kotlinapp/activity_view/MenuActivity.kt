package com.zrt.kotlinapp.activity_view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R

/**
 * 在Activity中使用menu
 * 第一步：
 * 首先在res目录下新建一个menu文件夹，
 * 接着在这个文件夹下新建一个menu_bar的菜单文件，右击menu文件夹->New->Menu resource file.
 *
 * 第二部：重写onCreateOptionsMenu方法
 */
class MenuActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_menu
    }

    override fun initData() {
        
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        // 返回true，允许创建的菜单显示。false：不显示创建的菜单
        return true
    }

    /**
     * 重写Menu的响应事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_item -> showToast("You clicked add")
            R.id.remove_item -> showToast("You clicked remove")
        }
        return true
    }

    fun showToast(content:String){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}