package com.zrt.kotlinapp.utils

import android.content.Context
import android.content.Intent

/**
 * @author：Zrt
 * @date: 2022/7/16
 */
inline fun <reified T> getGenericType() = T::class.java
inline fun <reified T> startActivity(context: Context){
    val intent = Intent(context, T::class.java)
    context.startActivity(intent)
}
inline fun <reified T> startActivity(context: Context, block:Intent.()->Unit){
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}