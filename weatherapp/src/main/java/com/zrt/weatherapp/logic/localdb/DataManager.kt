package com.zrt.weatherapp.logic.localdb

import com.zrt.weatherapp.MyApplication

/**
 * @author：Zrt
 * @date: 2022/11/17
 */
class DataManager {
    companion object{
        val dbName = "ydhl.db"
        val db_version = 1
        val databaseHelper: DatabaseHelper by lazy { DatabaseHelper(MyApplication.context, dbName, db_version) }
    }
}