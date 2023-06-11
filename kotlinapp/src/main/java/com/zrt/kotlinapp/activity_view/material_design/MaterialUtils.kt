package com.zrt.kotlinapp.activity_view.material_design

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * @author：Zrt
 * @date: 2022/8/3
 * 简化写法
 */

/**
 * "This is toast".showToast(this)
 */
fun String.showToast(context:Context, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, this, duration).show()
}

/**
 * R.string.app_name.showToast(this)
 */
fun Int.showToast(context:Context, duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, this, duration).show()
}

/**
 * view.showSnackbar("This is Snackbar")
 */
fun View.showSnackbar(text:String, duration: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, text, duration).show()
}
fun View.showSnackbar(resId:Int, duration: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, resId, duration).show()
}
fun View.showSnackbar(text:String, duration: Int = Snackbar.LENGTH_SHORT, actionText: String? = null,
        block:(()-> Unit)? = null){
    val make = Snackbar.make(this, text, duration)
    if (actionText != null && block != null){
        // 添加点击事件
        make.setAction(actionText){
            block()
        }
    }
    make.show()
}
fun View.showSnackbar(resId:Int, duration: Int = Snackbar.LENGTH_SHORT, actionResId: Int? = null,
                      block:(()-> Unit)? = null){
    val make = Snackbar.make(this, resId, duration)
    if (actionResId != null && block != null){
        // 添加点击事件
        make.setAction(actionResId){
            block()
        }
    }
    make.show()
}