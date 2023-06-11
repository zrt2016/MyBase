package com.zrt.ydhl_phz.logic.model

/**
 * @author：Zrt
 * @date: 2022/10/29
 * Success({"user_name":"宋爱国",
 * "user_suoshu_name":"宋爱国",
 * "user_suoshu_bingqu_name":"二病区(外科)|4",
 * "user_number":"007",
 * "user_suoshu_bingqu_position":"",
 * "shebei_id":"adae0403c7898b8f",
 * "session_id":"63626c8f5dbb5",
 * "zhixing_hushi_id":"10021",
 * "server_time":"2022-11-02 21:11:43",
 * "error":"200",
 * "msg":"",
 * "enable_bingquid_subject_tuisong":1,
 * "yiyuan_id":"0",
 * "api_version":"11.3.0.0"})
 */
data class NurseUser(val user_number: String, val login_password:String) {
    var error:String = ""
    var msg:String = ""
    var user_name:String = ""
    var user_suoshu_name:String = ""
    var user_suoshu_bingqu_name:String = ""
    var user_suoshu_bingqu_position:String = ""
    var shebei_id:String = ""
    var session_id:String = ""
    var zhixing_hushi_id:String = ""
    var server_time:String = ""
    var enable_bingquid_subject_tuisong = ""
    var yiyuan_id:String = ""
    var api_version:String = ""

    // 本地选择登录科室
    var current_bingqu_name = ""
    var current_bingqu_id = ""
    constructor():this("", "")
    constructor(user_number: String, login_password:String, shebei_id:String):this(user_number, login_password){
        this.shebei_id = shebei_id
    }

    override fun toString(): String {
        return "NurseUser(user_number='$user_number', " +
                "login_password='$login_password', error='$error'," +
                " shebei_id='$shebei_id', msg='$msg')"
    }


}