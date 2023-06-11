package com.zrt.weatherapp.logic.localdb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author：Zrt
 * @date: 2022/11/17
 */
class DatabaseHelper(val context: Context, val name:String, val version: Int)
        :SQLiteOpenHelper(context,name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.let {
            try {
                it.beginTransaction()
                it.execSQL("create table if not exists zhuyuan_basic_info(zhuyuan_id primary key,patient_id,xingming,xingbie,nianling,bingqu_name,bingqu_id,"
                        + " keshi_name,keshi_id,ruyuan_riqi_time,bingchuang_hao,hulijibie,yinshi_zhuangkuang,shengao,tizhong,shoushu_info,guominshi,zhenduan,zhuangtai,"
                        + " his_display_zhuyuanhao,bingchuang_fenzu,blood_type,chushengdi,gongzuo_dianhua,juzhu_dianhua,lianxiren_dianhua,shenfenzheng_hao,shengri,zhuzhenyishi,zhuzhenyishi_name,"
                        + " zerenhushi,ruyuan_qingkuang,yiliaofukuanfangshi,zhusu,chuyuan_riqi_time,modify_time,huanzhe_leixing,flag1,flag2,bingchuang_order,special_info,"
                        + " release_seat,otehr_info,zhuyuan_wdtw,xiaoxi_number integer default 0,face_feature,face_image,care_flag,wandai_tiaoma,pay_amount,zhuyuan_feiyong,"
                        + " recent_tiwen,recent_maibo,recent_huxi,recent_xueya,jiuzhen_state,ganran,mazui_modify_time,saomiao_wandai_time,his_bingqu_id,his_bingqu_name,"
                        + " his_keshi_id,his_keshi_name,qianfei_flag,bing_qing,tiwen_to_four,vte_color,mews_state);")
                it.setTransactionSuccessful()
            }finally {
                it.endTransaction()
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}