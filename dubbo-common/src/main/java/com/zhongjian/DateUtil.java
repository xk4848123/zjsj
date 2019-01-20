package com.zhongjian;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	private static final int YESTERDY = -1;

	private static final int TODAY = 0;

	private static final int TOMORROWDAT = 1;

	private static final int OTHER_DAY = 10000;

	public static boolean isLatestWeek(Date addtime, Date now) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(now);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为1天前
		Date before1days = calendar.getTime(); // 得到1天前的时间
		if (before1days.getTime() < addtime.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	public static String getCurrentYearMonth() {
		Date curretDateTime = new Date();
		Calendar curretDateTimeCalendar = Calendar.getInstance();
		curretDateTimeCalendar.setTime(curretDateTime);
		int curretYear = curretDateTimeCalendar.get(Calendar.YEAR);
		int curretMonth = curretDateTimeCalendar.get(Calendar.MONTH) + 1;
		String currentYearMonth = null;
		if (curretMonth < 10) {
			currentYearMonth = curretYear + "-0" + curretMonth;
		} else {
			currentYearMonth = curretYear + "-" + curretMonth;
		}
		return currentYearMonth;
	}

	// 获取某一天的日期
	public static String getACertainYearMonth(Date dateTime) {
		Calendar curretDateTimeCalendar = Calendar.getInstance();
		curretDateTimeCalendar.setTime(dateTime);
		int curretYear = curretDateTimeCalendar.get(Calendar.YEAR);
		int curretMonth = curretDateTimeCalendar.get(Calendar.MONTH) + 1;
		int curretDay = curretDateTimeCalendar.get(Calendar.DATE);
		String currentYearMonth = null;
		String currentYearMonthDay = null;
		if (curretMonth < 10) {
			currentYearMonth = curretYear + "-0" + curretMonth;
		} else {
			currentYearMonth = curretYear + "-" + curretMonth;
		}
		if (curretDay < 10) {
			currentYearMonthDay = currentYearMonth + "-0" + curretDay;
		} else {
			currentYearMonthDay = currentYearMonth + "-" + curretDay;
		}
		return currentYearMonthDay;
	}

	// 获取某一天的日期
	public static Date getACertainDay(Integer addsub) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, addsub);
		date = calendar.getTime();
		return date;
	}

	public static int JudgmentDay(Date date) {
		Calendar pre = Calendar.getInstance();
		Date predate = new Date(System.currentTimeMillis());
		pre.setTime(predate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
			int diffDay = cal.get(Calendar.DAY_OF_YEAR) - pre.get(Calendar.DAY_OF_YEAR);

			switch (diffDay) {
			case YESTERDY: {
				return YESTERDY;
			}
			case TODAY: {
				return TODAY;
			}
			case TOMORROWDAT: {
				return TOMORROWDAT;
			}
			}
		}
		return OTHER_DAY;
	}

	public static String getNextDatePointStr() {
		Date date = new Date();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String dateHStr = new SimpleDateFormat("HH").format(date);
		String dateYMDStr = format2.format(date);
		Integer nextDateH = Integer.valueOf(dateHStr);
		nextDateH++;
		String nextDateHStr = String.valueOf(nextDateH);
		String firstDateStr = dateYMDStr + " " + nextDateHStr + ":00:00";
		return firstDateStr;
	}

	public static Date getTodayTimePoint(String timePoint) throws ParseException {
		Date date = new Date();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String dateYMDStr = format2.format(date);
		String todayTimePoint = dateYMDStr + " " + timePoint;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sDateFormat.parse(todayTimePoint);
	}

	public static Date getTomorroTimePoint(String timePoint) throws ParseException {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateYMDStr = formatter.format(date);
		String tomorroTimePoint = dateYMDStr + " " + timePoint;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sDateFormat.parse(tomorroTimePoint);
	}

	
	public static Date getYesterday() {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}
	public static Date getYesterdayTimePoint(String timePoint) throws ParseException {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateYMDStr = formatter.format(date);
		String tomorroTimePoint = dateYMDStr + " " + timePoint;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sDateFormat.parse(tomorroTimePoint);
	}

	// 超过timepoint算前一天，否则算后一天
	public static String getDateStringByPoint(String timePoint) throws ParseException {
		Date now = new Date();
		Date toDayTimePoint = getTodayTimePoint(timePoint);
		if (now.compareTo(toDayTimePoint) > 0 || now.compareTo(toDayTimePoint) == 0) {
			// 超过当天七点
			return getACertainYearMonth(now);

		} else {
			return getACertainYearMonth(getACertainDay(-1));
		}
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static Date StringToDate(String dateStr, String formatStr) throws ParseException {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		date = dd.parse(dateStr);
		return date;
	}

	public static Date AddOrSubDate(Date holdDate,int days) {
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(holdDate);
		calendar.add(Calendar.DATE,days);
		return calendar.getTime();
}
	
	public static Date getDateYMD() throws ParseException {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateYMDStr = formatter.format(date);
		return formatter.parse(dateYMDStr);
	}
}
