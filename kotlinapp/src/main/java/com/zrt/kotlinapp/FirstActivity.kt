package com.zrt.kotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_first
    }

    override fun initData() {
        btn1.setOnClickListener {
            Toast.makeText(this, "You clicked button 1", Toast.LENGTH_SHORT).show()
        }
    }
}