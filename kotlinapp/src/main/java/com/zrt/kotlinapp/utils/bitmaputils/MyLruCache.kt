package com.zrt.kotlinapp.utils.bitmaputils

import android.annotation.SuppressLint
import android.content.ComponentCallbacks2

/**
 * @author：Zrt
 * @date: 2023/4/23
 */
class MyLruCache {
}

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * time  : 2020/8/6
 * desc  : LruCache
 * revise:
</pre> *
 */
//class SystemLruCache<K, V>(maxSize: Int) : ILruCache<K, V> {
//    /**
//     * LRU 控制
//     */
//    private var map: LinkedHashMap<K, V>
//
//    /**
//     * 当前缓存占用
//     */
//    private var size = 0
//
//    /**
//     * 最大缓存容量
//     */
//    private var maxSize: Int
//    // 以下属性用于数据统计
//    /**
//     * 设置数据次数
//     */
//    private var putCount = 0
//
//    /**
//     * 创建数据次数
//     */
//    private var createCount = 0
//
//    /**
//     * 淘汰数据次数
//     */
//    private var evictionCount = 0
//
//    /**
//     * 缓存命中次数
//     */
//    private var hitCount = 0
//
//    /**
//     * 缓存未命中数
//     */
//    private var missCount = 0
//    override fun resize(maxSize: Int) {
//        require(maxSize > 0) { "maxSize <= 0" }
//        synchronized(this) { this.maxSize = maxSize }
//        trimToSize(maxSize)
//    }
//
//    /**
//     * 具体来说，判断map中是否含有key值value值，若存在，则hitCount（击中元素数量）自增，并返回Value值，
//     * 若没有击中，则执行create(key)方法，这里看到create方法是一个空的实现方法，返回值为null，
//     * 所以可以重写该方法，在调用get（key）的时候若没有找到value值，则自动创建一个value值并压入map中。
//     * @param key   key
//     * @return
//     */
//    override fun get(key: K): V {
//        if (key == null) {
//            throw NullPointerException("key == null")
//        }
//        var mapValue: V?
//        synchronized(this) {
//            mapValue = map[key]
//            if (mapValue != null) {
//                hitCount++
//                return mapValue as V
//            }
//            missCount++
//        }
//        val createdValue = create(key) ?: return null
//        synchronized(this) {
//            createCount++
//            mapValue = map.put(key, createdValue)
//            if (mapValue != null) {
//                // There was a conflict so undo that last put
//                map.put(key, mapValue)
//            } else {
//                size += safeSizeOf(key, createdValue)
//            }
//        }
//        return if (mapValue != null) {
//            entryRemoved(false, key, createdValue, mapValue)
//            mapValue
//        } else {
//            trimToSize(maxSize)
//            createdValue
//        }
//    }
//
//    /**
//     * 插入一个键值对数据
//     * @param key   key
//     * @param value value
//     * @return  V数据
//     */
//    override fun put(key: K, value: V): V {
//        if (key == null || value == null) {
//            throw NullPointerException("key == null || value == null")
//        }
//        var previous: V
//        synchronized(this) {
//            putCount++
//            size += safeSizeOf(key, value)
//            //这里判断map.put方法的返回值是否为空
//            previous = map.put(key, value)
//            if (previous != null) {
//                //如果不为空的话，则说明我们刚刚并没有将我么你的键值对压入Map中，所以这里的size需要自减；
//                size -= safeSizeOf(key, previous)
//            }
//        }
//
//        //这里判断previous是否为空，如果不为空的话，调用了一个空的实现方法entryRemoved()
//        //也就是说我们可以实现自己的LruCache并在添加缓存的时候若存在该缓存可以重写这个方法；
//        if (previous != null) {
//            entryRemoved(false, key, previous, value)
//        }
//        trimToSize(maxSize)
//        return previous
//    }
//
//    /**
//     * 自动淘汰数据
//     * 该方法主要是判断该Map的大小是否已经达到阙值，若达到，则将Map队尾的元素（最不常使用的元素）remove掉。
//     * @param maxSize   最大值
//     */
//    private fun trimToSize(maxSize: Int) {
//        // 淘汰数据直到不超过最大容量限制
//        //循环遍历
//        while (true) {
//            var key: K
//            var value: V
//            synchronized(this) {
//
//                //如果为空，则抛出异常
//                check(!(size < 0 || map.isEmpty() && size != 0)) {
//                    (javaClass.name
//                            + ".sizeOf() is reporting inconsistent results!")
//                }
//                // 不超过最大容量限制，跳出
//                if (size <= maxSize) {
//                    break
//                }
//
//                //取最早的数据
//                var toEvict: Map.Entry<K, V>? = null
//                val entries: Set<Map.Entry<K, V>> = map.entrySet()
//                for (entry in entries) {
//                    toEvict = entry
//                    break
//                }
//                // toEvict 为 null 说明没有更多数据
//                if (toEvict == null) {
//                    break
//                }
//                key = toEvict.key
//                value = toEvict.value
//                //移除
//                map.remove(key)
//                //逐次减去1，减去旧 Value 内存占用
//                size -= safeSizeOf(key, value)
//                //统计淘汰计数
//                evictionCount++
//            }
//
//            //数据移除回调（value -> null）
//            entryRemoved(true, key, value, null)
//        }
//    }
//
//    override fun remove(key: K): V {
//        if (key == null) {
//            throw NullPointerException("key == null")
//        }
//        var previous: V
//        synchronized(this) {
//            previous = map.remove(key)
//            if (previous != null) {
//                size -= safeSizeOf(key, previous)
//            }
//        }
//        if (previous != null) {
//            entryRemoved(false, key, previous, null)
//        }
//        return previous
//    }
//
//    /**
//     * 这里是参考glide中的lru缓存策略，低内存的时候清除
//     * @param level             level级别
//     */
//    fun trimMemory(level: Int) {
//        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
//            // Entering list of cached background apps
//            // Evict our entire bitmap cache
//            clearMemory()
//        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN || level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {
//            // The app's UI is no longer visible, or app is in the foreground but system is running
//            // critically low on memory
//            // Evict oldest half of our bitmap cache
//            trimToSize(maxSize() / 2)
//        }
//    }
//
//    /**
//     * 清除掉所有的内存数据
//     */
//    fun clearMemory() {
//        trimToSize(0)
//    }
//
//    override fun containsKey(key: K): Boolean {
//        return map.containsKey(key)
//    }
//
//    override fun keySet(): Set<K>? {
//        return map.keySet()
//    }
//
//    protected fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?) {}
//    protected fun create(key: K): V? {
//        return null
//    }
//
//    /**
//     * 测量数据单元的内存占用
//     * @param key
//     * @param value
//     * @return
//     */
//    private fun safeSizeOf(key: K, value: V): Int {
//        // 如果开发者重写的 sizeOf 返回负数，则抛出异常
//        val result = sizeOf(key, value)
//        check(result >= 0) { "Negative size: $key=$value" }
//        return result
//    }
//
//    /**
//     * 测量缓存单元的内存占用
//     * @param key       key
//     * @param value     value
//     * @return
//     */
//    protected fun sizeOf(key: K, value: V): Int {
//        return 1
//    }
//
//    fun evictAll() {
//        // -1 will evict 0-sized elements
//        trimToSize(-1)
//    }
//
//    @Synchronized
//    override fun size(): Int {
//        return size
//    }
//
//    @Synchronized
//    override fun maxSize(): Int {
//        return maxSize
//    }
//
//    @Synchronized
//    fun hitCount(): Int {
//        return hitCount
//    }
//
//    @Synchronized
//    fun missCount(): Int {
//        return missCount
//    }
//
//    @Synchronized
//    fun createCount(): Int {
//        return createCount
//    }
//
//    @Synchronized
//    fun putCount(): Int {
//        return putCount
//    }
//
//    @Synchronized
//    fun evictionCount(): Int {
//        return evictionCount
//    }
//
//    @Synchronized
//    fun snapshot(): Map<K, V> {
//        return LinkedHashMap(map)
//    }
//
//    @Synchronized
//    override fun clear() {
//        map.clear()
//    }
//
//    @SuppressLint("DefaultLocale")
//    @Synchronized
//    override fun toString(): String {
//        val accesses = hitCount + missCount
//        val hitPercent = if (accesses != 0) 100 * hitCount / accesses else 0
//        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
//                maxSize, hitCount, missCount, hitPercent)
//    }
//
//    /**
//     * 由于缓存空间不可能设置无限大，所以开发者需要在构造方法中设置缓存的最大内存容量 maxSize。
//     * @param maxSize       最大值
//     */
//    init {
//        require(maxSize > 0) { "maxSize <= 0" }
//        this.maxSize = maxSize
//        //创建map集合
//        //第一个参数：
//        //第二个参数：
//        //第三个参数：
//        // 创建 LinkedHashMap 对象，并使用 LRU 排序模式
//        map = LinkedHashMap(0, 0.75f, true)
//    }
//}

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * time  : 2020/8/6
 * desc  : 缓存接口
 * revise: 内存缓存，主要使用到了淘汰算法
</pre> *
 */
interface ILruCache<K, V> {
    /**
     * 重制大小
     *
     * @param maxSize 大小
     */
    fun resize(maxSize: Int)

    /**
     * 获取元素
     *
     * @param key key
     * @return V
     */
    operator fun get(key: K): V

    /**
     * 插入元素
     *
     * @param key   key
     * @param value value
     * @return
     */
    fun put(key: K, value: V): V

    /**
     * 移除元素
     *
     * @param key key
     * @return V
     */
    fun remove(key: K): V

    /**
     * 判断是否包含key元素
     *
     * @param key key
     * @return V
     */
    fun containsKey(key: K): Boolean

    /**
     * 获取所有元素的键
     *
     * @return set集合
     */
    fun keySet(): Set<K>?

    /**
     * 最大值
     *
     * @return size
     */
    fun maxSize(): Int

    /**
     * 当前缓存大小
     *
     * @return size
     */
    fun size(): Int

    /**
     * 清除缓存中所有的内容
     */
    fun clear()
}