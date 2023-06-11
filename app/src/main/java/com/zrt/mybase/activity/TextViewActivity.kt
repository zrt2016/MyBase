package com.zrt.mybase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import com.zrt.mybase.R
import com.zrt.mybase.base.BaseActivity

class TextViewActivity : BaseActivity() {
    lateinit var tv_html: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_text_view

    override fun initData() {
//        tv_html.setTextColor(getResources().getColor(R.color.green));
        // 字体加粗
//        tv_html.setText(Html.fromHtml("<b><font color=#ff0000>接触隔离</font></b>"))
        tv_html.setText(Html.fromHtml("<font color=#ff0000>接触隔离</font>"))
        tv_html.append(" 张三 19床 ")
        tv_html.setTextColor(getResources().getColor(R.color.green));
    }

    override fun initView() {
        tv_html = findViewById(R.id.tv_html)
    }
}