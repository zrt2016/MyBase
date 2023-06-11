package com.zrt.kotlinapp.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.one.LeftFragment
import com.zrt.kotlinapp.fragment.one.RightFragment
import kotlinx.android.synthetic.main.activity_f_one.*

/**
 * 动态创建Fragment
 */
class FTwoActivity :  BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun getLayoutResID(): Int = R.layout.activity_f_two

    override fun initData() {
        replaceFragment(R.id.f_two_left_fl, LeftFragment())
        replaceFragment(R.id.f_two_right_fl, RightFragment())

    }

    /**
     * 传入容器ID和要添加的实例
     */
    fun replaceFragment(layoutID:Int, fragment: Fragment){
        val supportFragmentManager = supportFragmentManager
//        开起事务
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(layoutID, fragment)
        // 添加返回栈，先退出fragment在退出activity
//        先退出RightFragment再退出LeftFragment
        beginTransaction.addToBackStack(null)
        beginTransaction.commit()
    }
}