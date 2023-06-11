package com.zrt.kotlinapp.activity_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R

class EditTextActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_edit_text
    }

    override fun initData() {
        TODO("Not yet implemented")
    }
}