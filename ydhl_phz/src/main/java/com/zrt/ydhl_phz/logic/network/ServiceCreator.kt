package com.zrt.ydhl_phz.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author：Zrt
 * @date: 2022/8/16
 */
object ServiceCreator {
    val IP = "10.81.2.39"
    val SERVER_PORT = 8098
    val SERVER_IP = "$IP:$SERVER_PORT"
    val SERVER_URL = "http://$SERVER_IP/Common/"
    val REMOTE_APP_URL = "http://$SERVER_IP/zpdydhl_html5/"
    val REMOTE_APT_URL = "http://$IP/zpdydhl/"
    val SERVER_H5_URL = "http://$IP"

    private val BASE_URL = SERVER_URL

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    fun <T> create(serviceClass: Class<T>) : T = retrofit.create(serviceClass)
    inline fun <reified T> create():T = create(T::class.java)
}
