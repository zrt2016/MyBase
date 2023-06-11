package com.zrt.ydhl_phz.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.zrt.ydhl_phz.logic.Repository
import com.zrt.ydhl_phz.logic.model.NurseUser
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author：Zrt
 * @date: 2022/10/29
 */
class LoginViewModel: ViewModel() {
    private val localtionLD = MutableLiveData<NurseUser>()
    var user_number = ""
    var login_password = ""
    val nurseUserLD = Transformations.switchMap(localtionLD){
        user ->
        val params = mapOf("user_number" to user.user_number, "login_password" to user.login_password, "shebei_id" to  user.shebei_id)
        Log.i("","## login param:${params.toString()}")
        // 请求网络，进行登录验证
//        Repository.loginUser(params as HashMap<String, String>)
        Repository.loginUser(user.user_number, user.login_password, user.shebei_id)
//        Repository.loginUser2(user.user_number, user.login_password, user.shebei_id)
    }
    fun setUser(user:NurseUser){
        localtionLD.postValue(user)
    }

    fun doLogin(user_number: String, login_password:String, shebei_id:String){
        this.user_number = user_number
        this.login_password = login_password
        localtionLD.postValue(NurseUser(user_number, login_password, shebei_id))
    }
}