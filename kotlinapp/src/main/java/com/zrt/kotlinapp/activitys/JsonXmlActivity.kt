package com.zrt.kotlinapp.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.*
import kotlinx.android.synthetic.main.activity_json_xml.*

class JsonXmlActivity : BasicActivity() {

    val jsonObject = """{"state":"1","error":"200","msg":""}"""
    val jsonList = """[{"state":"2","error":"200","msg":""},{"state":"3","error":"200","msg":""}]"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_json_xml

    override fun initData() {
        a_jx_json.setOnClickListener {
            a_jx_json.setText(parseJSONList(JSON_LIST).toString())
        }
        a_jx_gson_object.setOnClickListener {
            a_jx_gson_object.setText(parseGsonObjectT<DataJson>(jsonObject).toString())
        }
        a_jx_gson_list.setOnClickListener {
            a_jx_gson_list.setText(parseGsonListT<DataJson>(jsonList).toString())
        }
    }

}