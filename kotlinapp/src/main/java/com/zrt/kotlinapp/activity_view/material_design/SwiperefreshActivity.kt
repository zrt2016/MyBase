package com.zrt.kotlinapp.activity_view.material_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import kotlinx.android.synthetic.main.activity_swiperefresh.*

import java.util.ArrayList
import kotlin.concurrent.thread

/**
 * 下拉刷新
 * 添加：implementation 'com.google.android.material:material:1.0.0'
 * 或者：implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.0.0'
 */
class SwiperefreshActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

    override fun getLayoutResID(): Int = R.layout.activity_swiperefresh

    override fun initData() {
        initFruits()
        val layoutManager = GridLayoutManager(this,2)
        a_s_recycle.layoutManager = layoutManager
        val adapter = MCFruitRVAdapter(this, mListData)
        a_s_recycle.adapter = adapter
        // 设置下拉刷新进度条的颜色
        a_swiperefresh.setColorSchemeResources(R.color.colorPrimary)
        // 接着设置下拉刷新的监听器，进行下拉刷新操作时，会回调Lambda表达式，在此处处理刷新逻辑
        a_swiperefresh.setOnRefreshListener {
            refreshFruits(adapter)
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

    private fun refreshFruits(adapter: MCFruitRVAdapter) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                a_swiperefresh.isRefreshing = false
            }
        }
    }
}