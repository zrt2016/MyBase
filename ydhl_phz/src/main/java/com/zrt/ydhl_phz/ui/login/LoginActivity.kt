package com.zrt.ydhl_phz.ui.login

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zrt.ydhl_phz.BasicActivity
import com.zrt.ydhl_phz.ui.MainActivity
import com.zrt.ydhl_phz.R
import com.zrt.ydhl_phz.tools.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

class LoginActivity : BasicActivity() {
    val viewModel:LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_login

    override fun initData() {
        a_l_username_edit.setText("007")
        a_l_userpassword_edit.setText("123")
        login_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                MainActivity.actionIntent(this@LoginActivity)
//                viewModel.doLogin(a_l_username_edit.text.toString(),
//                        a_l_userpassword_edit.text.toString(), getDeviceID()?:"1")
            }
        })
        viewModel.nurseUserLD.observe(this, Observer { result ->
            // 登录校验完成
            Log.i(">>>>","##result=$result")
            val login = result.getOrNull()
            if (login != null) {
                when (login.error) {
                    "200" -> {
                        ToastUtils.show("登录成功！")
                        val checkBingqu = login.user_suoshu_bingqu_name.split("|", limit = 2)
                        login.current_bingqu_name = checkBingqu[0]
                        login.current_bingqu_id = if (checkBingqu.size > 1){
                            checkBingqu[1]
                        }else {
                            ""
                        }
                        currentApplication.currentNurseUser = login
                        MainActivity.actionIntent(LoginActivity@this)
                    }
                    "20018" -> {
                        ToastUtils.show("该用户已在其他终端登录，请退出终端后重试，或等待系统自动将其他终端上的登录退出后再试！")
                    }
                    "20001" -> ToastUtils.show("error:${login.error}, msg=${login.msg}")
                    else -> {
                        ToastUtils.show("Error:${login.error} ${login.msg}")
                    }
                }
            } else {
                ToastUtils.show("登录异常！")
            }
        })
    }

    @SuppressLint("MissingPermission", "WrongConstant", "HardwareIds")
    fun getDeviceID(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) // C800设备Android10系统，暂只能获取使用AndroidID
        }
        return /*if ("C568+" == Constants.APP_MODEL) {
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } else*/ Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        //		return ((TelephonyManager) getSystemService("phone")).getDeviceId();
        // 20210407 设备号统一使用 ANDROID_ID
    }
}