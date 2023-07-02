package com.zrt.kotlinapp.activity_view.custom_view.loading_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_loading.*

class LoadingActivity: BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_loading)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_loading;
    }

    override fun initData() {
        a_load_bounce.setOnClickListener {
            a_load_bounce.visibility = View.INVISIBLE
        }
        val lp = a_load_frame_bounce.layoutParams as LinearLayout.LayoutParams
        lp.gravity = Gravity.CENTER
        a_load_frame_bounce.layoutParams = lp
        BounceLoadingView.attach(a_load_frame_bounce)

    }

}