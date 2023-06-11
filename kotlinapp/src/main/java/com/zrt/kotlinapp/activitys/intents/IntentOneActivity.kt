package com.zrt.kotlinapp.activitys.intents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_intent_one.*

class IntentOneActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_intent_one
    }

    override fun initData() {
        intent_one_text.setText(intent.getStringExtra("one_data"))
        // 添加返回值，返回给上一个Activity
        back_result.setOnClickListener {
            val intent = Intent()
            intent.putExtra("intent_result", "Hello Intent result")
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    /**
     * 重写返回键方法，点击back键返回时，也带入返回值
     */
    override fun onBackPressed() {
//        super.onBackPressed()
        val intent = Intent()
        intent.putExtra("intent_result", "Back Intent result")
        setResult(RESULT_OK, intent)
        finish()
    }

    /**
     * 声明伴生对象，其他Activity跳转该界面时，调用该函数进行传参
     */
    companion object{
        fun actionIntent(context: Context, one_data:String){
//            val intent = Intent(context, IntentOneActivity::class.java)
//            intent.putExtra("one_data", one_data)
//            context.startActivity(intent)
            // 使用Apply函数精简
            val intent = Intent(context, IntentOneActivity::class.java).apply {
                        putExtra("one_data", one_data)
                    }
            context.startActivity(intent)
        }
    }
}