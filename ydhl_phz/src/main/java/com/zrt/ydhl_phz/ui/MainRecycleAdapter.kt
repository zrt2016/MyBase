package com.zrt.ydhl_phz.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.activity_view.recyclerview.decoration.DividerGridItemDecoration
import com.zrt.ydhl_phz.R
import com.zrt.ydhl_phz.logic.model.MainMenuMode

/**
 * @author：Zrt
 * @date: 2022/11/13
 */
class MainRecycleAdapter(val context: Context, val mList: List<MainMenuMode>)
        : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder:RecyclerView.ViewHolder
        if (viewType == 0){
            val recyclerView = RecyclerView(parent.context)
            viewHolder = ItemViewHolder(recyclerView, recyclerView)
        }else{
            // 默认
            val recyclerView = RecyclerView(parent.context)
            viewHolder = ItemViewHolder(recyclerView, recyclerView)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0){

        }else {
            // 默认
            var menuMode = mList.get(position)
            var itemView: ItemViewHolder = holder as ItemViewHolder
            val layoutManager = GridLayoutManager(context, 4)
            val itemDecoration = DividerGridItemDecoration(context)
            val menuItemAdapter = MenuItemAdapter(context, menuMode)
            itemView.rv.layoutManager = layoutManager
            itemView.rv.addItemDecoration(itemDecoration)
            itemView.rv.adapter = menuItemAdapter
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ItemViewHolder(val itemView: View, val rv: RecyclerView):RecyclerView.ViewHolder(itemView){
    }

}