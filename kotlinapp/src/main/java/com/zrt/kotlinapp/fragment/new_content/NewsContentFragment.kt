package com.zrt.kotlinapp.fragment.new_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.BasicFragment
import kotlinx.android.synthetic.main.fragment_news_content.*

/**
 * @author：Zrt
 * @date: 2022/6/19
 */
class NewsContentFragment:BasicFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_content, container, false)
    }
    fun refresh(title: String, content: String){
        contentLayout.visibility = View.VISIBLE
        newsTitle.setText(title) // 刷新新闻标题
        newsContent.text = content // 刷新新闻内容
    }
}