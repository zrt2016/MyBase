package com.zrt.kotlinapp.fragment.one

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.BasicFragment

/**
 * @author：Zrt
 * @date: 2022/6/18
 */
class LeftFragment: BasicFragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_one_left, container, false)
    }
}