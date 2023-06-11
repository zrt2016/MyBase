package com.zrt.kotlinapp.activity_view.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerGridItemDecoration
import kotlinx.android.synthetic.main.activity_recycler_view_s_g_l.*
import java.lang.StringBuilder
import java.util.ArrayList

/**
 * 瀑布流
 */
class RecyclerViewSGLActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int {
        return R.layout.activity_recycler_view_s_g_l
    }

    override fun initData() {
        initFruits()
        val layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerGridItemDecoration(this))
        val adapter = FruitRVAdapter(mListData)
        adapter.resource = R.layout.rv_sgl_item_fruit
        recyclerView.adapter = adapter
    }
    fun initFruits(){
        // repeat：把Lambda直线n遍
        repeat(3){
            mListData.add(Fruit("$it${getRandomLengthString("Apple")}", R.mipmap.apple_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Banana")}", R.mipmap.banana_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Orange")}", R.mipmap.orange_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Watermelon")}", R.mipmap.watermelon_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Pear")}", R.mipmap.pear_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Grape")}", R.mipmap.grape_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Pineapple")}", R.mipmap.pineapple_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Strawberry")}", R.mipmap.strawberry_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Cherry")}", R.mipmap.cherry_pic))
            mListData.add(Fruit("$it${getRandomLengthString("Mango")}", R.mipmap.mango_pic))
        }
    }

    fun getRandomLengthString(value:String):String{
        val random = (1..20).random()
        val builder = StringBuilder()
        repeat(random){
            builder.append("、").append(value)
        }
        return builder.toString()
    }
}