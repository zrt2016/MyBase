package com.zrt.nfcapp.utils;

public class StringHelper {
	/**
	 * 检查指定字符串是否为空或长度为0。
	 *
	 * @param str
	 *            需要检查的字符串
	 * @return 字符串为空或长度为0，返回true，否则返回false。
	 */
	public static boolean isEmpty(String str) {

		if (str == null)
			return true;

		return str.length() == 0 ? true : false;
	}


}