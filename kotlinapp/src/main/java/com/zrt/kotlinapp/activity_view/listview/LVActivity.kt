package com.zrt.kotlinapp.activity_view.listview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_l_v.*
import java.util.ArrayList

class LVActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_l_v
    }

    override fun initData() {
        initFruits()
        val adapter = FruitAdapter(this, R.layout.lv_item_fruit, mListData)
        lv_listview.adapter = adapter
        lv_listview.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, mListData[position].name, Toast.LENGTH_SHORT).show()
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