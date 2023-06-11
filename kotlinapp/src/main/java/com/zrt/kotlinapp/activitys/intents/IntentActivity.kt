package com.zrt.kotlinapp.activitys.intents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_intent.*

/**
 * 显示Intent和隐式Intent
 * 关于<intent-filter>中的<data>标签
 * 例子：intent.data = Uri.parse("https://www.baidu.com")
 *   android:scheme：用于指定数据的协议部分。如上例中的https部分
 *   android:host：用于指定数据的主机名部分，如上例中的www.baidu.com
 *   android:port：用于指定数据的端口部分，一般紧随在主机名后之后
 *   android:path：用于指定主机名和端口之后的部分，如一段网址中跟在域名之后的内容
 *   android:mimeType：用于指定可以处理的数据类型，允许使用通配符的方式进行指定
 * 只有当<data>标签中指定的内容和intent中携带的data完全一致时，当前Activity才能够响应intent
 *
 */
class IntentActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            // 可在此处处理上次异常关闭，在onSaveInstanceState中保存的数据
        }
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_intent;
    }
    val REQUEST_CODE: Int = 1
    override fun initData() {
        // 显示Intent，跳转IntentOneActivity
        start_intent_one.setOnClickListener {
//            val intent = Intent(this, IntentOneActivity::class.java)
//            intent.putExtra("one_data", "ONE_DATA")
//            startActivity(intent)
            IntentOneActivity.actionIntent(this, "ONE_DATA")
        }
        // 显示Intent，跳转，返回时可带返回值
        start_intent_one_result.setOnClickListener {
//            val intent = Intent(this, IntentOneActivity::class.java)
//            intent.putExtra("one_data", "ONE_DATA_RESULT")
//            startActivityForResult(intent, REQUEST_CODE)
            IntentOneActivity.actionIntent(this, "ONE_DATA_RESULT")
        }
        /**
         * 隐式Intent，跳转IntentTwoActivity
         * 使用隐式Intent跳转到指定的Activity，需要在AndroidManifast.xml中声明：action和category
         * 例：
         * <activity android:name=".activity_tools.IntentTwoActivity" >
         *     <intent-filter>
         *         <action android:name="com.zrt.kotlinapp.ACTION_START"/>
         *         <!-- 默认category -->
         *         <category android:name="android.intent.category.DEFAULT"/>
         *         <!--  自定义category   -->
         *         <category android:name="com.zrt.kotlinapp.MY_CATEGORY"/>
         *     </intent-filter>
         * </activity>
         * 注：每个Intent中只能指定一个action，但是可以声明多个category
         */
        start_intent_two.setOnClickListener {
            val intent = Intent("com.zrt.kotlinapp.ACTION_START")
            // 可以不用声明android.intent.category.DEFAULT项，该项是一种默认的category
            intent.addCategory("com.zrt.kotlinapp.MY_CATEGORY")
            startActivity(intent)
        }
        /**
         * 隐式启动系统浏览器
         * 由于后续声明一个IntentBrowserActivity的<intent-filter><data>标签的android:scheme="https"
         * 因此此处点击跳转时会提示选择跳转系统浏览器还是IntentBrowserActivity
         *
         * 关于<intent-filter>中的<data>标签
         * 例子：intent.data = Uri.parse("https://www.baidu.com")
         *   android:scheme：用于指定数据的协议部分。如上例中的https部分
         *   android:host：用于指定数据的主机名部分，如上例中的www.baidu.com
         *   android:port：用于指定数据的端口部分，一般紧随在主机名后之后
         *   android:path：用于指定主机名和端口之后的部分，如一段网址中跟在域名之后的内容
         *   android:mimeType：用于指定可以处理的数据类型，允许使用通配符的方式进行指定
         * 只有当<data>标签中指定的内容和intent中携带的data完全一致时，当前Activity才能够响应intent
         */
        start_intent_browser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.baidu.com")
            startActivity(intent)
        }
        /**
         * 隐式跳转系统拨号界面
         * 注：模拟器上无拨号功能会报错：
         *  error =ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.DIAL dat=tel:xxxxx }
         */
        start_intent_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:10086")
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CODE -> if (resultCode == Activity.RESULT_OK){
                val intent_result = data?.getStringExtra("intent_result")
                Toast.makeText(this, intent_result?: "not result", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 重写返回键方法，点击back键返回时，也带入返回值
     */
    override fun onBackPressed() {
        // 重写时，注掉super.onBackPressed()
        super.onBackPressed()

    }

    /**
     * Activity在被回收前会调用该方法，可在该处保存相关数据，等下次进入时带入该数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}