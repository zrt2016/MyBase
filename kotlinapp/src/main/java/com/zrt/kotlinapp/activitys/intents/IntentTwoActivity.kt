package com.zrt.kotlinapp.activitys.intents

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R

class IntentTwoActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayoutResID(): Int {
        return R.layout.activity_intent_two
    }

    override fun initData() {

    }
}