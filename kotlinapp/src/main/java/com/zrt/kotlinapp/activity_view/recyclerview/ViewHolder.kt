package com.zrt.kotlinapp.activity_view.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2022/6/15
 */

sealed class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view)

class LeftViewHolder(view: View) : MsgViewHolder(view){
    val leftMsg: TextView = view.findViewById(R.id.leftMsg)
}
class RightViewHolder(view: View) : MsgViewHolder(view){
    val rightMsg: TextView = view.findViewById(R.id.rightMsg)
}