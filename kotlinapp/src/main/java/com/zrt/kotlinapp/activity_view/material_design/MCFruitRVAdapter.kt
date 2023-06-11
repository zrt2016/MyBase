package com.zrt.kotlinapp.activity_view.material_design

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.listview.Fruit

/**
 * @author：Zrt
 * @date: 2022/6/12
 * 卡片布局
 */
class MCFruitRVAdapter(val context:Context, val data:ArrayList<Fruit>): RecyclerView.Adapter<MCFruitRVAdapter.ViewHolder>() {
    @LayoutRes
    var resource:Int = R.layout.rv_item_mc_fruit
    inner class ViewHolder(val convertView: View): RecyclerView.ViewHolder(convertView){
        val fruitImage:ImageView = convertView.findViewById(R.id.lv_fruit_iamge)
        val fruitName:TextView = convertView.findViewById(R.id.lv_fruit_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        val holder = ViewHolder(view)
        // 设置Item点击事件
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val fruit = data[position]
//            Toast.makeText(parent.context, "ItemView ${data[position].name}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, CollapsingToolbarActivity::class.java).apply {
                putExtra(CollapsingToolbarActivity.FRUIT_NAME, fruit.name)
                putExtra(CollapsingToolbarActivity.FRUIT_IMAGE_ID, fruit.imageId)
            }
            context.startActivity(intent)
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
        holder.fruitName.setText(fruit.name)
//        holder.fruitImage.setImageResource(fruit.imageId)
        // 使用glide图片加载库
        Glide.with(context).load(fruit.imageId).into(holder.fruitImage)

    }
}