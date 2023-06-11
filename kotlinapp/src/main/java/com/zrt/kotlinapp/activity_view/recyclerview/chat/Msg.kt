package com.zrt.kotlinapp.activity_view.recyclerview.chat

/**
 * @author：Zrt
 * @date: 2022/6/14
 */
class Msg(val content: String, val type: Int) {
    companion object{
        // 接收消息
        const val TYPE_RECEIVED = 0
        // 发送消息
        const val TYPE_SEND = 1
    }
}