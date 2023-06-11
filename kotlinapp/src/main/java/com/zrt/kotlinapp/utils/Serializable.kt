package com.zrt.kotlinapp.utils

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * @author：Zrt
 * @date: 2022/8/9
 * Serializable和Parcelable
 */
/**
 * 实现序列化
 */
class PersonSeria: Serializable{
    var name = ""
    var age = 0
}

/**
 * Parcelable：与Serializable的序列化不同，是将对象进行分解，而分解后的每一部分都是Intent所支持的数据类型
 */
class PersonParce :Parcelable{
    var name = ""
    var age = 0
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonParce> {
        override fun createFromParcel(parcel: Parcel): PersonParce {
            val personParce = PersonParce()
            // 此处内容读取顺序要和写入顺序一致
            personParce.name = parcel.readString() ?: ""
            personParce.age = parcel.readInt()
            return personParce
        }

        override fun newArray(size: Int): Array<PersonParce?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Parcelable的简写，添加@Parcelize注解即可
 */
@Parcelize
class PersonParceK(var name:String, var age:Int) :Parcelable