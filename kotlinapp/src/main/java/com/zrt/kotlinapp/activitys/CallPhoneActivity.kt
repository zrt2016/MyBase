package com.zrt.kotlinapp.activitys

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import com.zrt.permissionlibrary.PermissionX
import kotlinx.android.synthetic.main.activity_call_phone.*
import java.lang.Exception

class CallPhoneActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_call_phone

    override fun initData() {
        makeCallBtn.setOnClickListener {
            PermissionX.request(this, Manifest.permission.CALL_PHONE){
                allGranted, deniedList ->
                if (allGranted){
                    call()
                }else{
                    ToastUtils.show("You denied $deniedList")
                }
            }
        }
    }

    private fun call() {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:10086")
            startActivity(intent)
        }catch (ex:Exception){
            ToastUtils.show("call error=${ex.toString()}")
        }
    }
}