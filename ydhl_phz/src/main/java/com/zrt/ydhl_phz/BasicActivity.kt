package com.zrt.ydhl_phz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.zrt.ydhl_phz.utils.ActivityCollector

abstract class BasicActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_basic)
//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
//    }
    lateinit var currentApplication:MyApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        ActivityCollector.addActivity(this)
        currentApplication = application as MyApplication
        Log.i("BasicActivity","taskID=$taskId")// 打印Activity 栈ID
        // javaClass等价于java中的getClass()
        Log.i("BasicActivity", "activity=${javaClass.simpleName}") // 打印当前activity
        initData()
    }
    @LayoutRes
    abstract fun getLayoutResID(): Int
    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    /**
     * 退出APP
     */
    fun exitApp(){
        ActivityCollector.finishAll()
        // 杀掉当前进程的代码，保证程序的完全退出
        // 仅用于杀掉当前进程
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 获取上一个activity通过setResult返回的数据
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 重写返回键方法，点击back键返回时，也带入返回值
     */
    override fun onBackPressed() {
        // 重写时，注掉super.onBackPressed()
        super.onBackPressed()

    }

    /**
     * Activity在被回收前会调用该方法，可在该处保存相关数据，等下次进入时带入该数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}