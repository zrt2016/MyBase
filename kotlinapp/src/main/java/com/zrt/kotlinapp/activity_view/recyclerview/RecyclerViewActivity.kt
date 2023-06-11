package com.zrt.kotlinapp.activity_view.recyclerview

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerItemDecoration
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.ArrayList

/**
 * 在项目build.gradle中引入:
 *  implementation 'androidx.recyclerview:recyclerview:1.1.0'
 */
class RecyclerViewActivity : BasicActivity() {
    val mListData = ArrayList<Fruit>()
    lateinit var mAdapter:FruitRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_recycler_view
    }

    override fun initData() {
        initFruits()
        // 设置布局管理器
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // 设置item增加和删除的动画
        recyclerView.itemAnimator = DefaultItemAnimator()
        // 设置间隔线
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        mAdapter = FruitRVAdapter(mListData)
        recyclerView.adapter = mAdapter
        setListener()
    }

    private fun setListener() {
        mAdapter?.setOnItemClickListener(object : FruitRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, fruit: Fruit) {
                ToastUtils.show("ItemView ${fruit.name}")
            }

            override fun onItemLongClick(view: View?, position: Int, fruit: Fruit) {
                val builder = AlertDialog.Builder(this@RecyclerViewActivity)
                builder.setTitle("确认删除吗？")
                        .setNegativeButton("取消", object :DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                            }
                        })
                        .setPositiveButton("确定", object :DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                mAdapter.removeData(position)
                            }
                        })
                        .show()
            }

        })

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