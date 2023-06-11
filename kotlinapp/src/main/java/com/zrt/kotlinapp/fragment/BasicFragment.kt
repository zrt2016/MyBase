package com.zrt.kotlinapp.fragment

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author：Zrt
 * @date: 2022/6/19
 * 声明周期启动：
 *  onAttach --> onCreate --> onCreateView --> onActivityCreated --> onStart --> onResume
 *  销毁：
 *  onPause --> onStop --> onDestroyView --> onDestroy --> onDetach
 *  如果开起返回栈，或者未被销毁会执行以下流程：
 *  onPause --> onStop --> onDestroyView
 *  未被销毁，重新启动：
 *  onCreateView --> onActivityCreated --> onStart --> onResume
 */
open class BasicFragment :Fragment(){
    companion object{
        var Tag = "BasicFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(Tag, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(Tag, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(Tag, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(Tag, "onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.i(Tag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(Tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(Tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(Tag, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(Tag, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Tag, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(Tag, "onDetach")
    }

}