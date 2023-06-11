@file:JvmName("LambdaUtils") // java中可调用
package com.zrt.kotlinapp.utils

/**
 * @author：Zrt
 * @date: 2022/7/11
 */

infix fun String.beginsWith(prefix: String) = startsWith(prefix)

infix fun <T> Collection<T>.has(element: T) = contains(element)

infix fun <A, B> A.with(that: B): Pair<A, B> = Pair(this, that)
// infix函数构建中缀调用
fun infix1(){
    if ("hello kotlin" beginsWith "hello"){

    }
    val list = listOf<String>("A", "B", "C", "D")
    if (list has "B"){}
    val mapOf = mapOf("A" with 1, "B" with 2, "C" to 3)
    print("${mapOf.toString()}；${mapOf["C"]}")
}
fun main() {
    infix1()
}