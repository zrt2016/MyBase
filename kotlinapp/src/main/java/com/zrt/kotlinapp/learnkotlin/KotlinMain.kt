package com.zrt.kotlinapp.learnkotlin

import com.zrt.kotlinapp.learnkotlin.bean.Person
import kotlin.math.max

/**
 * @author：Zrt
 * @date: 2022/6/3
 */
fun main(){
    log("Hello Kotlin")
    var i = 1;
    log(largerNumber(37, 40))
    checkNumber(11)
    checkForOrWhile()
}
fun largerNumber(n1: Int, n2:Int):Int{
    return max(n1, n2)
}
fun largerNumber2(n1: Int, n2:Int) = if (n1 > n2) n1 else n2
fun checkNumber(num: Number): String{
    return when(num){
        is Int -> "number is Int"
        is Double -> "number is Double"
        else -> "number not support"
    }
}
fun checkForOrWhile(){
    val nums = 1..10
    var i = 0
    do {
        log2("nums=${nums.elementAt(i)} 、")
        i++
    }while (i<nums.count())
    log("A-")
    val listOf = listOf<String>("A", "B", "C", "D")
    for ((index, value) in listOf.withIndex()){
        log2("index=$index, value=$value; ")
    }
    log("B-")
    for (item in 0 until 10 step 2){
        log2("item=$item, ")
    } // item=0, item=2, item=4, item=6, item=8,
    log("C-")
    for (item in 10 downTo 1){
        log2("item=$item, ")
    } // item=10, item=9, item=8, item=7, item=6, item=5, item=4, item=3, item=2, item=1,

    val p = Person()
    p.name = "Tom"
    p.age = 28
    p.eat()
    val p2 = Person()
    p.name = "Tom"
    p.age = 28
    log("p == p2 ? ${p == p2}") // p == p2 ? false
    val pd1 = PersonData("Tom", 26)
    val pd2 = PersonData("Tom", 26)
    log("pd1 == pd2 ? ${pd1 == pd2}") // pd1 == pd2 ? true
}
data class PersonData(val name:String, val age:Int)
fun log(message: Any?){
    println(message)
}
fun log2(message: Any?){
    print(message)
}
