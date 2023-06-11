package com.zrt.weatherapp.tools

import android.content.Context
import android.widget.Toast
import com.zrt.weatherapp.MyApplication

/**
 * @author：Zrt
 * @date: 2022/6/20
 */
object ToastUtils {
    fun show(context:Context = MyApplication.context, msg:String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun show(msg:String){
        Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show()
    }
}