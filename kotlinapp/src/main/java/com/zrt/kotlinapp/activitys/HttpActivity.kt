package com.zrt.kotlinapp.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.learnkotlin.log
import com.zrt.kotlinapp.utils.https.*
import kotlinx.android.synthetic.main.activity_http.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

/**
 * Android9.0开始 应用程序默认使用HTTPS类型的网络请求，Http类型的网络请求存在安全隐患默认不在支持
 * 因此需要添加如下配置：AndroidManifest.xml
 * <application
    ...
    android:networkSecurityConfig="@xml/network_config">
 */
class HttpActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_http

    override fun initData() {

    }

    fun onClick(view:View){
        when(view.id){
            R.id.a_http ->{
                thread {
                    connectGetHttpDemo(this@HttpActivity){
                        result ->
                        runOnUiThread{
                            a_http_text.setText(result)
                        }
                    }

                }
            }
            R.id.a_okhttp ->{
                thread {
                    connectGetOkhttpDemo(){
                        result ->
                        runOnUiThread{
                            a_okhttp_text.setText(result)
                        }
                    }

                }
            }
            R.id.a_retrofit_get ->{
                val retrofit = Retrofit.Builder()
                        .baseUrl(BAIDU_URL) // 指定所有请求的根路径
                        .addConverterFactory(GsonConverterFactory.create()) // 指定解析数据时所使用的转换库
                        .build()
                // create() 创建一个APPService的动态管理对象
                val appService = retrofit.create(AppService::class.java)
                // enqueue根据注解中配置的服务器地址进行网络请求，响应的数据会回调到Callback方法
                appService.getAppData().enqueue(object : Callback<List<App>>{
                    override fun onFailure(call: Call<List<App>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
                        val list = response.body()
                        if (list != null){
                            for (app in list){
                                log(app.toString())
                            }
                        }
                    }

                })
            }
        }
    }
}