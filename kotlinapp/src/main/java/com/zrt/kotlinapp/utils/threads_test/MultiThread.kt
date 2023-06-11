package com.zrt.kotlinapp.utils.threads_test

import android.os.AsyncTask
import java.lang.Exception
import java.util.*
import java.util.concurrent.*
import kotlin.Comparator


/**
 * @author：Zrt
 * @date: 2022/10/9
 * 多线程
 * 线程中的阻塞队列
 * 1、ArrayBlockingQueue：数组实现的有界队列
 * 2、LinkedBlockingQueue：基于链表实现的阻塞队列
 * 3、PriorityBlockingQueue：支持优先级的无界队列，默认采取自然顺序升序排列
 * 4、DelayQueue：支持延时获取元素的无界队列，队列使用PriorityQueue实现
 * 5、SynchronousQueue：不存储元素的阻塞队列，每个插入操作，必须等待另一个线程的移除操作
 * 6、LinkedTransferQueue：链表结构组成的无界阻塞队列，
 * 7、LinkedBlockingDeque：由链表组成的双向阻塞队列。可以从对象两端插入和移出元素。
 *
 * 线程池
 * ThreadPoolExecutor
 */

fun blockingQueue(){
    /**
     * 数组实现的有界阻塞队列，先进先出。容量满，阻塞进队操作，容量空，阻塞出队操作
     * 参数一、队列容量：2000，
     * 参数二、公平队列：true，多个生产线程时，先阻塞的生产线程，优先往队列插入数据。
     */
    val arrayBlockingQueue = ArrayBlockingQueue<String>(2000, true)
    arrayBQ()
    /**
     * 基于链表的阻塞队列，先进先出，该队列有链表构成
     * 无参时，默认队列容量为Integer.MAX_VALUE
     * 优势：生产端和消费端分包采用独立的锁控制数据同步，因此在高并发的情况下生产者和消费者可以并行操作队列中的数据
     * 参数一、队列容量：10，
     */
    val linkedBlockingQueue = LinkedBlockingQueue<String>()
    val linkedBlockingQueue2 = LinkedBlockingQueue<String>(10)
    /**
     * 支持优先级的无界队列，默认采取自然顺序升序排列，可自定义compareTo()方法指定元素排序
     * 参数一、队列容量：10，
     * 参数二：排序规则
     */
    val priorityBlockingQueue = PriorityBlockingQueue<Int>(10, object : Comparator<Int> {
        override fun compare(o1: Int, o2: Int): Int {
            return o1.compareTo(o2)
        }
    })

    /**
     * DelayQueue：支持延时获取元素的无界队列，队列使用PriorityQueue实现
     * 队列元素必须实现Delayed接口
     */
    val delayQueue = DelayQueue<MyDelay<String>>()

    /**
     * 不存储元素的阻塞队列，每个插入操作，必须等待另一个线程的移除操作
     */
    val synchronousQueue = SynchronousQueue<String>()

    /**
     * 由链表结构组成的无界阻塞队列，
     * 重要的3个方法：
     * transfer()：存在一个正在等待获取的消费者线程，则立刻传递元素给该消费者线程。没有则将元素插入到队列尾部，
     *          并且进入阻塞状态，直到其他线程取走该元素。待消费者接收后返回。
     * tryTransfer(E e) : 存在一个正在等待获取的消费者线程，则立刻传递元素给该消费者线程，并返回false，
     *          且不进入队列，这是一个不阻塞操作。该方法无论消费者是否接收，都会立即返回
     * tryTransfer(E e,long timeout,TimeUnit unit): 存在等待的消费者线程，立即传递元素给该消费者。
     *          不存在则将元素插入队列尾部，等待消费者线程获取该元素。若超时后元素未被获取，则返回false。
     */
    val linkedTransferQueue = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        LinkedTransferQueue<String>()
    } else {
        null
    }
    /**
     * 参数一：容量，无参时，默认值为Integer.MAX_VALUE
     * 由链表组成的双向阻塞队列。可以从对象两端插入和移出元素。
     * 比其他多了方法：addFirst、addLast、offerFirst、offerLast、peekFirst、peekLast等方法
     * first结尾的分别表示插入、获取和移除双端队列的第一个元素。last则时插入、获取和移除双端队列的最后一个元素
     */
    val linkedBlockingDeque = LinkedBlockingDeque<String>()
}

