package com.zrt.kotlinapp.activity_view.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.ArrayList

/**
 * 在项目build.gradle中引入:
 *  implementation 'androidx.recyclerview:recyclerview:1.1.0'
 *  横向滑动RecycleView
 */
class RecyclerViewHActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_recycler_view
    }

    override fun initData() {
        initFruits()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        val adapter = FruitRVAdapter(mListData)
        adapter.resource = R.layout.rv_h_item_fruit
        recyclerView.adapter = adapter
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