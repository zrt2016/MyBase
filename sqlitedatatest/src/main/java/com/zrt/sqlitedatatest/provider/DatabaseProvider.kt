package com.zrt.sqlitedatatest.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.zrt.sqlitedatatest.storage.MyDataBaseHelper

/**
 * 需要在AndroidManifest.xml中注册：
 * <provider
        android:name=".provider.DatabaseProvider"
        android:authorities="com.zrt.sqlitedatatest.provider"
        android:enabled="true"
        android:exported="true"></provider>
 *  enabled和exported 允许被其他应用程序访问
 */
class DatabaseProvider : ContentProvider() {
    private val bookDir = 0
    private val bookItem = 1
    private val categoryDir = 2
    private val categoryItem = 3
    private val authority = "com.zrt.sqlitedatatest.provider"
    private lateinit var dbHelper: MyDataBaseHelper

    /**
     * by lazy 在第一次使用的时候初始化
     */
    private val uriMatcher by lazy{
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "book", bookDir)
        matcher.addURI(authority, "book/#", bookItem)
        matcher.addURI(authority, "category", categoryDir)
        matcher.addURI(authority, "category/#", categoryItem)
        matcher
    }

    override fun getType(uri: Uri): String? {
        val dir = "android.cursor.dir"
        val item = "android.cursor.item"
        return when(uriMatcher.match(uri)){
            bookDir -> "vnd.$dir/vnd.$authority.book"
            bookItem -> "vnd.$item/vnd.$authority.book"
            categoryDir -> "vnd.$dir/vnd.$authority.category"
            categoryItem -> "vnd.$item/vnd.$authority.category"
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        // context为null，则返回false，初始化失败
        return context?.let {
            dbHelper = MyDataBaseHelper.getInstance(it)
            true
        } ?: false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return dbHelper?.let {
            val db = it.writableDatabase
            val cursor = when(uriMatcher.match(uri)){
                bookDir -> {
                    db.query("book", projection, selection, selectionArgs,
                            null, null, sortOrder)
                }
                bookItem -> {
                    val bookID = uri.pathSegments[1]
                    db.query("book", projection, "id = ?", arrayOf(bookID),
                            null, null, sortOrder)
                }
                categoryDir -> {
                    db.query("category", projection, selection, selectionArgs,
                            null, null, sortOrder)
                }
                categoryItem -> {
                    val categoryID = uri.pathSegments[1]
                    db.query("category", projection, "id = ?", arrayOf(categoryID),
                            null, null, sortOrder)
                }
                else -> null
            }
            cursor
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return dbHelper?.let {
            val db = it.writableDatabase
            val uriReturn = when (uriMatcher.match(uri)) {
                bookDir, bookItem -> {
                    val bookID = db.insert("book", null, values)
                    Uri.parse("content://$authority/book/$bookID")
                }
                categoryDir, categoryItem -> {
                    val categoryID = db.insert("category", null, values)
                    Uri.parse("content://$authority/book/$categoryID")
                }
                else -> null
            }
            uriReturn
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return dbHelper?.let {
            val db = it.writableDatabase
            val updateRows = when(uriMatcher.match(uri)){
                bookDir -> {
                    db.update("book", values, selection, selectionArgs)
                }
                bookItem -> {
                    val bookID = uri.pathSegments[1]
                    db.update("book", values, "id = ?", arrayOf(bookID))
                }
                categoryDir -> {
                    db.update("category", values, selection, selectionArgs)
                }
                categoryItem -> {
                    val categoryID = uri.pathSegments[1]
                    db.update("category", values, "id = ?", arrayOf(categoryID))
                }
                else -> null
            }
            updateRows
        } ?: 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return dbHelper?.let {
            val db = it.writableDatabase
            val deleteRows = when(uriMatcher.match(uri)){
                bookDir -> {
                    db.delete("book", selection, selectionArgs)
                }
                bookItem -> {
                    val bookId = uri.pathSegments[1]
                    db.delete("book", "id = ?", arrayOf(bookId))
                }
                categoryDir -> {
                    db.delete("category", selection, selectionArgs)
                }
                categoryItem -> {
                    val categoryID = uri.pathSegments[1]
                    db.delete("category", "id = ?", arrayOf(categoryID))
                }
                else -> 0
            }
            deleteRows
        } ?: 0
    }
}
