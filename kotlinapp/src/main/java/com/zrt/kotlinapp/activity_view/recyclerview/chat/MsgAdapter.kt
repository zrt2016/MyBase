package com.zrt.kotlinapp.activity_view.recyclerview.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.activity_view.recyclerview.LeftViewHolder
import com.zrt.kotlinapp.activity_view.recyclerview.RightViewHolder

/**
 * @author：Zrt
 * @date: 2022/6/14
 */
class MsgAdapter(val msgList: List<Msg>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Msg.TYPE_RECEIVED){
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.msg_left_item, parent, false)
            return LeftViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.msg_right_item, parent, false)
            return RightViewHolder(view)
        }
    }
    override fun getItemCount(): Int = msgList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when(holder){
            is LeftViewHolder -> holder.leftMsg.setText(msg.content)
            is RightViewHolder -> holder.rightMsg.text = msg.content
        }
    }

    // 使用ViewHolder.kt中的密封类MsgViewHolder进行处理
//    inner class LeftViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val leftMsg:TextView = view.findViewById(R.id.leftMsg)
//    }
//    inner class RightViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val rightMsg:TextView = view.findViewById(R.id.rightMsg)
//    }
}