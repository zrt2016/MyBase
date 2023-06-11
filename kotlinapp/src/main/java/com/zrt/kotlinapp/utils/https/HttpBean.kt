package com.zrt.kotlinapp.utils.https

/**
 * @author：Zrt
 * @date: 2022/7/25
 */

class App(val id:String, val name:String, val version:String){
    override fun toString(): String {
        return "App(id='$id', name='$name', version='$version')"
    }
}
class Data(val id:String, val content:String){
    override fun toString(): String {
        return "Data(id='$id', content='$content')"
    }
}