package com.zrt.sqlitedatatest.storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author：Zrt
 * @date: 2022/6/25
 */
class MyDataBaseHelper(val context: Context, private val version:Int = CURRENT_VERSION)
        : SQLiteOpenHelper(context, DB_NAME, null, version) {

    /**
     * 创建表
     */
    override fun onCreate(db: SQLiteDatabase?) {
        initDataBase(db)
    }

    /**
     * 更新数据库
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        dropDataBase(db)
        onCreate(db)
    }

    fun write(sql:String) = writableDatabase.execSQL(sql)
    fun reader(sql:String) = readableDatabase.execSQL(sql)

    companion object{
        val DB_NAME = "dataSQL" //数据库名称
//        var TABLE_NAME = "user_info" //表名称
        var CURRENT_VERSION = 1 //当前的最新版本，如有表结构变更，该版本号要加一
        private var instance: MyDataBaseHelper? = null
        @Synchronized
        fun getInstance(context: Context): MyDataBaseHelper {
            if (instance == null) {
                //如果调用时没传版本号，就使用默认的最新版本号
                instance = MyDataBaseHelper(context.applicationContext)
            }
            return instance!!
        }

        fun initDataBase(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS book(id integer primary key autoincrement, author text," +
                    " price real, pages integer, name text)")
            db?.execSQL("CREATE TABLE IF NOT EXISTS category(id integer primary key autoincrement, category_name text, category_code integer)")
        }

        fun dropDataBase(db: SQLiteDatabase?) {
            db?.execSQL("drop table if exists book")
            db?.execSQL("drop table if exists category")
        }
    }
}