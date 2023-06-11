package com.zrt.mybase.tools;

import android.annotation.SuppressLint;

import com.zrt.mybase.utils.StringHelper;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class DateHelper {
	private static String defaultPattern = "yyyy-MM-dd";

	public static final String DT_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	public static final String DT_FORMAT_TYPE_1 = "yyyy年MM月dd日 HH:mm";

	public static final String DT_FORMAT_TYPE_2 = "yyyy-MM-dd HH:mm";

	public static final String DT_FORMAT_TYPE_3 = "MMddyyHHmm";

	/**
	 * 得到今天的日期 默认格式：yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getToday() {
		return getToday(defaultPattern);
	}

	/**
	 * 得到今天的日期
	 *
	 * @param pattern
	 *            格式：如yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getToday(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 字符串转换为日期格式
	 * @param format
	 * @param data
	 * @return
	 */
	public static Date parseDate(String format, String data) {
		try {
			return new SimpleDateFormat(format).parse(data);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 得到昨天的日期 默认格式：yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getYesterday() {
		return getYesterday(defaultPattern);
	}

	public static String getDay(int days) {
		return getDay(days, defaultPattern);
	}

	public static String getDay(int days, String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 获取当前时间，加或减 N秒
	 * @param pattern
	 * @param second
	 * @return
	 */
	public static String getCurrentTimeDiffSecond(String pattern, int second) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, second);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 获取当前时间，加或减 N分钟
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTimeDiffMinutes(String pattern, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minute);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 得到昨天的日期
	 *
	 * @param pattern
	 *            格式：如yy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getYesterday(String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(cal.getTime());
	}

	/**
	 * 得到月份列表 eg: getMonthList(-5,5) 取前5个月与后5个月
	 *
	 * @param beforOffset
	 *            当月之前的偏移量(负数)
	 * @param afterOffset
	 *            当月之后的偏移量(正数)
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(int beforOffset,
														 int afterOffset) {
		return getMonthList(beforOffset, afterOffset, "yyyy-MM");
	}

	/**
	 * 得到月份列表 eg: getMonthList("2008-01","2007-08","yyyy-MM")
	 *
	 * @param startday
	 *            开始月份
	 * @param endday
	 *            结束月份
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getDayList(String startday,
													   String endday, String pattern) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse(startday);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endday);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(startCal.getTime());
				hash.put("DAY", month);
				list.add(hash);
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (ParseException e) {
		}
		return list;
	}

	public static List<String> getDayListString(String startday, String endday, String pattern) {
		ArrayList<String> list = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse(startday);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endday);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0) {
				list.add(format.format(startCal.getTime()));
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (ParseException e) {
		}
		return list;
	}

	/**
	 * 得到月份列表 eg: getMonthList(-5,5,"yyyy-MM") 取前5个月与后5个月
	 *
	 * @param beforOffset
	 *            前偏移量
	 * @param afterOffset
	 *            后偏移量
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(int beforOffset,
														 int afterOffset, String pattern) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, beforOffset);
		int offset = afterOffset - beforOffset;
		if (offset >= 0) {
			for (int i = 0; i <= offset; i++) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(cal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				cal.add(Calendar.MONTH, 1);
			}
		} else {
			for (int i = 0; i >= offset; i--) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(cal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				cal.add(Calendar.MONTH, -1);
			}
		}
		return list;
	}

	/**
	 * 得到月份列表 eg: getMonthList("2008-01","2007-08","yyyy-MM")
	 *
	 * @param startMonth
	 *            开始月份
	 * @param endMonth
	 *            结束月份
	 * @param pattern
	 *            格式：如yyyy-MM
	 * @return
	 */
	public static List<Map<String, String>> getMonthList(String startMonth,
														 String endMonth, String pattern) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse(startMonth);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endMonth);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String month = format.format(startCal.getTime());
				hash.put("MONTH", month);
				list.add(hash);
				startCal.add(Calendar.MONTH, 1);
			}
		} catch (ParseException e) {
		}
		return list;
	}

	/**
	 * 得到指定月份
	 *
	 * @param offset
	 *            偏移量 offset = -1 取得上一个月 offset = 0 取得本月 offset = 1 取得下一个月
	 * @param pattern
	 *            模式
	 * @return
	 *
	 */
	public static String getMonth(int offset, String pattern) {
		String yearMonth = "197101";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MONTH, offset);
		yearMonth = format.format(nowCal.getTime());
		return yearMonth;
	}

	public static String getMonth(int offset, String pattern,
								  String currentMonth) {
		String yearMonth = "197101";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse(currentMonth);
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(startDate);
			nowCal.add(Calendar.MONTH, offset);
			yearMonth = format.format(nowCal.getTime());
		} catch (ParseException e) {
			System.out.println("d");
		}
		return yearMonth;
	}

	/**
	 * 得到一周第几天
	 *
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTime(date);
		int x = aCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (x == 0)
			x = 7;
		return x;
	}

	/**
	 * 得到一月第几天
	 *
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		aCalendar.setTime(date);
		int x = aCalendar.get(Calendar.DAY_OF_MONTH);
		return x;
	}

	public static int getLastDayOfMonth() {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int lastday = aCalendar.getActualMaximum(Calendar.DATE);
		return lastday;
	}

	public static Date getNextWeekFirstDate(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		int dayOfWeek = 0;

		for (int i = 1; i <= 7; i++) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 2)
				return c.getTime();

		}
		return date;

	}

	public static String getNextWeekFirstDay(Date date) {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.format(getNextWeekFirstDate(date));

	}

	public static Date getNextWeekLastDate(Date date) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(getNextWeekFirstDate(date));
		int dayOfWeek = 0;
		for (int i = 1; i <= 7; i++) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 1)
				return c.getTime();

		}
		return date;
	}

	public static String getNextWeekLastDay(Date date) {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.format(getNextWeekLastDate(date));

	}

	public static String getFirstDayOfMonth(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(c.getTime());

	}

	public static Date getFirstDateOfMonth(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();

	}

	/**
	 * 得到两个日期的相差的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDaysDiff(Date date1, Date date2) {
		if(date1 == null){
			return 0;
		}
		if(date2 == null){
			return 0;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) Math.abs((c1.getTimeInMillis() - c2.getTimeInMillis()) / (24 * 3600 * 1000));

	}

	public static int getMinutesDiff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) (c1.getTimeInMillis() - c2.getTimeInMillis()) / (60 * 1000);

	}

	public static int getSecondsDiff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) (c1.getTimeInMillis() - c2.getTimeInMillis()) / (1000);

	}

	public static int getHoursDiff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return (int) (c1.getTimeInMillis() - c2.getTimeInMillis()) / (60 * 60 * 1000);

	}

	public static Date getFirstMondayOfMonth(Date date) {
		Date firstDate = getFirstDateOfMonth(date);
		Calendar c = Calendar.getInstance();
		c.setTime(firstDate);
		int dayOfWeek = 0;
		for (int i = 0; i < 30; i++) {

			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 2)
				return c.getTime();
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return null;

	}

	public static String getDateString(Date date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return format.format(c.getTime());

	}

	public static Date strToDate(String strDate, String pattern){

		try {
			DateFormat f = new SimpleDateFormat(pattern);
			Date d = (Date) f.parseObject(strDate);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static String dateToStr(Date date, String pattern){
		SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
		return sFormat.format(date);
	}

	/**
	 * 得到当月第一天
	 *
	 * @param date
	 * @return
	 */
	public static String getFirstdayOfMonth(Date date) {
		return getDateString(date, "yyyy-MM") + "-01";
	}

	/**
	 * 得到某月第一天
	 *
	 * @param month
	 * @return
	 */
	public static String getFirstdayOfMonth(String month, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String firstday = "";
		try {
			Date date = format.parse(month);
			firstday = getDateString(date, "yyyy-MM") + "-01";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return firstday;
	}

	/**
	 * 得到某月最后一天
	 *
	 * @param month
	 * @return
	 */
	public static String getLastdayOfMonth(String month, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String lastday = "";
		try {
			Date date = format.parse(month);
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.setTime(date);
			aCalendar.add(Calendar.MONTH, 1);
			aCalendar.add(Calendar.DAY_OF_MONTH, -1);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			lastday = format1.format(aCalendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastday;
	}

	public int getWeekIndexOfYear(String day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		try {
			Date startDate = format.parse(day);
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(startDate);
			index = nowCal.get(Calendar.WEEK_OF_YEAR);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return index;
	}

	/**
	 * 获得一年的某周的第一天
	 *
	 * @param year
	 * @param week_index
	 * @return
	 */
	public static String getFirstDayOfWeek(String year, String week_index,
										   String patten) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy w");
		Date date = new Date();
		try {
			date = formatter.parse(year + " " + week_index);
		} catch (ParseException e) {
			return "";
		}
		SimpleDateFormat formatter2 = new SimpleDateFormat(patten);
		return formatter2.format(date);

	}

	public static String getLastDayOfWeek(String year, String week_index,
										  String patten) {
		String first_day = getFirstDayOfWeek(year,
				String.valueOf(Integer.valueOf(week_index) + 1), patten);
		return getOffsetDay(first_day, -1, patten);

	}

	public static String getOffsetDay(String currentDay, int offset,
									  String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String day = "";
		try {
			Date startDate = format.parse(currentDay);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			startCal.add(Calendar.DAY_OF_MONTH, offset);
			day = format.format(startCal.getTime());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return day;
	}

	public static void main(String[] args) throws Exception {
		System.out
				.println(getWeekList("2009-07-01", "2009-07-20", "yyyy-MM-dd"));
	}

	/**
	 * eg: getHourList("8:00","10:00","HH:mm",30)
	 *
	 * @return
	 */
	public static List<Map<String, String>> getHourList(String startHour,
														String endHour, String pattern, int minute) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		try {
			Date startDate = format.parse(startHour);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endHour);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				String hour = format.format(startCal.getTime());
				hash.put("HOUR", hour);
				list.add(hash);
				startCal.add(Calendar.MINUTE, minute);
			}
		} catch (ParseException e) {
		}
		return list;
	}

	/**
	 * 得到星期列表 eg: getWeekList("2008-01-01","2008-01-20","yyyy-MM-dd")
	 *
	 * @param startday
	 *            开始日期
	 * @param endday
	 *            结束日期
	 * @param pattern
	 *            格式：如yyyy-MM-dd
	 * @return
	 */
	public static List<Map<String, String>> getWeekList(String startday,
														String endday, String pattern) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			Date startDate = format.parse(startday);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Date endDate = format.parse(endday);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			while (startCal.compareTo(endCal) <= 0) {
				Hashtable<String, String> hash = new Hashtable<String, String>();
				SimpleDateFormat fmt = new SimpleDateFormat("E");
				DateFormatSymbols symbols = fmt.getDateFormatSymbols();
				symbols.setShortWeekdays(new String[] { "", "日", "一", "二", "三",
						"四", "五", "六" });
				fmt.setDateFormatSymbols(symbols);
				hash.put("WEEK", fmt.format(startCal.getTime()));
				list.add(hash);
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (ParseException e) {
		}
		return list;
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	public static String convertToTime(long time, String format) {
		if(StringHelper.isEmpty(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 参数格式：yyyy-MM-dd HH:mm:ss
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compareDateStr(String str1, String str2){
		long l1 = Long.valueOf(str1.replaceAll("[-\\s:]",""));
		long l2 = Long.valueOf(str2.replaceAll("[-\\s:]",""));
		if(l1>l2){
			return 1;
		}else if(l1==l2){
			return 0;
		}else{
			return -1;
		}
	}

	/**
	 * 参数格式：yyyy-MM-dd HH:mm:ss
	 * @param str1
	 * @param str2
	 *
	 * 如果 str1 > str2 返回1，相等返回0， str1 < str2 返回-1
	 * @return
	 */
	public static int compareDateStrCall(String str1, String str2){
		if(StringHelper.isEmpty(str1) || StringHelper.isEmpty(str2)) {
			return 2;
		}
		long l1 = Long.valueOf(str1.replaceAll("[-\\s:]",""));
		long l2 = Long.valueOf(str2.replaceAll("[-\\s:]",""));
		if(l1>l2){
			return 1;
		}else if(l1==l2){
			return 0;
		}else{
			return -1;
		}
	}

	/**
	 * 参数格式：yyyy-MM-dd HH:mm:ss
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compareDateStr(long str1, long str2){
		if(str1>str2){
			return 1;
		}else if(str1==str2){
			return 0;
		}else{
			return -1;
		}
	}

	public static Date convertStrToDate(String str, String format){
		if(format==null || "".equals(format)){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒 data Date类型的时间
	 */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	/**
	 * long类型转换为String类型
	 *
	 * @param currentTime要转换的long类型的时间 ；
	 * @param formatType要转换的string类型的时间格式
	 * @return
	 * @throws ParseException
	 */
	public static String longToString(long currentTime, String formatType)
			throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}


	/**
	 * long转换为Date类型
	 *
	 * @param currentTime 要转换的long类型的时间
	 * @param formatType 要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return
	 * @throws ParseException
	 */
	public static Date longToDate(long currentTime, String formatType) throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	/**
	 * string类型转换为date类型
	 *
	 * @param  strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日HH时mm分ss秒，
	 * @param  strTime的时间格式必须要与formatType的时间格式相同
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		if(StringHelper.isEmpty(formatType)){
			formatType = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}


	/**
	 * String类型转换为long类型
	 *
	 * .strTime的时间格式和formatType的时间格式必须相同
	 * @param strTime要转换的String类型的时间
	 * @param formatType时间格式
	 * @return
	 * @throws ParseException
	 */
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	/**
	 * date类型转换为long类型
	 *
	 * @param date 要转换的date类型的时间
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static String getTime() {
		final Calendar instance = Calendar.getInstance();
		final String string = instance.get(11) + ":" + instance.get(12) + ":" + instance.get(13);
		return string;
	}

	public static Date getDateBefore(int day) {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * @param pattern
	 * 		  yyyy-MM-dd HH:mm:ss
	 * @param offset
	 * @return
	 */
	public static String getDateBefore(String pattern, int offset) {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - offset);
		if(null == pattern || "".equals(pattern)){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(now.getTime());
	}

	/**
	 *
	 * @param time
	 * 		Long 转换成时间格式
	 * @return
	 */
	public static String longToStringFormat(long time, String pattern){
		if(null == pattern || "".equals(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new java.text.SimpleDateFormat(pattern).format(new java.util.Date(time * 1000));
	}


	/*
	 * 计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟 根据差值返回多长之间前或多长时间后
	 */
	public static String getDistanceTime(long time1, long time2) {
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long diff;
		String flag;
		if (time1 < time2) {
			diff = time2 - time1;
			flag = "前";
		} else {
			diff = time1 - time2;
			flag = "后";
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if (day != 0)
			return day + "天" + flag;
		if (hour != 0)
			return hour + "小时" + flag;
		if (min != 0)
			return min + "分钟" + flag;
		return "刚刚";
	}

	public static String getDistanceTime(String date1, String date2){
		long time1 = 0;
		long time2 = 0;
		try {
			time1 = stringToLong(date1, "");
			time2 = stringToLong(date2, "");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long diff;
//		String flag;
		if (time1 < time2) {
			diff = time2 - time1;
//			flag = "前";
		} else {
			diff = time1 - time2;
//			flag = "后";
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if (day != 0)
			return day + "天" ;//+ flag;
		if (hour != 0)
			return hour + "小时" ;//+ flag;
		if (min != 0)
			return min + "分钟" ; //+ flag;
		return "刚刚";
	}

	public static long getDistanceTimeSecond(String date1, String date2){
		long time1 = 0;
		long time2 = 0;
		try {
			time1 = stringToLong(date1, "");
			time2 = stringToLong(date2, "");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sec = 0;
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		sec = (diff / 1000);
		return sec;
	}

	public static long getDistanceTimeMinute(String date1, String date2){
		long time1 = 0;
		long time2 = 0;
		try {
			time1 = stringToLong(date1, "");
			time2 = stringToLong(date2, "");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long min = 0;
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		min = ((diff / (60 * 1000)));
		return min;
	}

	public static String getDistanceTimeMinusDay(String date1, String date2){
		long time1 = 0;
		long time2 = 0;
		try {
			time1 = stringToLong(date1, "");
			time2 = stringToLong(date2, "");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		StringBuilder builder = new StringBuilder();

		diff = diff - (24 * 60 * 60 * 1000);//减去一天的时间再计算
		hour = (diff / (60 * 60 * 1000));
		min = ((diff / (60 * 1000)) - hour * 60);
		sec = (diff / 1000 - hour * 60 * 60 - min * 60);

		if (hour != 0)
			builder.append(hour + "小时");
		if (min != 0)
			builder.append(min + "分钟");

		return builder.toString();
	}
}

