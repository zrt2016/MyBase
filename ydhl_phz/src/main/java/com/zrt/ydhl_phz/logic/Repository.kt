package com.zrt.ydhl_phz.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.zrt.ydhl_phz.logic.model.NurseUser
import com.zrt.ydhl_phz.logic.network.PHZNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.ResponseBody
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * @author：Zrt
 * @date: 2022/10/29
 */
object Repository {
//    fun loginUser(params:HashMap<String, String>)
    fun loginUser(user_number:String, login_password:String, shebei_id:String)
        = fire<NurseUser>(Dispatchers.IO){
        val loginResponse = PHZNetwork.checkLogin(user_number, login_password, shebei_id)
        Log.i("","## login2 ${loginResponse.toString()}")
        if (loginResponse != null){
//        if ("200".equals(loginResponse.error)){
            Result.success(loginResponse)
//        }else if ("20018".equals(loginResponse.error)){
//            Result.success(loginResponse)
        }else {
            Result.failure(RuntimeException("response status is null"))
        }
    }
    fun loginUser2(user_number:String, login_password:String, shebei_id:String)
            = fire<String>(Dispatchers.IO){
        val loginResponse = PHZNetwork.checkLogin2(user_number, login_password, shebei_id)
        Log.i("","## login2 ${loginResponse.toString()}")

        if (loginResponse != null){
//        if ("200".equals(loginResponse.error)){
            Result.success(loginResponse.string())
//        }else if ("20018".equals(loginResponse.error)){
//            Result.success(loginResponse)
        }else {
            Result.failure(RuntimeException("response status is null"))
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Log.i("", "##login exception:${e.toString()}")
                Result.failure<T>(e)
            }
            emit(result)
        }
}