package com.zrt.kotlinapp.utils.https

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author：Zrt
 * @date: 2022/7/26
 * 顶级协程：GlobalScope.launch   当应用程序结束时，会跟着一起结束.
 *         如果在activity中开起，则在activity关闭时关闭该协程.调用cancel()
 * 协程：runBlocking  一般用于测试，保证在协程作用域中所有代码和子协程没有全部执行完之前一直阻塞当前线程。如果在主线程中可能会导致界面卡死
 * suspend:将一个函数声明成挂起函数，可以在函数中调用delay()
 *         声明后该函数只能在协程中调用
 * coroutineScope: 一个挂起函数，可以在函数在开起子协程
 * async {}.await()
 * withContext(Dispatchers.Default) {}
 */
fun main(){
    globalScope()
//    runBlockDemo()
    // 常用协程
//    coroutineScopeDemo()
    // 获取协程返回值
    asyncDemo()
}

/**
 * 顶级协程
 * 输出：
 * codes run in coroutine scope1
 * codes run in coroutine scope2
 */
fun globalScope(){
    /**
     * 创建一个顶层协程，当应用程序结束时，会跟着一起结束.
     * 该协程中的内容可能在程序结束时还未运行
     */
    val launch1 = GlobalScope.launch {
        println("codes run in coroutine scope1")
    }
    // 主线程阻塞1秒后运行，有打印codes run in coroutine scope
    Thread.sleep(1000)
    launch1.cancel()
    /**
     * 如果协程未在1秒内运行完成，还是会被进程杀死
     * 例如下：让协程延迟1.5秒
     */
    val launch2 = GlobalScope.launch {
        println("codes run in coroutine scope2") // 打印
        delay(1500) // 延迟1.5秒后运行
        println("codes run in coroutine scope finish") // 不会打印
    }
    Thread.sleep(1000)
    launch2.cancel()
    val launch3 = GlobalScope.launch {
        val a = System.currentTimeMillis()
        val a1 = async {
            delay(1000) // 延迟1秒后运行
            println("GlobalScope.launch async 1")
        }
        val a2 = async {
            delay(1000) // 延迟1秒后运行
            println("GlobalScope.launch async 2")
        }
        println("GlobalScope.launch async 3 =${a1.await()}${a2.await()} time=${ System.currentTimeMillis() -a}")
    }
}

/**
 * runBlocking：保证在协程作用域中所有代码和子协程没有全部执行完之前一直阻塞当前线程
 * 通常只在测试环境中使用，正式环境使用容易产生一些性能问题
 */
fun runBlockDemo(){
    runBlocking {
        println("runBlocking 0") // 打印
        delay(1500) // 延迟1.5秒后运行
        println("runBlocking 0 end") // 打印
    }
    // 在runBlocking中创建子协程
    runBlocking {
        launch {
            println("runBlocking launch 1") // 打印
            delay(500) // 延迟1.5秒后运行
            println("runBlocking launch 1 end") // 打印
        }
        launch {
            println("runBlocking launch 2") // 打印
            delay(500) // 延迟1.5秒后运行
            println("runBlocking launch 2 end") // 打印
        }
        /**
         * 交替打印
         *  runBlocking launch 1
            runBlocking launch 2
            runBlocking launch 1 end
            runBlocking launch 2 end
         */
    }

}

suspend fun printDot(){
    println("suspend 1")
    delay(1000)
}
suspend fun printDot2() = coroutineScope {
    launch {
        println("suspend coroutineScope 1")
    }
}
fun coroutineScopeDemo(){
    val job = Job()
    val coroutineScope = CoroutineScope(job)
    coroutineScope.launch {

    }
    job.cancel()

}

fun asyncDemo(){
    // 获取协程执行结果返回值
    runBlocking {
        val result = async {
            5+5
        }.await()
        println("##async=$result")
    }
    // 串行async
    runBlocking {
        val a = System.currentTimeMillis()
        val result1 = async {
            delay(1000)
            5+5
        }.await()
        val result2 = async {
            delay(1000)
            2+2
        }.await()
        // 串行async=14 time=2018
        println("串行async=${result1 + result2} time=${System.currentTimeMillis() - a}")
    }

    // 并行async
    runBlocking {
        val a = System.currentTimeMillis()
        val result1 = async {
            delay(1000)
            5+5
        }
        val result2 = async {
            delay(1000)
            2+2
        }
        // 并行async=14 time=1008
        println("并行async=${result1.await() + result2.await()} time=${System.currentTimeMillis() - a}")
    }

}

/**
 * withContext挂起函数，可以理解成async的简化版写法
 * 使用需要指定线程参数：Dispatchers.Default、Dispatchers.IO、Dispatchers.Main
 * Dispatchers.Default：默认使用低并发的线程策略（适用于计算密集型的任务）
 * Dispatchers.IO：使用一种较高并发的线程策略（适用于较高并发的线程策略，例大多数时间是在阻塞和等待中，例如网络请求时）
 * Dispatchers.Main：不会开起子线程，而是在主线程中执行代码（只能在Android中使用）
 */
fun withContextDemo(){
    runBlocking {
        val withContext = withContext(Dispatchers.Default) {
            5 + 5
        }
        println("withContext=$withContext")
    }

}

/**
 * 使用协程简化回调的方法
 */
fun xiechengDemo(){
    // 例：一
    sendHttpRequest("", object : HttpCallbackListener{
        override fun onFinish(response: String) { }
        override fun onError(e: Exception) { }
    })
    // 简化后

    GlobalScope.launch {
        request("")
    }
}

/**
 * suspendCoroutine:将协程立即挂起，然后在一个普通的线程中执行Lambda表达式
 * 通过调用ontinuation.resume(response)或者 continuation.resumeWithException(e)让协程恢复执行
 */
suspend fun request(address:String):String{
    return suspendCoroutine {
        continuation ->
        sendHttpRequest(address, object : HttpCallbackListener{
            override fun onFinish(response: String) {
                continuation.resume(response) // 通知协程恢复执行
            }
            override fun onError(e: Exception) {
                continuation.resumeWithException(e) // 通知协程恢复执行
            }

        })
    }
}
suspend fun getBaiduResponse(){
    try {
        // 获取服务器响应数据
        val response = request(BAIDU_URL)
        // 对服务器响应数据进行处理

        // 调用Retrofit网络请求
        val await = ServiceCreater.create<AppService>().getAppData().await()
    }catch (ex:Exception){}

}
// 简化Retrofit网络请求
suspend fun <T> Call<T>.await():T{
    return suspendCoroutine { continuation ->
        enqueue(object : Callback<T>{
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response body is null"))
            }

        })
    }
}