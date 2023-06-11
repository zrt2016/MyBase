package com.zrt.kotlinapp.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.zrt.kotlinapp.learnkotlin.log
import java.io.*
import java.lang.StringBuilder

/**
 * @author：Zrt
 * @date: 2022/6/23
 * 三大存储：
 *      1、文件存储：
 *      2、SharedPreferences存储：
 *      3、SQLite存储：
 */
class StorageUtils {
}

/**
 * 文件存储：
 * openFileOutput（）
 * 参数一：文件名，文件创建时使用，不包含文件路径。默认存放于：/data/data/<package name>/file/目录下
 * 参数二：文件的操作方式。主要有2中模式选择：
 *      1、MODE_PRIVATE: 指定相同文件名时，所写内容会覆盖原文件内容
 *      2、MODE_APPEND: 指定相同文件名时，会在文件内容追加所写内容
 */
fun saveFile(context: Context, fileName:String = "data",inputString: String){
    try {
        val output = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val writer = BufferedWriter(OutputStreamWriter(output))
        // use 运行完成后会自动关闭writer
        writer.use {
            it.write(inputString)
        }
    }catch (ex:Exception){
        log("error=${ex.toString()}")
    }
}

/**
 * 文件读取
 */
fun readFile(context: Context, fileName:String = "data"):String{
    val content = StringBuilder()
    try {
        val input = context.openFileInput(fileName)
        val reader = BufferedReader(InputStreamReader(input))
        reader.use {
            reader.forEachLine {
                content.append(it)
            }
        }
    }catch (ex:Exception){
        log("error=${ex.toString()}")
    }
    return content.toString()
}

/**
 * SharedPreferences存储：会生成xml文件存储
 * getSharedPreferences()
 *  参数一：文件名称，默认存放于：/data/data/<package name>/shared_prefs/目录下
 *  参数二：操作模式，默认只有一种MODE_PRIVATE，表示只允许当前程序可操作。其他模式均已废弃
 *
 *  apply()提交
 */
fun saveSP(context: Context, fileName:String = "data"){
//    方式一：
//    val editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
//    editor.apply{
//        putString("name", "Tom")
//        putInt("age", 36)
//        putBoolean("marred", false)
//        apply()
//    }
    //方式二
    context.getSharedPreferences(fileName, Context.MODE_PRIVATE).open {
        putString("name", "Tom")
        putInt("age", 36)
        putBoolean("marred", false)
    }
//    方式三：使用KTX库，implementation 'androidx.core:core-ktx:1.1.0'
//    context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit{
//        putString("name", "Tom")
//        putInt("age", 36)
//        putBoolean("marred", false)
//    }
}
/**
 * 简化SharedPreference的用法
 * block：Lambda表达式，添加传参数据
 */
fun SharedPreferences.open(block: SharedPreferences.Editor.()->Unit){
    val edit = edit()
    edit.block()
    edit.apply()
}

fun getSP(context: Context, fileName:String = "data"): String{
    val builder = StringBuilder()
    val prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    builder.append("name=${prefs.getString("name", "")}, ")
            .append("age=${prefs.getInt("age", 0)}, ")
            .append("marred=${prefs.getBoolean("marred", false)}")
    return builder.toString()
}

fun deleteAllSP(context: Context){

    val file = File("/data/data/${context.getPackageName()}/shared_prefs")
    if (file != null && file.exists() && file.isDirectory){
        for (item in file.listFiles()){
            log("delete Name=${item.name}")
            item.delete()
        }
    }
}


