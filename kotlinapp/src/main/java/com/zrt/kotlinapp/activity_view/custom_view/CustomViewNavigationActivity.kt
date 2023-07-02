package com.zrt.kotlinapp.activity_view.custom_view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.custom_view.loading_view.LoadingActivity
import com.zrt.kotlinapp.activity_view.custom_view.zuhe_view.TabListActivity
import com.zrt.kotlinapp.activity_view.custom_view_basic.CustomViewBasicNavigationActivity
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_custom_view_navigation.*

class CustomViewNavigationActivity : BasicActivity() {
    var mList: MutableList<CustomViewBasicNavigationActivity.CustomViewNavigation> = ArrayList<CustomViewBasicNavigationActivity.CustomViewNavigation>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_custom_view_navigation
    }

    override fun initData() {

        initList()
        addRecycleView()
    }

    fun addRecycleView() {
        val recyclerView = RecyclerView(this)
        a_c_v_frame.addView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            inner class ViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {
                val i_btn: Button = convertView.findViewById(R.id.i_btn)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_btn, parent, false)
                val viewHolder = ViewHolder(view)
                viewHolder.i_btn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val position = viewHolder.adapterPosition
                        startActivity(Intent(this@CustomViewNavigationActivity, mList[position].startActivity))
                    }
                })
                return viewHolder
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                if (holder is ViewHolder) {
                    holder.i_btn.setText(mList[position].title)
                }
            }

            override fun getItemCount(): Int {
                return mList.size
            }
        }
    }

    fun initList(){
        mList.add(CustomViewBasicNavigationActivity.CustomViewNavigation("TabList", TabListActivity::class.java))
        mList.add(CustomViewBasicNavigationActivity.CustomViewNavigation("Loading View", LoadingActivity::class.java))
    }
}