package com.zrt.kotlinapp.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zrt.kotlinapp.activity_view.recyclerview.chat.Msg
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

val JSON_LIST = """[{"state":"ok","error":"200","msg":""},{"state":"ok","error":"200","msg":""}]"""
/**
 * @author：Zrt
 * @date: 2022/7/24
 */
/**
 * [{"state":"ok","error":"200","msg":""},{"state":"ok","error":"200","msg":""}]
 */
fun parseJSONList(json:String):List<HashMap<String, String>>{
    val list = arrayListOf<HashMap<String, String>>()
    try {
        val jsonArray = JSONArray(json)
        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            list.add(parseJSONObject(jsonObject.toString()))
        }
    }catch (ex:Exception){
        Log.i("", ex.toString())
    }
    return list
}
fun parseJSONObject(json:String):HashMap<String, String>{
    val map = hashMapOf<String, String>()
    try {
        val jsonObject = JSONObject(json)
        val keys = jsonObject.keys()
        keys.forEach {
            map.put(it, jsonObject.getString(it))
        }
    }catch (ex:Exception){
        Log.i("", ex.toString())
    }
    return map
}

/**
 * 使用Gson解析
 *
 */
fun parseJSONWithGSON(json:String){

}
class DataJson(val state:String, val error:String, val msg: String){
    override fun toString(): String {
        return "DataJson(state='$state', error='$error', msg='$msg')"
    }
}
/**
 * {"state":"ok","error":"200","msg":""}
 */
fun parseGsonObject(json:String):DataJson{
    val gson = Gson()
    val fromJson = gson.fromJson<DataJson>(json, DataJson::class.java)
    return fromJson
}
inline fun <reified T> parseGsonObjectT(json:String):T{
    val gson = Gson()
    val fromJson = gson.fromJson<T>(json, T::class.java)
    return fromJson
}

/**
 * [{"state":"ok","error":"200","msg":""},{"state":"ok","error":"200","msg":""}]
 */
fun parseGsonList(json:String):List<DataJson>{
    val gson = Gson()
    val type = object : TypeToken<List<DataJson>>() {}.type
    val fromJson = gson.fromJson<List<DataJson>>(json, type)
    return fromJson
}
inline fun <reified T> parseGsonListT(json:String):List<T>{
    val gson = Gson()
    val type = object : TypeToken<List<T>>() {}.type
    val fromJson = gson.fromJson<List<T>>(json, type)
    return fromJson
}