package com.zrt.kotlinapp.jetpack

import android.util.Log
import androidx.lifecycle.*
import com.zrt.kotlinapp.learnkotlin.log

/**
 * @author：Zrt
 * @date: 2022/8/4
 */

/**
 * ViewModel推荐做法，只暴露不可变的liveData
 */
class BasicViewModel(countReserved:Int) : ViewModel(){
//    var counter = countReserved
    val counter = MutableLiveData<Int>()
    // 不暴露可变的livedata number
    val counters:LiveData<Int> get() = _number
    private val _number = MutableLiveData<Int>()
    init {
        counter.value = countReserved
        _number.value = countReserved
    }
    fun plusOne(){
        val count = counter.value ?: 0
        counter.value = count + 1
//         val counts = _number.value ?: 0
//        _number.value = counts + 1
    }
    fun getCounterValue(): Int{
        return counter.value ?: 0
    }
    fun clear(){
        counter.value = 0
    }
    fun setcounters(i:Int){
        _number.postValue(i)
    }
    // map和switchMap
    private val userLiveData = MutableLiveData<UserLV>()
    // map将UserLV类型的LiveData转换为任意其他类型的LiveData
    // 此处将user转换为一个字符串
    val userName:LiveData<String> = Transformations.map(userLiveData){
        user ->
        "${user.firstName} ${user.lastName}"
    }
//    fun getUser(userId: String):LiveData<UserLV>{
//        // 每次调用都是返回一个新的LiveData，而userLiveData一直都是老的LiveData，因此无法观察数据的变化
//        return RepositoryLV.getUser(userId)
//    }
    // switchMap
    private val userIdLiveData = MutableLiveData<String>()
    val userLV:LiveData<UserLV> = Transformations.switchMap(userIdLiveData){
        userID -> RepositoryLV.getUser(userID)
    }
    // 每次调用getUser，都会userLV.switchMap执行，并调用我们编写的转换函数
    fun getUser(userId: String){
        userIdLiveData.value = userId
    }
}

/**
 * 向ViewModel传递参数
 */
class BasicViewModelFactory(val countReserved:Int): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BasicViewModel(countReserved) as T
    }
}

/**
 * 感知Activity的生命周期
 * activity中添加：
 * lifecycle.addObserver(MyObserver())
 *
 * lifecycle:可以通过lifecycle.currentState随时获取当前生命周期状态
 * 分别有5种：INITIALIZED、CREATED、STARTED、RESUMED、DESTROYED
 * 例：如下，一个activity的开起和关闭
 * activityCreate ：CREATED
 * activityStart ：STARTED
 * activityResume ：RESUMED
 * activityPause ：STARTED
 * activityStop ：CREATED
 * activityDestory ：DESTROYED
 */
class MyObserver(val lifecycle: Lifecycle): LifecycleObserver{
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun activityCreate(){
        Log.i("MyObserver", "activityCreate ："+lifecycle.currentState)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart(){
        Log.i("MyObserver", "activityStart ："+lifecycle.currentState)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun activityResume(){
        Log.i("MyObserver", "activityResume ："+lifecycle.currentState)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun activityPause(){
        Log.i("MyObserver", "activityPause ："+lifecycle.currentState)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop(){
        Log.i("MyObserver", "activityStop ："+lifecycle.currentState)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun activityDestory(){
        Log.i("MyObserver", "activityDestory ："+lifecycle.currentState)
    }
}

/**
 * 模仿实际使用LiveData
 * 实际使用中LiveData的对象实例不一定一直都是在ViewModel中创建
 */
object RepositoryLV{
    // 每次调用返回都是一个新的LiveData
    fun getUser(userId:String):LiveData<UserLV>{
        val liveData = MutableLiveData<UserLV>()
        liveData.value = UserLV(userId, userId, 10)
        return liveData
    }
}