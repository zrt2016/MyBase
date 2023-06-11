package com.zrt.kotlinapp.utils.https

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * @author：Zrt
 * @date: 2022/7/25
 * 添加引用，添加此依赖后，无需再次添加OKhttp的引用
 * implementation 'com.squareup.retrofit2:retrofit:2.6.1'
 * implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
 *
 * retrofit的接口文件建议以具体功能种类名开头，Service结尾
 */

/**
 * @GET("get_data.json")
 * 标识调用getAppData()时是get请求，请求地址是"get_data.json"
 */
interface AppService{
    @GET("get_data.json")
    fun getAppData():Call<List<App>>
}

interface ExampleService{
    // 发送请求时page参数会代替{page}地址
    @GET("{page}/get_data.json")
    fun getData(@Path("page") page:Int):Call<Data>
}
/**
 * 解决带参地址
 * Get http://baidu.com/get_data.json?u=<user>&t=<token>
 * */
interface ExampleService2{
    @GET("get_data.json")
    fun getData(@Query("u") user:String, @Query("t") token:String)
}

/**
 * 删除指定ID
 * 使用@Path("id")动态指定id
 * ResponseBody：接收任意类型的数据，不会对响应数据进行解析
 */
interface DeleteService{
    @DELETE("data/{id}")
    fun deleteData(@Path("id") id:String):Call<ResponseBody>
}

/**
 * retrofit发送请求时会动态将Data转换为Json格式的数据，并放到http请求的body部分
 */
interface PostService{
    @POST("data/create")
    fun createData(@Body data:Data):Call<ResponseBody>
}

/**
 * 指定header参数
 * 静态声明
 */
interface HeaderService{
    @Headers("User-Agent: okhttp", "Cache-Control:max-age=0")
    @GET("get_data.json")
    fun getData():Call<Data>
}
/**
 * 指定header参数
 * 动态声明
 */
interface HeaderService2{
    @Headers("User-Agent: okhttp", "Cache-Control:max-age=0")
    @GET("get_data.json")
    fun getData(@Header("User-Agent") userAgent: String,
        @Header("Cache-Control") cacheControl:String):Call<Data>
}

/**
 * 封装Retrofit
 */
object ServiceCreater{
    const val BASE_URL = "http://www.baidu.com/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    fun <T> create(serviceClass:Class<T>):T = retrofit.create(serviceClass)
    inline fun <reified T> create() : T = create(T::class.java)
}