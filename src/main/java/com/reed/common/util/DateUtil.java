/** 
 * DateUtil.java
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
 
/**
 * Date utility for formatting, converting, etc.
 */
public final class DateUtil {
	/** format pattern: yyyyMMdd */
	public static final String YYYYMMDD = "yyyyMMdd";
	/** format pattern: yyyy-MM-dd HH:mm:ss */
	public static final String ALL = "yyyy-MM-dd HH:mm:ss";
	/** format pattern: yyyy-MM-dd */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	/** format pattern: HH:mm:ss */
	public static final String HH_MM_SS = "HH:mm:ss";
	/** day in milliseconds */
	public static final long DAY_IN_MILLI = 86400000;
	
	// DateFormat are not synchronized.
	/** format instance: yyyy-MM-dd HH:mm:ss */
	private static final SimpleDateFormat FORMAT_ALL = new SimpleDateFormat(ALL);
	/** format instance: yyyy-MM-dd */
	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(
			YYYY_MM_DD);
	/** format instance: HH:mm:ss */
	private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(
			HH_MM_SS);
	
	/**
	 * private constructor
	 */
	private DateUtil() {
		
	}

	/**
	 * get days between given two dates.
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return days between given two dates.
	 */
	public static long getInterval(final Date start, final Date end) {
		if (start == null || end == null) {
			return 0;
		}
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		endCalendar.set(Calendar.HOUR_OF_DAY, 0);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.HOUR_OF_DAY, 0);
		return (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis())
				/ DAY_IN_MILLI;
	}
	
	/**
	 * get date after certain days.
	 * 
	 * @param start
	 *            the start date
	 * @param days
	 *            interval
	 * @return date 
	 */
	public static Date getDate(final Date start, int days) {
		if (start == null || days == 0) {
			return start;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		startCalendar.set(Calendar.DATE, startCalendar.get(Calendar.DATE) + days);
		return startCalendar.getTime();
	}
	
	/**
	 * get beginning time of a day
	 * @param date
	 * 				the specified day
	 * @return the beginning time of a day
	 */
	public static Date getDayBeginning(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	/**
	 * get ending time of a day
	 * @param date
	 * 				the specified day
	 * @return the ending time of a day
	 */
	public static Date getDayEnding(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		return calendar.getTime();
	}
	/**
	 * get month 之后的 date
	 * @param month 
	 * @return
	 */
	public static Date getMonthAfterDay(int month){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
	
	
	/**
	 * get days between given two dates.
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return days between given two dates.
	 */
	public static long getInterval(final Integer start, final Integer end) {
		if (start == null || end == null) {
			return 0;
		}
		return getInterval(parse(start), parse(end));
	}

	/**
	 * parse integer to date
	 * 
	 * @param date
	 *            integer date(yyyyMMdd)
	 * @return java.util.Date type instance
	 */
	public static Date parse(final Integer date) {
		if (date == null) {
			return null;
		}
		return parse(String.valueOf(date), YYYYMMDD);
	}

	/**
	 * parse string to date
	 * 
	 * @param str
	 *            date
	 * @param format
	 *            format pattern
	 * @return java.util.Date type instance
	 */
	public static Date parse(final String str, final String format) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * format date
	 * 
	 * @param date
	 *            given date
	 * @param format
	 *            format pattern
	 * @return formated date string
	 */
	public static String format(final Date date, final String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return format(date, sdf);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * format date
	 * 
	 * @param date
	 *            given date
	 * @param df
	 *            format instance
	 * @return formated date string
	 */
	public static String format(final Date date, final DateFormat df) {
		if (date == null) {
			return null;
		}
		try {
			return df.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * format date with pattern "yyyy-MM-dd"
	 * synchronized, because DateFormat are not synchronized.
	 * 
	 * @param date
	 *            given date
	 * @return formated date string
	 */
	public static synchronized String formatDate(final Date date) {
		return format(date, FORMAT_DATE);
	}

	/**
	 * format date with pattern "HH:mm:ss"
	 * synchronized, because DateFormat are not synchronized.
	 * 
	 * @param date given date
	 * @return formated time string
	 */
	public static synchronized String formatTime(final Date date) {
		return format(date, FORMAT_TIME);
	}

	/**
	 * format date with pattern "yyyy-MM-dd HH:mm:ss"
	 * synchronized, because DateFormat are not synchronized.
	 * 
	 * @param date
	 *            given date
	 * @return formated date and time string
	 */
	public static synchronized String formatAll(final Date date) {
		return format(date, FORMAT_ALL);
	}

	/**
	 * format date
	 * 
	 * @param dateInt
	 *            date(yyyyMMdd)
	 * @param format
	 *            format pattern
	 * @return formated string
	 */
	public static String format(final int dateInt, final String format) {
		Date date = parse(String.valueOf(dateInt), YYYYMMDD);
		return DateUtil.format(date, format);
	}
}
