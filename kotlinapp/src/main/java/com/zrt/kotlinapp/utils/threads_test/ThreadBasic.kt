package com.zrt.kotlinapp.utils.threads_test

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author：Zrt
 * @date: 2022/9/28
 * 线程状态：
 * 1、New：Thread() 创建一个线程，但是还没有启动
 * 2、Runnable： 可允许状态，调用start方法后，开始运行。
 * 3、Blocked：阻塞状态，线程被锁阻塞，无法活动
 * 4、Waiting：等待状态，线程通过Object.wait()或Object.join()暂时无法运行，处于等待的状态，
 *      等待其他线程通过Object.notify()或Object.notifyAll()唤醒
 * 5、Timed waiting：超时等待状态，与等待状态不同，他可以在指定时间自行返回。通过Thread.sleep(long)
 *      、join(long)或wait(long)停止指定时间，然后自动唤醒或通过其他线程Object.notify()或Object.notifyAll()唤醒
 * 6、Terminated：终止状态。表示线程正常执行完毕，或异常终止了运行
 *
 * 关键字：volatile：修饰变量，该变量需保持原子性，对变量的操作不会依赖于当前值.
 *  
 */
class ThreadBasic {}

fun main(arg:Array<String>){
    TestThread().start()
    Thread(TestRunnable()).start()
    val callable = TestCallable()
    val mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    val submit = mExecutorService.submit(callable)
    try {
        println("Submit=${submit.get()}")
    }catch (ex:Exception){
        println("error=${ex.toString()}")
    }
//    val testInterrupte = TestInterrupte()
//    testInterrupte.start()
//    TimeUnit.MICROSECONDS.sleep(101)
//    // 中断线程：线程被阻塞时，无法检测中断状态，且会报错,但线程不会停止运行
//    testInterrupte.interrupt()
}


/**
 * 创建线程的几种方法
 * TestThread
 * TestRunnable
 * TestCallable:可以在任务结束后，提供一个返回值。call可以抛出异常
 */
class TestThread: Thread() {
    override fun run() { }
}
class TestRunnable:Runnable{
    override fun run() {}
}
class TestCallable: Callable<String>{
    override fun call(): String {
        return "1"
    }
}

/**
 * 线程中断
 * */
class TestInterrupte: Thread() {
    override fun run() {
        try {
            while (!Thread.currentThread().isInterrupted){
                print("1")
                sleep(50)
            }
        }catch (ex:InterruptedException){
            Thread.currentThread().interrupt()
            print("2")
        }

    }
}
// 重入锁
class Alipay{
    lateinit var accounts:DoubleArray
    lateinit var alipayLock:Lock
    lateinit var condition:Condition
    constructor(n:Int, money:Double){
        accounts = DoubleArray(n)
        alipayLock = ReentrantLock()
        condition = alipayLock.newCondition()
        for (i in 0 until accounts.size){
            accounts[i] = money
        }
    }
    fun transfer(from:Int, to:Int, amount:Int){
        // 加锁
        alipayLock.lock()
        try {
            while (accounts[from]<amount){
                // 阻塞当前线程，并放弃锁
                condition.await()
            }
            accounts[from] = accounts[from] - amount
            accounts[to] = accounts[to] + amount
            // 激活其他等待的线程
            condition.signalAll()
        }finally {
            // 解锁
            alipayLock.unlock()
        }
    }
    @Synchronized
    fun transfer2(){
        // 加锁
    }
}

/**
 * Volatile关键字
 * */
class MoonRunner : Runnable{
    private var i: Long = 0
    @Volatile
    var on:Boolean = true
    override fun run() {
        while (on){
            i++
            println("##i=$i")
        }
        println("stop")
    }
    public fun cancel() {
        on = false
    }
}