/**
 * ArrayBlockingQueue 阻塞队列实现原理
 */
fun arrayBQ() {
    val arrayBlockingQueue = ArrayBlockingQueue<String>(10, true)
    // 添加元素，如果队列元素已满，则调用notFull.await()进行等待
    // 队列未满，则调用enqueue(e)插入元素(插入到队尾putIndex位置),同时调用notEmpty.signal()方法唤醒等待的消费线程
    // 当putIndex为队列长度值时，归0，重新开始
    arrayBlockingQueue.put("A")
    // 获取元素，如果队列为空，则调用notEmpty.await()进行等待
    // 队列不为空，则调用dequeue()方法获取第一个元素（即takeIndex堆首元素），同时调用notFull.signal()唤醒等待的生成现场
    arrayBlockingQueue.take()
    /**
     * 长度为3的队列，插入和弹出过程(n代表空值)
     * takeIndex:队首下标。putIndex：队尾下标
     * 下标：      0---1---2   putIndex    takeIndex
     * put(1):    1---n---n      0->1         0
     * put(2):    1---2---n      1->2         0
     * put(3):    1---2---3      2->0         0
     * take():    n---2---3      0           0->1
     * take():    n---n---3      0           1->2
     * take():    n---n---n      0           2->0
     * put(4):    4---n---n      0->1         0
     */
}

/**
 * @param delayTime:延迟时间
 * @param expire:过期时间 = 当前时间+延迟时间
 * @param data:数据
 */
class MyDelay<T>(var delayTime : Long=0, var expire : Long = 0, var data: T) : Delayed{
    // 优先级规则：两个任务比较，时间短的优先执行
    override fun compareTo(other: Delayed?): Int {
        val l = this.getDelay(TimeUnit.MILLISECONDS) - (other?.getDelay(TimeUnit.MILLISECONDS) ?: 0)
        return l.toInt()
    }
    // 剩余时间 = 到期时间 - 当前时间
    override fun getDelay(unit: TimeUnit?): Long {
        return unit?.convert(this.expire-System.currentTimeMillis(), TimeUnit.MILLISECONDS) ?: 0
    }
}

/**
 * 使用Object.wait()和Object.notify()和非阻塞队列实现生产者-消费者模式
 */
class BlockNotTest{
    var queueSize = 10
    var queue:PriorityQueue<Int> = PriorityQueue<Int>(queueSize)
    @Volatile
    var isRun:Boolean = true
    var lock = queue as Object
    fun main(){
        val producer = this.Producer()
        val consumer = this.Consumer()
        producer.start()
        consumer.start()
    }
    // 消费者
    inner class Consumer: Thread(){
        override fun run() {
            super.run()
            while (isRun){
                synchronized(lock){
                    while (queue.size == 0){
                        try {
                            println("队列空，等待数据")
                            lock.wait()
                            println("空队列有数据了，移除数据")
                        }catch (ex:Exception){
                            lock.notify()
                        }
                    }
                    queue.poll() // 移走堆首元素
                    println("消费者：${queue.size}")
                    if (isRun) {
                        lock.notify()
                    }else{
                        lock.notifyAll()
                    }
                }
            }
            println("消费者结束")
        }
    }
    // 生产者
    inner class Producer:Thread(){
        override fun run() {
            super.run()
            while (isRun){
                synchronized(lock){
                    while(queue.size == queueSize){
                        try {
                            println("队列满，等待空余空间")
                            lock.wait()
                            println("唤醒队列，队列有空余空间了")
                        }catch (ex:Exception) {
                            lock.notify()
                        }
                    }
                    queue.offer(1) // 插入一个元素
                    println("生产者：${queue.size}")
                    if (isRun) {
                        lock.notify()
                    }else{
                        lock.notifyAll()
                    }
                }
            }
            println("生产者结束")
        }
    }
}
/**
 * 使用阻塞队列实现生产者-消费者模式
 * 优点：无需单独考虑同步和线程间通信的问题
 */
