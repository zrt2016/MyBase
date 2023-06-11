package com.zrt.kotlinapp.contentproviders
/**
 * @author：Zrt
 * @date: 2022/6/29
 * contentResolver的基本用法，通过context中的getContentResolver()获取该类的实例
 * 可调用insert()\update()\delete()\query()实现增删改查操作
 * uri:ContentProvider中数据的唯一标识符，分为两个部分组成：authority和path
 *      authority：对不同程序做区分，一般采用应用包名：com.example.app
 *      path:对同一程序中的表做区分。例：/table1或/table2
 *    authority和path进行组合：com.example.app.provider/table1
 *    完整的URI标准格式：content://com.example.app.provider/table1
 */
/**
 * 查询操作，与Sqlite中的查询操作相似
 * URI： 对应from table_name
 * projection： select colimn1, column2
 * selection:where column = ？
 * selectionArgs: 为占位符提供值
 * sortOrder： 指定结果排序
 */
//        contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
/**
 * 插入
 */
//        val contentValuesOf = contentValuesOf("column" to "text", "column2" to 1)
//        contentResolver.insert(uri, contentValuesOf)
/**
 * 更新
 */
//        val values = contentValuesOf("column" to "text")
//        contentResolver.update(uri, values, "column = ? and column2 = ?", arrayOf("text", "1"))
/**
 * 删除
 */
//        contentResolver.delete(uri, " column2 = ?", arrayOf("1"))

