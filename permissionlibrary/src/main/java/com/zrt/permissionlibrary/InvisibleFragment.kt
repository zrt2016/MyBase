package com.zrt.permissionlibrary

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import kotlin.Unit as Unit

/**
 * @author：Zrt
 * @date: 2022/8/24
 * 在Fragment中实现Permission的申请
 */
// typealias关键字可以用于给任意类型指定一个别名，但不能声明在函数内或类中
typealias PermissionCallback = (Boolean, List<String>) -> Unit
class InvisibleFragment : Fragment() {
    private var callback:((Boolean, List<String>) -> Unit)? = null
    
    fun requestNow(cb: PermissionCallback, vararg permission: String){
        callback = cb
        requestPermissions(permission, 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
            permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1){
            val deniedList = ArrayList<String>()
            for((index, result) in grantResults.withIndex()){
                if (result != PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            val empty = deniedList.isEmpty()
            callback?.let {
                it(empty, deniedList)
            }
        }

    }
}