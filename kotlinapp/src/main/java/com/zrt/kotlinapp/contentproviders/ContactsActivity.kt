package com.zrt.kotlinapp.contentproviders

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_c_p.*

/**
 * 读取联系人
 * AndroidManifest.xml添加权限：
 *  <!-- 读取联系人 -->
 *  <uses-permission android:name="android.permission.READ_CONTACTS"/>
 */
class ContactsActivity : BasicActivity() {
    val CONTACTS_CODE = 1
    val contacrList = ArrayList<String>()
    lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_c_p

    override fun initData() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacrList)
        contact_lv.adapter = adapter
        // 校验是否开起权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            // 未开起，则进行申请
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_CODE)
        }else {
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CONTACTS_CODE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else {
                    ToastUtils.show(this, "You denied the perission")
                }
            }
        }
    }

    private fun readContacts() {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null)?.apply {
            while (moveToNext()){
                // 联系人姓名
                val displayName = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacrList.add("$displayName\n$number")
            }
            adapter.notifyDataSetChanged()
            close()
        }

    }
}