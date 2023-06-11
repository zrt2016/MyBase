package com.zrt.sqlitedatatest.storage

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

/**
 * @author：Zrt
 * @date: 2022/6/25
 */

class DataManager{
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
