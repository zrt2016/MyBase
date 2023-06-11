package com.zrt.kotlinapp.storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zrt.kotlinapp.utils.ToastUtils

/**
 * @author：Zrt
 * @date: 2022/6/25
 */
class MyDataBaseHelper(val context: Context, val name: String, val version:Int, val data:DataManager?)
        : SQLiteOpenHelper(context, name, null, version) {

    /**
     * 创建表
     */
    override fun onCreate(db: SQLiteDatabase?) {
        data?.initDataBase(db)
        ToastUtils.show(context, "Create Table Sucess")
    }

    /**
     * 更新数据库
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        data?.dropDataBase(db)
        onCreate(db)
    }
}