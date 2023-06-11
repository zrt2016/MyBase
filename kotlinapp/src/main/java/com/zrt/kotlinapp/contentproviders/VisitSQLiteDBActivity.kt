package com.zrt.kotlinapp.contentproviders

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.contentValuesOf
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log
import kotlinx.android.synthetic.main.activity_visit_s_q_lite_d_b.*

/**
 * 访问第三方sqlitedatatest APP的数据、
 * 具体实现可以查看sqlitedatatest APP的provider.DatabaseProvider类
 * 测试生成日志：
 * System.out: insert id2
 * System.out: book id=1, name=Kings,author=George,pages=1040,price=29.8
 * System.out: book id=2, name=Kings,author=George,pages=1040,price=29.8
 * System.out: book id=1, name=Kings,author=George,pages=1040,price=29.8
 * System.out: book id=2, name=Kings-2,author=George-2,pages=1200,price=24.5
 * System.out: book id=1, name=Kings,author=George,pages=1040,price=29.8
 */
class VisitSQLiteDBActivity : BasicActivity() {
    var bookID:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_visit_s_q_lite_d_b

    override fun initData() {
        v_s_q_addData.setOnClickListener {
            val uri = Uri.parse("content://com.zrt.sqlitedatatest.provider/book")
            val values = contentValuesOf("name" to "Kings", "author" to "George",
                            "pages" to 1040, "price" to 29.8)
            val newUri = contentResolver.insert(uri, values)
            bookID = newUri?.pathSegments?.get(1)
            log("insert id${bookID}")
        }
        v_s_q_queryData.setOnClickListener {
            val uri = Uri.parse("content://com.zrt.sqlitedatatest.provider/book")
            contentResolver.query(uri, null, null, null, null)?.apply {
                while (moveToNext()) {
                    log("book id=${getString(getColumnIndex("id"))}, " +
                            "name=${getString(getColumnIndex("name"))}," +
                            "author=${getString(getColumnIndex("author"))}," +
                            "pages=${getInt(getColumnIndex("pages"))}," +
                            "price=${getDouble(getColumnIndex("price"))}")
                }
                close()
            }
        }
        v_s_q_updateData.setOnClickListener {
            bookID?.let {
                val uri = Uri.parse("content://com.zrt.sqlitedatatest.provider/book/$bookID")
                val values = contentValuesOf("name" to "Kings-2", "author" to "George-2",
                        "pages" to 1200, "price" to 24.5)
                contentResolver.update(uri, values, null, null)
            }
        }
        v_s_q_deleteData.setOnClickListener {
            bookID?.let {
                val uri = Uri.parse("content://com.zrt.sqlitedatatest.provider/book/$bookID")
                contentResolver.delete(uri, null, null)
            }
        }
    }
}