package com.zrt.kotlinapp.storage

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

/**
 * @author：Zrt
 * @date: 2022/6/25
 */
interface DataBase{
    fun initDataBase(db: SQLiteDatabase?)
    fun dropDataBase(db: SQLiteDatabase?)
}
class DataManager: DataBase{
    companion object{
        val DB_NAME = "dataSQL"
        val DB_VERSION = 1
        var db: MyDataBaseHelper? = null
        var dataManager: DataManager? = null
        fun myInstance(application:Application): DataManager?{
            if (dataManager == null){
                dataManager = DataManager()
            }
            if (db == null || DB_VERSION > db!!.version) {
                db = MyDataBaseHelper(application, DB_NAME, DB_VERSION, dataManager)
            }
            return dataManager
        }
        fun dbW(sql:String){
            db?.writableDatabase?.execSQL(sql)
        }
    }

    override fun initDataBase(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS book(id integer primary key autoincrement, author text," +
                " price real, pages integer, name text)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS category(id integer primary key autoincrement, category_name text, category_code integer)")
    }

    override fun dropDataBase(db: SQLiteDatabase?) {
        db?.execSQL("drop table if exists book")
        db?.execSQL("drop table if exists category")
    }

    /**
     * 优化ContentValues
     * KTX库中也提供了类似的方法：contentValuesOf
     */
    fun cvOf(vararg pairs:Pair<String, Any?>):ContentValues{
        val cv = ContentValues()
        cv.apply {
            for (pair in pairs){
                val key = pair.first
                val value = pair.second
                when(value){
                    is Int -> put(key, value)
                    is Long -> put(key, value)
                    is Short -> put(key, value)
                    is Float -> put(key, value)
                    is Double -> put(key, value)
                    is Boolean -> put(key, value)
                    is String -> put(key, value)
                    is Byte -> put(key, value)
                    is ByteArray -> put(key, value)
                    null -> putNull(key)
                }
            }
        }
        return cv
    }

}
