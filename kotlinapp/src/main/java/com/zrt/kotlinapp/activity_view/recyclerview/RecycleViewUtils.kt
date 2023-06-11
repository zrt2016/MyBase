package com.zrt.kotlinapp.activity_view.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerItemDecoration


/**
 * @author：Zrt
 * @date: 2023/1/31
 */
data class CustomViewNavigation(val title:String, val startActivity:Class<out BasicActivity>)

/**
 * 添加RecycleView
 * @param context
 * @param view 外布局
 * @param mList 需要跳转的activity
 */
fun addRecycleView(context: Context, view: ViewGroup, mList: MutableList<CustomViewNavigation>) {
    val recyclerView = RecyclerView(context)
    view.addView(recyclerView)
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))
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
                    context.startActivity(Intent(context, mList[position].startActivity))
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