package com.zrt.kotlinapp.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
/**
 * @author：Zrt
 * @date: 2022/6/19
 * 程序加载单页模式和双页模式，通过限定符（qualifier实现）：
 * 单页模式：小屏幕设备适配，当个fragment
 * 双页模式：大屏幕设备适配，多个fragment
 * 在res目录下创建一个layout-large文件夹，存放双页模式使用的布局文件，
 * 文件名称一般与layout中的单页布局名称保持一致。
 * layout-sw480dp:最小宽度限定，屏幕宽度大于等于480dp时加载该布局
 */
class LargeActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_large

    override fun initData() {

    }
}