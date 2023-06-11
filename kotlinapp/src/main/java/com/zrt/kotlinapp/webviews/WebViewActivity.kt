package com.zrt.kotlinapp.webviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_web_view

    override fun initData() {
        // 设置webView支持JavaScript脚本
        a_webView.settings.javaScriptEnabled = true
        // 传入一个webViewClient实例，表示在当前页面显示，而不是打开浏览器
        a_webView.webViewClient = WebViewClient()
        a_webView.loadUrl("https://www.baidu.com")
//        runOnUiThread()
    }
}