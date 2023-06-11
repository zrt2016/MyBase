package com.zrt.ydhl_phz.logic.network

import android.util.Log
import com.zrt.ydhl_phz.logic.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author：Zrt
 * @date: 2022/10/31
 */
object PHZNetwork {
    // user_number:String, login_password:String, shebei_id:String
    val loginService = ServiceCreator.create(LoginService::class.java)
//    suspend fun checkLogin(params:HashMap<String, String>)
//        = loginService.checkLogin(params).await();
    suspend fun checkLogin(user_number:String, login_password:String, shebei_id:String)
        = loginService.checkLogin(user_number, login_password, shebei_id).await();

    suspend fun checkLogin2(user_number:String, login_password:String, shebei_id:String)
            = loginService.checkLogin2(user_number, login_password, shebei_id).await();

    suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {
            continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    Log.i(">>>>", "## login response =${body?.toString()?: " result null"}")
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}