@file:JvmName("OkhttpUtils")
package com.zrt.kotlinapp.utils.https

import okhttp3.*
import java.io.IOException

/**
 * @author：Zrt
 * @date: 2022/7/23
 */

fun connectGetOkhttpDemo(block: (String?) ->Unit){
    val client = OkHttpClient()
    val request = Request.Builder()
            .url(BAIDU_URL)
            .build()
    // 获取服务器返回数据
    val response = client.newCall(request).execute()
    val responseData = response.body()?.string()
    block(responseData)
}
fun connectPostOkhttpDemo(block: (String?) ->Unit){
    val client = OkHttpClient()
    // 构建requestBody存放提交参数对象
    val requestBody = FormBody.Builder()
            .add("name", "admin")
            .add("password", "123456")
            .build()
    val request = Request.Builder()
            .url(BAIDU_URL)
            .post(requestBody)
            .build()
    // 获取服务器返回数据
    val response = client.newCall(request).execute()
    val responseData = response.body()?.string()
    block(responseData)
}

fun sendOkHttpRequest(address: String, callback: okhttp3.Callback) {
    val client = OkHttpClient()
    val request = Request.Builder()
            .url(address)
            .build()
    client.newCall(request).enqueue(callback)
}

fun send(){
    sendOkHttpRequest("", object : Callback{
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body()?.toString()
        }

    })
}
