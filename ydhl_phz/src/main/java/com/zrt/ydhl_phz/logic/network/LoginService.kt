package com.zrt.ydhl_phz.logic.network

import com.zrt.ydhl_phz.logic.model.NurseUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author：Zrt
 * @date: 2022/10/31
 */
interface LoginService {
    @FormUrlEncoded
    @POST("Mobile/ClientCommunication/checkLogin")
//    fun checkLogin(@Body user_number:String, login_password:String, shebei_id:String):Call<NurseUser>
//    fun checkLogin(@Body params:HashMap<String, String>):Call<NurseUser>
//    fun checkLogin(@FieldMap params:HashMap<String, String>):Call<NurseUser>
    fun checkLogin(@Field("user_number") user_number:String,
                   @Field("login_password") login_password:String,
                   @Field("shebei_id") shebei_id:String):Call<NurseUser>
    // 返回json数据
    @FormUrlEncoded
    @POST("Mobile/ClientCommunication/checkLogin")
    fun checkLogin2(@Field("user_number") user_number:String,
                    @Field("login_password") login_password:String,
                    @Field("shebei_id") shebei_id:String):Call<ResponseBody>

}