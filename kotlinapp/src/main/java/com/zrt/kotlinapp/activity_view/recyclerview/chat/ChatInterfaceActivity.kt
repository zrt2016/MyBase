package com.zrt.kotlinapp.activity_view.recyclerview.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_chat_interface.*

/**
 * 精美的聊天界面
 */
class ChatInterfaceActivity : BasicActivity() {
    lateinit var msgAdapter:MsgAdapter
    val msgList = ArrayList<Msg>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_chat_interface

    override fun initData() {
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // 校验是否已初始化，避免重复初始化
        if (!::msgAdapter.isInitialized) {
            msgAdapter = MsgAdapter(msgList)
        }
        recyclerView.adapter = msgAdapter
        send.setOnClickListener {
            val content = inputText.text.toString()
            if (content.isNotEmpty()){
                val msg = Msg(content, Msg.TYPE_SEND)
                msgList.add(msg)
//              当前有新消息，刷新recyclerView的显示
                msgAdapter?.notifyItemInserted(msgList.size-1)
//                定位到recyclerView最后一行
                recyclerView.scrollToPosition(msgList.size-1)
                inputText.setText("")
            }
        }
    }

    private fun initMsg() {
        msgList.add(Msg("Hello guy", Msg.TYPE_RECEIVED))
        msgList.add(Msg("Hello. Who is that?", Msg.TYPE_SEND))
        msgList.add(Msg("This is Tom. Nice talking to you.", Msg.TYPE_RECEIVED))
    }
}