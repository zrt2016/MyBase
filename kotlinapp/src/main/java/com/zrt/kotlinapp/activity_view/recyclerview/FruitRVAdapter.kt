package com.zrt.kotlinapp.activity_view.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit
import com.zrt.kotlinapp.utils.ToastUtils

/**
 * @author：Zrt
 * @date: 2022/6/12
 */
class FruitRVAdapter(val data:ArrayList<Fruit>): RecyclerView.Adapter<FruitRVAdapter.ViewHolder>() {
    @LayoutRes
    var resource:Int = R.layout.rv_item_fruit
    var mOnItemClickListener:OnItemClickListener? = null
    inner class ViewHolder(val convertView: View): RecyclerView.ViewHolder(convertView){
        val fruitImage:ImageView = convertView.findViewById(R.id.lv_fruit_iamge)
        val fruitName:TextView = convertView.findViewById(R.id.lv_fruit_name)

    }
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, fruit:Fruit)
        fun onItemLongClick(view: View?, position: Int, fruit:Fruit)
    }

    fun setOnItemClickListener(onItemClickListener:OnItemClickListener){
        this.mOnItemClickListener = onItemClickListener
    }

    fun removeData(position:Int){
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        val holder = ViewHolder(view)
        // 设置Item点击事件
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
//            Toast.makeText(parent.context, "ItemView ${data[position].name}", Toast.LENGTH_SHORT).show()
            if (mOnItemClickListener != null){
                mOnItemClickListener?.onItemClick(holder.itemView, position, data[position])
            }else {
                ToastUtils.show("ItemView ${data[position].name}")
            }
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            mOnItemClickListener?.onItemLongClick(holder.itemView, position, data[position])
            return@setOnLongClickListener false
        }
        // 设置view的点击事件
        holder.fruitImage.setOnClickListener {
            val position = holder.adapterPosition
            Toast.makeText(parent.context, data[position].name, Toast.LENGTH_SHORT).show()
        }
        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = data[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.setText(fruit.name)

    }
}