package com.zrt.mybase.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.content.Intent;

public class StringHelper {

	public static String get(Map<String, String> map, String keyName) {
		if (null == map)
			return "";
		return notEmpty(map.get(keyName));
	}

	public static String get2(Map<String, Object> map, String keyName) {
		if (null == map)
			return "";
		return notEmpty(map.get(keyName)+"");
	}

	public static String get(List<Map<String, String>> list, int index,
							 String keyName) {
		return get(list.get(index), keyName);
	}

	public static String notEmpty(String src) {
		return (src == null || "null".equals(src) || src.length() == 0) ? "" : src.toString();
	}

	public static String notNull(String src) {
		return (src == null || src == "null" || src == "'null'") ? "" : src.toString();
	}

	public void print(Object obj) {
		if (obj instanceof Object[]) {
			Object[] objs = (Object[]) obj;
			for (int i = 0; i < objs.length; i++) {
				System.out.println(i + "=" + objs[i].toString());
			}
		}
	}

	public static String ListToString(List<Map<String, String>> list,
									  String key, String split) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			str += get(map, key) + split;
		}
		if (!"".equals(str))
			str = str.substring(0, str.lastIndexOf(split));
		return str;

	}

	public static String toUTF8(String str) {
		String s = str;
		try {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) > '\177') {
					result.append("&#x");
					String hex = Integer.toHexString(s.charAt(i));
					StringBuffer hex4 = new StringBuffer(hex);
					hex4.reverse();
					int len = 4 - hex4.length();
					for (int j = 0; j < len; j++)
						hex4.append('0');

					for (int j = 0; j < 4; j++)
						result.append(hex4.charAt(3 - j));

					result.append(';');
				} else {
					result.append(s.charAt(i));
				}

			return result.toString();
		} catch (Exception e) {
			return s;
		}
	}

	public static String encoderByMd5(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] unencodedPassword = str.getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return str;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}

	public static String encodeURL(String str) {
		String result = "";
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		return result;
	}

	public static double getDouble(String value) {
		double result = 0.00;
		try {
			result = Double.parseDouble(value);
		} catch (Exception e) {
			System.out.println(value);
			return 0.00;
		}
		return result;
	}

	public static String ArrayToString(String[] strArray, String splitFlag) {
		String tmpString = "";
		if (strArray.length == 0) {
			tmpString = "";
		} else {
			for (int i = 0; i < strArray.length; i++) {
				tmpString = tmpString + strArray[i];
				if (i < strArray.length - 1) {
					tmpString = tmpString + splitFlag;
				}
			}
		}
		tmpString = tmpString.trim();
		return tmpString;
	}

//	public static String getStringExtra(Intent intent, String key) {
//		String key_value = "";
//		try {
//			key_value = intent.getStringExtra(key);
//			if (null == key_value) {
//				key_value = "";
//			}
//		} catch (Exception e) {
//			key_value = "";
//		}
//		return key_value;
//	}

	public static String upperFirst(String str) {
		if (str == null || str.length() < 1) {
			return str;
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 删除字符串中的回车换行
	 */
	public static String deleteRN(String str){
		String newString = str;
		Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
		Matcher m = CRLF.matcher(str);
		if (m.find()) {
			newString = m.replaceAll("");
		}
		return newString;
	}

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

	/**
	 *
	 * @param params 需要check的参数
	 * @param returnValue 返回默认参数
	 * @return
	 */
	public static String getCursorString(String params, String returnValue){
		return null == params || "null".equals(params) || "".equals(params) ? returnValue : params;
	}

	/**
	 *
	 * @param hulijibie 根据患者护理级别名称转换
	 * @return
	 */
	public static String convertHulijibieName(String hulijibie){
		if("特级护理".equals(hulijibie))
		{
			return "特级";
		}
		else if("三级护理".equals(hulijibie))
		{
			return "III级";
		}
		else if("二级护理".equals(hulijibie))
		{
			return "II级";
		}
		else if("一级护理".equals(hulijibie))
		{
			return "I级";
		}else{
			return "";
		}
	}

	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static String getStringVal(Object obj){
		if(obj == null){
			return "";
		}else if("null".equals(obj) || "null" == obj){
			return "";
		}else{
			return String.valueOf(obj);
		}
	}

	/**
	 * 采用正则表达式的方式来判断一个字符串是否为数字，这种方式判断面比较全
	 * 可以判断正负、整数小数
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		boolean isInt = Pattern.compile("^-?[1-9]\\d*$").matcher(str).find();
		boolean isDouble = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).find();
		return isInt || isDouble;
	}
	
	
	/** 需要去掉的特殊符号 */
	public final static String SPECIAL_SYMBOL = "[,，=＝《》<>%％&＆]";
	/**
	  *  过滤特殊字符
	 * @param code
	 * @return
	 */
	public static String replaceSpecialSymbol(String code){
		Pattern p = Pattern.compile(SPECIAL_SYMBOL);
		Matcher matcher = p.matcher(code);
		String newCode = matcher.replaceAll("").trim();
		return newCode;
	}
	/**
	 * 过滤特殊字符
	 * @param code
	 * @param special 需要过滤的字符例：=
	 * 		格式：[=]
	 * @return
	 */
	public static String replaceSpecialSymbol(String code, String special){
		Pattern p = Pattern.compile(special);
		Matcher matcher = p.matcher(code);
		String newCode = matcher.replaceAll("").trim();
		return newCode;
	}

}