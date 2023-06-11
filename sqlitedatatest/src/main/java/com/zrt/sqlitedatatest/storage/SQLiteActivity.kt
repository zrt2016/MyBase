package com.zrt.sqlitedatatest.storage

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zrt.sqlitedatatest.BasicActivity
import com.zrt.sqlitedatatest.R
import kotlinx.android.synthetic.main.activity_s_q_lite.*
import java.lang.Exception

/**
 * SQLitter数据库存储
 */
class SQLiteActivity : BasicActivity() {
    lateinit var db:MyDataBaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = MyDataBaseHelper.getInstance(application)
    }

    override fun getLayoutResID(): Int = R.layout.activity_s_q_lite

    override fun initData() {
    }

    fun onClick(view: View){
        when(view.id){
            R.id.sql_create_table ->{
                val sql = "insert into book(author,price, pages, name) values('张三','36.0','120','Android')"
                db?.writableDatabase?.execSQL(sql)
            }
            R.id.sql_upgrade_db ->{
//                DataManager.myInstance(application)
//                DataManager.DB_VERSION++
                sql_create_table.performClick()
            }
            R.id.sql_insert_data ->{
                /**
                 * insert()
                 * 参数一：表名
                 * 参数二：在未指定添加数据的情况下给某些可为空的列自动赋值null，一般可为null
                 * 参数三：ContentValues对象，放入需要添加的数据
                 */
                val cp = ContentValues()
                cp.apply {
                    put("author", "李四")
                    put("price", 28.0)
                    put("pages", 90)
                    put("name", "Kotlin")
                }
                db.writableDatabase.insert("book", null, cp)
            }
            R.id.sql_update_data->{
                /**
                 * update（）
                 * 参数一：表名
                 * 参数二：ContentValues对象，放入需要更新的对象
                 * 参数三和参数四：更新某一行或某几行的数据，不指定默认更新所有
                 */
                val cp = ContentValues()
                cp.apply {
                    put("author", "王五")
                    put("price", 48.0)
                    put("pages", 190)
                    put("name", "Kotlins")
                }
                db.writableDatabase.update("book", cp,
                        "name=? and author=?", arrayOf("Kotlin", "李四"))
            }
            R.id.sql_delete_data->{
                /**
                 * delete（）
                 * 参数一：表名
                 * 参数二和参数三：删除条件
                 */
                db.writableDatabase.delete("book", "pages > ?", arrayOf("150"))
            }
            R.id.sql_query_data->{
                /**
                 * query()
                 * 参数：
                 * 1、tablie：表名
                 * 2、columns：指定查询列明：select column1,column2
                 * 3、selection：指定where约束条件 where column=value
                 * 4、selectionArgs：为where中的占位符提供具体的值 group by column1
                 * 5、groupBy：指定需要groupBy的列 group by column=value
                 * 6、having：对group by后的结果进一步约束 having c
                 * 7、orderBy：指定查询结果排序 order by column
                 */
                val cursor: Cursor? = db.readableDatabase.query("book", null, null, null, null, null, null)
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        fun checkColumn(key:String): String{
                            val columnIndex = cursor.getColumnIndex(key)
                            if (columnIndex >= 0){
                                return cursor.getString(columnIndex)
                            }else{
                                return ""
                            }
                        }
                        Log.i(">>>>", "book id=${checkColumn("id")}")
                        Log.i(">>>>", "book name=${checkColumn("name")}")

                    }
                    cursor.close()
                }
            }
            R.id.sql_query_data_transaction->{
                //开起事务查询
                val dbw = db.writableDatabase
                dbw.beginTransaction() // 开启事务
                try {
                    val cp = ContentValues()
                    cp.apply {
                        put("author", "赵六")
                        put("price", 128.0)
                        put("pages", 290)
                        put("name", "Java")
                    }
                    dbw.insert("book", null, cp)
                    dbw.setTransactionSuccessful() // 事务执行成功
                }catch (ex:Exception){

                }finally {
                    dbw.endTransaction() // 关闭事务
                }
            }
        }
    }
}