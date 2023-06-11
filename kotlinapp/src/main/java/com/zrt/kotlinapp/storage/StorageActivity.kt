package com.zrt.kotlinapp.storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.activity_file_storage.*

/**
 * 文件存储
 */
class StorageActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.activity_file_storage

    override fun initData() {
        // 文件存储
        storage_file_bt.setOnClickListener {
            saveFile(this, "data", storage_file_et.text.toString())
        }
        // 文件读取
        storage_file_get.setOnClickListener {
            storage_file_get.setText(readFile(this, "data"))
        }
        // sharedPreference存储
        storage_sp_bt.setOnClickListener {
            saveSP(this, "data")
        }
        storage_sp_get.setOnClickListener {
            storage_sp_get.setText(getSP(this, "data"))
        }
        // 删除sharedPreference
        storage_sp_delete.setOnClickListener {
            deleteAllSP(this)
        }
    }
}