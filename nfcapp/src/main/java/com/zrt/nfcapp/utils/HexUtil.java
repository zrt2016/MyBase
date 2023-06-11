package com.zrt.nfcapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class HexUtil {

	private static String hexString = "0123456789ABCDEF";

	// 字节数组转换为16进制字符串
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2) {
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		}
		// return new String(baos.toByteArray());

		try {
			byte[] lens = baos.toByteArray();
			return new String(lens);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
					baos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }
	
	
	  /**
     * 十六进制的ASCII码转字符串
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }

        return sb.toString();
    }
    
    /**
     * 过滤ASCII码中的特殊字符
     * @param content
     * @return
     */
    public static String filter(String content) {
		if (content != null && content.length() > 0) {
			char[] contentCharArr = content.toCharArray();
			for (int i = 0; i < contentCharArr.length; i++) {
				if (contentCharArr[i] < 0x20 || contentCharArr[i] == 0x7F) {
					contentCharArr[i] = 0x20;
				}
			}
			return new String(contentCharArr);
		}
		return "";
	}

}
