package com.zrt.kotlinapp.fragment

/**
 * @author：Zrt
 * @date: 2022/6/19
 * 程序加载单页模式和双页模式，通过限定符（qualifier实现）：
 * 单页模式：小屏幕设备适配，当个fragment
 * 双页模式：大屏幕设备适配，多个fragment
 * 在res目录下创建一个layout-large文件夹，存放双页模式使用的布局文件，
 * 文件名称一般与layout中的单页布局名称保持一致。
 */
class LargeFragment : BasicFragment() {

}