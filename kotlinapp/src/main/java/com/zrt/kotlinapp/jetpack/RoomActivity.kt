package com.zrt.kotlinapp.jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_room.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class RoomActivity : BasicActivity() {
    lateinit var viewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_room

    override fun initData() {
        viewModel = ViewModelProvider(this, RoomViewModelFactory()).get(RoomViewModel::class.java)
        val userDao = AppDatabase.getDatabase(this).userDao()
        val user1 = UserLV("Tom", "Brady", 40)
        val user2 = UserLV("Tom", "Hanks", 63)
        addDataBtn.setOnClickListener {
            thread {
                userDao.insertUser(user1)
                userDao.insertUser(user2)
            }
        }
        updateDataBtn.setOnClickListener {
            thread {
                user1.age = 42
                userDao.updateUser(user1)
            }
        }
        deleteDataBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }
        queryDataBtn.setOnClickListener {
            showRoomText.setText("")
            thread {
                for (user in userDao.loadAllUsers()){
                    Log.d("RoomActivity", user.toString())
                    runOnUiThread {
                        showRoomText.append(user.toString())
                        showRoomText.append("\n")
                    }
                }

            }
        }
        doWorkBtn.setOnClickListener {
            // BackoffPolicy.LINEAR 线性方式延迟。 BackoffPolicy.EXPONENTIAL重试时间以指数的方式延迟
            // 构造一次性任务
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                    .setInitialDelay(5, TimeUnit.SECONDS) //设置任务在5秒后运行
                    .addTag("simple") // 添加标签，可通过标签取消任务:cancelAllWorkByTag("simple")
                    // 如果doWork()返回Result.retry(),即失败，可通过设置setBackoffCriteria重新进行连接
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS) // 10秒后继续运行
                    .build()
            // 加入到运行队列中
            WorkManager.getInstance(this).enqueue(request)
//            // 周期性运行后台任务请求，设置周期间隔时间15分钟
//            val builder = PeriodicWorkRequest.Builder(SimpleWorker::class.java, 15, TimeUnit.MINUTES).build()
//            添加任务监听
            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(request.id)
                    .observe(this, Observer {
                        workInfo ->
                        if(workInfo.state == WorkInfo.State.SUCCEEDED){
                            Log.i("WorkManager", "WorkInfo.State.SUCCEEDED")
                        }else if(workInfo.state == WorkInfo.State.FAILED){
                            Log.i("WorkManager", "WorkInfo.State.FAILED")
                        }
                    })
        }
        cancelWorkBtn.setOnClickListener {
            // 通过添加的标签，取消任务
            WorkManager.getInstance(this).cancelAllWorkByTag("simple")
            // 也可以通过请求id取消任务
//            WorkManager.getInstance(this).cancelWorkById(request.id)
            // 取消所有任务
//            WorkManager.getInstance(this).cancelAllWork()
        }

    }
}