package com.zrt.kotlinapp.utils.https

import java.lang.Exception

/**
 * @author：Zrt
 * @date: 2022/7/25
 */
interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}