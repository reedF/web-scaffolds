/**
 * DecimalUtil.java
 *
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * number format/parse utility class
 */
public final class DecimalUtil {
	// ---------------- private begin----------------	
	/** private constructor */
	private DecimalUtil() {
		
	}
	
	/**
	 * 格式化数据后由于四舍五入的原因会有类似于-0.00或-0这种现象，对于这种去除-号
	 * @param str 格式化后的数字字符串
	 * @return string without hyphen character(-)
	 */
	private static String formatZero(final String str) {
		Matcher matcher = Pattern.compile("[1-9]").matcher(str);
		if (!matcher.find()) {
			return str.replaceAll("-", "");
		}
		return str;
	}
	// ---------------- private end----------------

	/**
	 * format number
	 * @param data to format number
	 * @param digits the number of fraction digits to be shown
	 * @param plus show plus in return
	 * @return formated number in string
	 */
	public static synchronized String format(final Number data, final int digits, final boolean plus) {
		if (data == null) {
			return null;
		}
		NumberFormat df = DecimalFormat.getInstance(Locale.CHINA);
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setMinimumFractionDigits(digits);
		df.setMaximumFractionDigits(digits);
		String str = df.format(data);
		if (data.toString().indexOf("-") == -1 && plus) {
			str = "+" + str;
		}
		return formatZero(str);
	}

	

	/**
	 * format number
	 * @param data to format number
	 * @param digits the number of fraction digits to be shown
	 * @return formated number in string
	 * 
	 * @see DecimalUtil#format(Number, int, boolean)
	 */
	public static String format(final Number data, final int digits) {
		return format(data, digits, false);
	}


	/**
	 * format number using "round half up"
	 * @param data to format number
	 * @param format format pattern
	 * @param plus show plus in return
	 * @return formated number in string
	 */
	public static synchronized String format(final Number data, final String format, final boolean plus) {
		if (data == null) {
			return null;
		}
		String ds = data.toString();
		BigDecimal bd = new BigDecimal(ds);
		int trailCount = format.indexOf(".");
		if (trailCount > 0) {
			trailCount = format.length() - trailCount - 1;
			bd = bd.setScale(trailCount, BigDecimal.ROUND_HALF_UP);
		}
		DecimalFormat f = new DecimalFormat(format);
		String str = f.format(bd);
		if (ds.indexOf("-") == -1 && plus) {
			str = "+" + str;
		}
		str = formatZero(str);
		return str;
	}

	/**
	 * format number using "round half up"
	 * @param data to format number
	 * @param format format pattern
	 * @return formated number in string
	 */
	public static String format(final Number data, final String format) {
		return format(data, format, false);
	}
	
}
