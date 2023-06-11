package com.zrt.kotlinapp.fragment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.new_content.NewsContentFragment
import kotlinx.android.synthetic.main.activity_news_content.*

/**
 * 一个简易版的新闻应用
 */
class NewsContentActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int = R.layout.activity_news_content

    override fun initData() {
        val news_title = intent.getStringExtra("news_title")
        val news_content = intent.getStringExtra("news_content")
        if (news_content != null && news_title != null){
            val fragment = newsContentFrag as NewsContentFragment
            fragment.refresh(news_title, news_content)
        }
    }
    companion object{
        fun actionStart(context:Context, title: String, content: String){
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_title", title)
                putExtra("news_content", content)
            }
            context.startActivity(intent)
        }
    }
}