package com.zrt.mybase.utils;


//import com.iflytek.cloud.msc.util.Base64;

import android.util.Base64;

/**
 * @author：Zrt
 * @date: 2023/2/15
 */
public class Base64Util {

    public static final String APP_ID = "APP_8616FECE98464F238FEED766EF6B0E9C";
    public static final String APP_ID_BASE64 = stringBase64(APP_ID);

    public static void main(String[] args) {
        System.out.println("base64="+APP_ID_BASE64+";");
    }

    public static String stringBase64(String text){
//        return Base64.encode(text.getBytes());
        return Base64.encodeToString(text.getBytes(), Base64.NO_WRAP); // NO_WRAP 去换行
//        byte[] encode =  Base64.getEncoder().encodeToString(text.getBytes());
//        System.out.println(new String(encode));
//
//        byte[] decode1 = Base64.getDecoder().decode(new String(encode));
//        byte[] decode2 = Base64.getDecoder().decode(encode);
//        System.out.println(new String(decode1));
//        System.out.println(new String(decode2));
    }


}
