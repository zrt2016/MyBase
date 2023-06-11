package com.zrt.kotlinapp.activity_view.listview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.lv_item_fruit.view.*

/**
 * @author：Zrt
 * @date: 2022/6/12
 */
class FruitAdapter(context:Context, val resourceId:Int, val data:List<Fruit>)
        : ArrayAdapter<Fruit>(context, resourceId, data) {
    lateinit var inflater: LayoutInflater
    init {
         inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var pView:View
        var mHolder:ViewHolder?
        if (convertView == null){
            pView = inflater.inflate(resourceId, parent, false)
            mHolder = ViewHolder(pView)
            pView.tag = mHolder
        }else {
            pView = convertView
            mHolder = pView.tag as ViewHolder
        }
        val item = getItem(position)
        if (item != null){
            mHolder.fruitImage.setImageResource(item.imageId)
            mHolder.fruitName.setText(item.name)
        }
        return pView
    }
    inner class ViewHolder(val convertView:View){
        lateinit var fruitImage:ImageView
        lateinit var fruitName:TextView
        init {
            fruitImage = convertView.findViewById(R.id.lv_fruit_iamge)
            fruitName = convertView.findViewById(R.id.lv_fruit_name)

        }
    }
}