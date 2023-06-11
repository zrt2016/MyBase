@file:JvmName("HttpUtils")
package com.zrt.kotlinapp.utils.https

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * @author：Zrt
 * @date: 2022/7/23
 */
val BAIDU_URL = "https://www.baidu.com"
fun connectGetHttpDemo(context: Context, block: (String) ->Unit){
    val url = URL(BAIDU_URL)
    val connection: HttpURLConnection? = null
    try {
        val connection = url.openConnection() as HttpURLConnection
        // 设置GET请求
        connection.requestMethod = "GET"
        // 设置连接超时毫秒数
        connection.connectTimeout = 8000
        // 设置读取超时的毫秒数
        connection.readTimeout = 8000
        // 获取服务器返回的输入刘
        val inputStream = connection.inputStream
        // 对输入流进行读取
        val response = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.use {
            bufferedReader.forEachLine {
                response.append(it)
            }
        }
        block(response.toString())
    }catch (ex:Exception){
    }finally {
        // 关闭流
        connection?.disconnect()
    }
}
// 暂未实验
fun connectPostHttpDemo(context: Context, block: (String) ->Unit){
    val url = URL(BAIDU_URL)
    val connection: HttpURLConnection? = null
    try {
        val connection = url.openConnection() as HttpURLConnection
        // 设置POST请求
        connection.requestMethod = "POST"
        // 设置连接超时毫秒数
        connection.connectTimeout = 8000
        // 设置读取超时的毫秒数
        connection.readTimeout = 8000
        // 上传
        val output = DataOutputStream(connection.outputStream)
        output.writeBytes("username=admin&password=123456")
        output.flush()
        // 获取服务器返回的输入刘
        val inputStream = connection.inputStream
        // 对输入流进行读取
        val response = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.use {
            bufferedReader.forEachLine {
                response.append(it)
            }
        }
        output.close()
        block(response.toString())
    }catch (ex:Exception){
    }finally {
        // 关闭流
        connection?.disconnect()
    }
}

fun sendHttpRequest(address: String):String{
    var connection:HttpURLConnection? = null
    try {
        val response = StringBuilder()
        val url = URL(address)
        connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        val input = connection.inputStream
        val reader = BufferedReader(InputStreamReader(input))
        reader.use {
            reader.forEachLine {
                response.append(it)
            }
        }
        return response.toString()
    }catch (ex:Exception){
        return ex.toString()
    }finally {
        connection?.disconnect()
    }
}

fun sendHttpRequest(address: String, listener: HttpCallbackListener){
    thread {
        var connection:HttpURLConnection? = null
        try {
            val response = StringBuilder()
            val url = URL(address)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            val input = connection.inputStream
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    response.append(it)
                }
            }
            listener.onFinish(response.toString())
        }catch (ex:Exception){
            listener.onError(ex)
        }finally {
            connection?.disconnect()
        }
    }
}