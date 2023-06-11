package com.zrt.kotlinapp.jetpack

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.storage.open
import kotlinx.android.synthetic.main.activity_view_mode.*

/**
 * jetpack组件
 *  ViewModel
 *  添加库依赖：implementation "androidx.lifecycle:lifecycle-extensions:2.1.0"
 *
 *  LiveData：ViewModel中的数据发生变化时，通知Activity进行更新
 *  修改BasicViewModel中的counter为：var counter = MutableLiveData<Int>()
 *  同时初始化其值：
 */
class ViewModeActivity : BasicActivity() {
    lateinit var viewModel: BasicViewModel
    lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_view_mode

    override fun initData() {
        sp = getPreferences(Context.MODE_PRIVATE)
        val count_reserved = sp.getInt("count_reserved", 0)
        // 初始化ViewModel,通过ViewModelProvider获取ViewModel实例,可以保证ViewModel的存活周期大于activity的生命周期
        // ViewModelProvider(传入Activity或Fragment的实例).get(你的ViewModel::class.java)
//        viewModel = ViewModelProvider(this).get(BasicViewModel::class.java)
        viewModel = ViewModelProvider(this, BasicViewModelFactory(count_reserved)).get(BasicViewModel::class.java)
        // 添加Activity生命周期监控
        lifecycle.addObserver(MyObserver(lifecycle))

        plusOneBtn.setOnClickListener {
//            viewModel.counter++
//            viewModel.plusOne()
            viewModel.counter.postValue(viewModel.getCounterValue() + 1)
            viewModel.setcounters((viewModel.counters.value?:0)+1 )
        }
        clearBtn.setOnClickListener {
//            viewModel.counter = 0
            viewModel.clear()
        }
        /**
         * 给viewModel.counter添加监听，如果发生变化，通知activity进行修改
         */
        viewModel.counter.observe(this, Observer {
            count ->
            infoText.setText(count.toString())
        })
        viewModel.counters.observe(this, Observer {
            Log.i("observe", "##viewModel.counters="+viewModel.counters.value)
        })

        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.getUser(userId)
        }
        viewModel.userLV.observe(this, Observer { user ->
            userText.text = user.firstName
        })
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}