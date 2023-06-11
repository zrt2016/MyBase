package com.zrt.kotlinapp.utils

import java.lang.Exception

/**
 * @author：Zrt
 * @date: 2022/6/15
 */
/**
 * 密封类
 */
sealed class Result
class Sucess(val msg:String):Result()
class Failure(val error:Exception):Result()
fun getResult(result: Result) = when(result){
    is Sucess -> result.msg
    is Failure -> "Error is ${result.error.toString()}"
}