package com.zrt.kotlinapp.learnkotlin.bean

import com.zrt.kotlinapp.learnkotlin.log

/**
 * @author：Zrt
 * @date: 2022/6/4
 */
open class Person { // open使Person可以被继承
    var name = ""
    var age = 0
    fun eat(){
        println("$name is eating. He is $age years old.")
    }
}