class BlockTest{
    var queueSize = 10
    var queue: ArrayBlockingQueue<Int> = ArrayBlockingQueue<Int>(queueSize)
    @Volatile
    var isRun:Boolean = true
    fun main(){
        val producer = this.Producer()
        val consumer = this.Consumer()
        producer.start()
        consumer.start()
    }
    // 消费者
    inner class Consumer: Thread(){
        override fun run() {
//            super.run()
            while (isRun){
                try {
                    queue.take()
                }catch (ex:Exception){
                    println("消费者 error="+ex.toString())
                }
            }
        }
    }
    // 生产者
    inner class Producer:Thread(){
        override fun run() {
//            super.run()
            while (isRun){
                try {
                    queue.put(1)
                }catch (ex:Exception){
                    println("生产者 error="+ex.toString())
                }
            }
        }
    }
}

/*
 * 线程池
 * 线程池处理流程
 *                      创建核心线程                  添加到任务队列                 创建非核心线程
 *                          ↑ 否             是          ↑ 否           是               ↑ 否                    是
 * 提交任务 -----> 线程数是否达到corePoolSize -----> 校验任务队列是否满 ------> 校验是否达到最大线程数maximumPoolSize -----> 执行饱和策略
 *
 * 四种常用的线程池
 *  1、FixedThreadPool：
 *  2、CachedThreadPool：
 *  3、SingleThreadExecute：
 *  4、ScheduledThreadPool：
 */
fun threadPool(){
    /**
     * int corePoolSize, 核心线程数，
     * int maximumPoolSize, 线程池允许创建最大线程数，分别为：核心线程和非核心线程
     * long keepAliveTime, 非核心线程闲置的超时时间，超过则回收。allowCoreThreadTimeOut=true，会作用到核心线程
     * TimeUnit unit, keepAliveTime参数的时间单位
     * BlockingQueue<Runnable> workQueue, 任务队列，如果当前线程数大于corePoolSize，则将任务添加到此任务队列
     * ThreadFactory threadFactory, 线程工厂，可以给每个创建出来的线程设置名字
     * RejectedExecutionHandler handler 饱和策略，队列和线程池都满了时，采取的对应策略，默认AbordPolicy，无法处理，并抛出异常。此外还有3中策略。
     */
    val threadPoolExecutor = ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, ArrayBlockingQueue<Runnable>(100))
    /*
     * 1、FixedThreadPool：
     */
    val newFixedThreadPool = Executors.newFixedThreadPool(10)
    newFixedThreadPool.execute(object : Runnable{
        override fun run() {
            println("start newFixedThreadPool")
        }
    })
    val newCachedThreadPool = Executors.newCachedThreadPool()
    newCachedThreadPool.execute(object : Runnable{
        override fun run() {
            println("start newCachedThreadPool")
        }
    })
    val newSingleThreadExecutor = Executors.newSingleThreadExecutor()
    newCachedThreadPool.execute(object : Runnable{
        override fun run() {
            println("start newSingleThreadExecutor")
        }
    })
    val newScheduledThreadPool = Executors.newScheduledThreadPool(4)
    // 3秒后执行
    newScheduledThreadPool.schedule(object : Runnable{
        override fun run() {
            println("start newScheduledThreadPool3")
        }
    }, 3, TimeUnit.SECONDS)
    newScheduledThreadPool.schedule(object : Runnable{
        override fun run() {
            println("start newScheduledThreadPool6")
            // 结束线程
            newScheduledThreadPool.shutdown()
        }
    }, 6, TimeUnit.SECONDS)
    /**
     * 实现周期线程
     * initialDelay：线程第一次执行任务延迟时间
     * period：两个连续线程之间的周期
     */
    newScheduledThreadPool.scheduleAtFixedRate(object : Runnable{
        override fun run() {
            println("start newScheduledThreadPool2")
        }
    }, 1, 2, TimeUnit.SECONDS)
    // 结束线程
//    newScheduledThreadPool.shutdown()
//    AsyncTask().execut
}

fun main(){
    // Object非阻塞队列
//    val blockNotTest = BlockNotTest()
//    blockNotTest.main()
//    val timer = Timer()
//    val timerTask = object : TimerTask(){
//        override fun run() {
//            blockNotTest.isRun = false
//        }
//    }
//    // 阻塞队列实现
//    timer.schedule(timerTask, 1)
//    val blockTest = BlockTest()
//    blockTest.main()
//    timer.schedule(object :TimerTask(){
//        override fun run() {
//            blockTest.isRun = false
//        }
//    }, 1)

    // 线程池
    threadPool()
}
class MultiThread{

}