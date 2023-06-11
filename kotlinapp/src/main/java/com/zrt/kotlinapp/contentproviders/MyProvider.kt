package com.zrt.kotlinapp.contentproviders

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * @author：Zrt
 * @date: 2022/6/30
 * 在AndroidManifest.xml中注册
 * <provider
        android:authorities="com.zrt.kotlinapp.provider"
        android:name=".contentproviders.MyProvider"
        android:enabled="true"
        android:exported="true"/>
 * enabled和exported 允许被其他应用程序访问
 */
class MyProvider: ContentProvider() {

    val table1Dir = 0
    val table1Item = 1
    val table2Dir = 2
    val table2Item = 3

    /**
     * 借助UriMatcher的addUri把authority、path和自定义代码加载进去
     * 后调用UriMatcher.match匹配传入的URI所对应的自定义代码
     */
    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    /**
     * URI：
     *  * 匹配任意长度的字符串
     *  # 匹配任意长度的数字
     */
    init {
        uriMatcher.addURI("com.zrt.kotlinapp.provider", "table1", table1Dir)
        uriMatcher.addURI("com.zrt.kotlinapp.provider", "table1/#", table1Item)
        uriMatcher.addURI("com.zrt.kotlinapp.provider", "table2", table2Dir)
        uriMatcher.addURI("com.zrt.kotlinapp.provider", "table2/#", table2Item)
    }

    /**
     * 根据传入的URI返回相应的MIME类型
     * 一个URI所对应的MIME字符串主要有3部分组成
     *   1、vnd开头
     *   2、如果内容URI以路径结尾，则接上：android.cursor.dir/
     *      如果内容URI以id结尾，则接上：android.cursor.item/
     *   3、最后接上：vnd.<authority>.<path>
     *  例:对于content://com.zrt.kotlinapp.provider/table1这个URI所对应的MIME类型
     *      vnd.android.cursor.dir/vnd.com.zrt.kotlinapp.provider.table1
     */
    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)){
            table1Dir -> "vnd.android.cursor.dir/vnd.com.zrt.kotlinapp.provider.table1"
            table1Item -> "vnd.android.cursor.item/vnd.com.zrt.kotlinapp.provider.table1"
            table2Dir -> "vnd.android.cursor.dir/vnd.com.zrt.kotlinapp.provider.table2"
            table2Item -> "vnd.android.cursor.item/vnd.com.zrt.kotlinapp.provider.table2"
            else -> null
        }
    }
    /**
     * 初始化时调用：返回true初始化成功，返回false初始化失败
     * 通常再次完成数据库的创建和升级操作
     */
    override fun onCreate(): Boolean {
        return false
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        when (uriMatcher.match(uri)){
            table1Dir -> {}
            table1Item -> {}
            table2Dir -> {}
            table2Item -> {}
        }
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }


}