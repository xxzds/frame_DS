package com.anjz.util;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间处理工具类
 * @author ding.shuai
 * @date 2016年7月27日上午9:44:38
 */
public class DateUtil {

	public static final String DEFAULT_FORMAT_STYLE = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_FORMAT_STYLE_NOSPACE = "yyyy-MM-dd_HH:mm:ss";
	public static final String DEFAULT_MINUTE_STYLE = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_MINUTE_STYLE_NOSPACE = "yyyy-MM-dd_HH:mm";
	public static final String DEFAULT_HOUR_STYLE = "yyyy-MM-dd HH";
	public static final String DEFAULT_HOUR_STYLE_NOSPACE = "yyyy-MM-dd_HH";
	public static final String DEFAULT_HOUR_STYLE_NOYEAR = "MM-dd HH:mm";
	public static final String DEFAULT_ONLYHOUR_STYLE = "HH:mm";
	public static final String DEFAULT_DAY_STYLE = "yyyy-MM-dd";
	public static final String DEFAULT_DAY_STYLE_NOYEAR = "MM-dd";
	public static final String DEFAULT_DAY_STYLE_NOSPACE = "yyyyMMdd";
	public static final String DEFAULT_MILLISECOND_STYLE = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String DEFAULT_MILLISECOND_STYLE_1 = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_ONLYSEC_STYLE = "mm:ss";
	public static final String DEFAULT_ONLY_HOUR_STYLE = "HH";
	public static final String DEFAULT_HAOMIAO = "hhmmssSSS";
	public static final String DEFAULT_FULL = "yyyyMMddhhmmssSSS";
	public static final String DEFAULT_DATE_STYLE = "yyyy-MM-dd HH:mm:ss";

	private DateUtil() {
	}

	/**
	 * @param date
	 * @param formatStyle
	 * @return
	 */
	public static String formatDate(Date date, String formatStyle) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.format(date);
	}
	
	/**
	 * 将日期转换成"yyyy-MM-dd HH:mm:ss"格式
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_FORMAT_STYLE);
	}
	
	/**
	 * 将日期转换成"yyyy-MM-dd"格式
	 * @param date
	 * @return
	 */
	public static String formatDay(Date date) {
		return formatDate(date, DEFAULT_DAY_STYLE);
	}
	
	/**
	 * 日期字符串->日期对象
	 * @param dateString
	 * @param formatStyle
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateString, String formatStyle)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.parse(dateString);
	}

	/**
	 * 将日期字符串转换成"yyyy-MM-dd HH:mm:ss"格式的对象
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateString) throws ParseException {
		return parseDate(dateString, DEFAULT_FORMAT_STYLE);
	}	
	
	/**
	 * 两个日期之间相差的时间
	 * @param startDate
	 * @param endDate
	 * @param timeUnit
	 * @return
	 */
	public static int diff(Date startDate, Date endDate, TimeUnit timeUnit) {
		long diff = endDate.getTime() - startDate.getTime();
		if (timeUnit == TimeUnit.SECONDS) {
			return (int) (diff / 1000);
		} else if (timeUnit == TimeUnit.MINUTES) {
			return (int) (diff / 1000 / 60);
		} else if (timeUnit == TimeUnit.HOURS) {
			return (int) (diff / 1000 / 60 / 60);
		} else {
			throw new RuntimeException("only support second/minute/hour!");
		}
	}
	
	/**
	 * 计算两个日期相差的天数-不考虑时间
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException 
	 */
	public static int substractDay(Date startDate, Date endDate) throws ParseException {		
		Date start=DateUtil.format2Day(startDate);
		Date end=DateUtil.format2Day(endDate);				
		long diff = end.getTime()- start.getTime();
		return (int)(diff / (1000 * 60 * 60 * 24));
	}

	
	/**
	 * @param times
	 * @param timeUnit
	 * @return
	 */
	public static Date before(int times, TimeUnit timeUnit) {
		return before(new Date(), times,timeUnit);
	}

	/**
	 * 	${date}日期${times}${timeUnit}前的日期
	 * @param date
	 *  @param times
	 * @param timeUnit
	 * @return
	 */
	public static Date before(Date date,int times, TimeUnit timeUnit) {
		// ignore millisecond
		long d = date.getTime() / 1000;
		if (timeUnit == SECONDS) {
			return new Date((d - times) * 1000);
		} else if (timeUnit == MINUTES) {
			return new Date((d - times * 60) * 1000);
		} else if (timeUnit == HOURS) {
			return new Date((d - times * 60 * 60) * 1000);
		} else if (timeUnit == DAYS) {
			return new Date((d - times * 60 * 60 * 24) * 1000);
		}

		throw new RuntimeException(
				"only support TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS!");
	}

	
	/**
	 * @param date
	 * @param timeUnit
	 * @return
	 */
	public static Date after(Date date,TimeUnit timeUnit){
		return after(new Date(), timeUnit);
	}
	
	
	/**
	 * 	 ${date}日期${times}${timeUnit}后的日期
	 * @param date  日期
	 * @param times   
	 * @param timeUnit  单位
	 * @return 
	 */
	public static Date after(Date date,int times, TimeUnit timeUnit) {
		// ignore millisecond
		long d = date.getTime() / 1000;
		if (timeUnit == SECONDS) {
			return new Date((d + times) * 1000);
		} else if (timeUnit == MINUTES) {
			return new Date((d + times * 60) * 1000);
		} else if (timeUnit == HOURS) {
			return new Date((d + times * 60 * 60) * 1000);
		} else if (timeUnit == DAYS) {
			return new Date((d + times * 60 * 60 * 24) * 1000);
		}
		throw new RuntimeException(
				"only support TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS !");
	}

	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date format2Hour(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_HOUR_STYLE),
				DEFAULT_HOUR_STYLE);
	}

	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date format2Min(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_MINUTE_STYLE),
				DEFAULT_MINUTE_STYLE);
	}

	/**
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date format2Second(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_FORMAT_STYLE),
				DEFAULT_FORMAT_STYLE);
	}

	/**
	 * @param d
	 * @return
	 * @throws ParseException
	 */
	public static Date format2Day(Date date) throws ParseException {
		return parseDate(formatDate(date), DEFAULT_DAY_STYLE);
	}
}
