package com.zrt.mybase.utils;

public class Constants {
    public static final String APP_NAME = "ydhl";
    public static final String LOG = "log";
    public static final String SIGN = "sign";
    public static final String RUN_LOG = LOG + "/run";
    public static final String CRASH_LOG = LOG + "/crash";
    public static boolean LOG_FLAG = true; // 打印控制台日志记录开关
    public static boolean MYLOG_WRITE_SWITCH = true; // 写入文件日志记录开关
    public static int SDCARD_LOG_FILE_SAVE_DAYS = 3;    // sd卡中日志文件的最多保存天数
    /* 医嘱跟踪日志 false关闭             默认打开 	 */
    public static boolean WHITE_RUN_LOG_SWITCH = true;  //默认false
    /*日志写入开关，I级别日志是否写入到文件记录 */
    public static boolean WHITE_RUN_LOG_SWITCH_I = true;
    /*日志写入开关，D级别日志是否写入到文件记录 */
    public static boolean WHITE_RUN_LOG_SWITCH_D = true;
    /*日志写入开关，V级别日志是否写入到文件记录 */
    public static boolean WHITE_RUN_LOG_SWITCH_V = true;
    /*日志写入开关，W级别日志是否写入到文件记录 */
    public static boolean WHITE_RUN_LOG_SWITCH_W = true;
    /*日志写入开关，E级别日志是否写入到文件记录 */
    public static boolean WHITE_RUN_LOG_SWITCH_E = true;

    /* 网络请求超时时间 默认10秒 	*/
    public static int HTTP_REQUEST_TIME_OUT = 30000;
    public static int HTTP_REQUEST_CONNECT_TIMEOUT = 30;    // 单位秒
    public static int HTTP_REQUEST_READ_TIMEOUT = 30;    // 单位秒
    public static int HTTP_REQUEST_WRITE_TIME_OUT = 30;    // 单位秒

    /* 数据压缩请求开关 ； true打开  	*/
    public static boolean http_data_compression_flag = false;
}
