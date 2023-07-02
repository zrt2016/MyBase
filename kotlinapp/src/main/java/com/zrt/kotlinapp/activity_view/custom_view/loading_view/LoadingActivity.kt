package com.zrt.kotlinapp.activity_view.custom_view.loading_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R

class LoadingActivity: BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_loading;
    }

    override fun initData() {

    }


}