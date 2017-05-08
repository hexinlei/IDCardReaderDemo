package com.demo.ranger.idreaderdemo.util;

/**
 * @project:IDCardReaderDemo
 * @package:com.demo.ranger.idreaderdemo.util
 * @copyright: Copyright[2017-2999] [Markor Investment Group Co. LTD]. All Rights Reserved.
 * @filename: DateUtil
 * @description:&lt;描述&gt;
 * @author: wangyunlei
 * @date: 17/5/9-上午12:55
 * @version: 1.0
 */

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 *
 */
public class DateUtil
{

	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据起始日期和间隔时间计算结束日期
	 *
	 * @params sDate开始时间
	 *
	 * @params days间隔时间
	 * 按照天数区分
	 * @return 结束时间
	 * */
	public static Date calculateEndDate(final Date sDate, final int days)
	{
		// 将开始时间赋给日历实例
		final Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		// 计算结束时间
		sCalendar.add(Calendar.DAY_OF_YEAR, days);
		// 返回Date类型结束时间
		return sCalendar.getTime();
	}

	public static Date convertStr2Date(final String dateStr, final String dateFormat)
	{
		if(dateStr == null){
			return null;
		}
		// final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try
		{
			date = sdf.parse(dateStr);
		}
		catch (final ParseException e)
		{
			// YTODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return date;
	}

	public static Date convertStr2Date(final String dateStr)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try
		{
			date = sdf.parse(dateStr);
		}
		catch (final ParseException e)
		{
			// YTODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return date;
	}

	// add by huangbijin 2015-08-11
	/**
	 * 字符串转换为java.util.Date<br>
	 * 支持格式为 yyyy.MM.dd G 'at' hh:mm:ss z 如 '2002-1-1 AD at 22:10:59 PSD'<br>
	 * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'<br>
	 * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'<br>
	 * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00' <br>
	 * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am' <br>
	 *
	 * @param time String 字符串<br>
	 * @return Date 日期<br>
	 */
	public static Date stringToDate(String time)
	{
		SimpleDateFormat formatter;
		int tempPos = time.indexOf("AD");
		time = time.trim();
		formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
		if (tempPos > -1)
		{
			time = time.substring(0, tempPos) + "公元" + time.substring(tempPos + "AD".length());// china
			formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
		}
		tempPos = time.indexOf("-");
		if (tempPos > -1 && (time.indexOf(" ") < 0))
		{
			formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
		}
		else if ((time.indexOf("/") > -1) && (time.indexOf(" ") > -1))
		{
			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		else if ((time.indexOf("-") > -1) && (time.indexOf(" ") > -1))
		{
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		else if ((time.indexOf("/") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1))
		{
			formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		}
		else if ((time.indexOf("-") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1))
		{
			formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		}
		final ParsePosition pos = new ParsePosition(0);
		final java.util.Date ctime = formatter.parse(time, pos);

		return ctime;
	}

	/**
	 * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制)<br>
	 * 如Sat May 11 17:24:21 CST 2002 to '2002-05-11 17:24:21'<br>
	 *
	 * @param time Date 日期<br>
	 * @return String 字符串<br>
	 */


	public static String dateToString(final Date time)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final String ctime = formatter.format(time);

		return ctime;
	}

	public static String dateToString(final Date time, final String format)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat(format);
		final String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss a'(12小时制)<br>
	 * 如Sat May 11 17:23:22 CST 2002 to '2002-05-11 05:23:22 下午'<br>
	 *
	 * @param time Date 日期<br>
	 * @param x int 任意整数如：1<br>
	 * @return String 字符串<br>
	 */
	public static String dateToString(final Date time, final int x)
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
		final String ctime = formatter.format(time);

		return ctime;
	}

	/**
	 * 取系统当前时间:返回只值为如下形式
	 * 2002-10-30 20:24:39
	 *
	 * @return String
	 */
	public static String Now()
	{
		return dateToString(new Date());
	}

	/**
	 * 取系统当前时间:返回只值为如下形式
	 * 2002-10-30 08:28:56 下午
	 *
	 * @param hour 为任意整数
	 * @return String
	 */
	public static String Now(final int hour)
	{
		return dateToString(new Date(), hour);
	}

	/**
	 * 取系统当前时间:返回值为如下形式
	 * 2002-10-30
	 *
	 * @return String
	 */
	public static String getYYYY_MM_DD()
	{
		return dateToString(new Date()).substring(0, 10);

	}

	/**
	 * 取系统当前时间：按照指定格式返回
	 * @param format
	 * @return
	 */
	public  static Date getNowDateForFormate(final String format)
	{
		return  convertStr2Date(Now(),format);
	}

	/**
	 * 取系统给定时间:返回值为如下形式
	 * 2002-10-30
	 *
	 * @return String
	 */
	public static String getYYYY_MM_DD(final String date)
	{
		return date.substring(0, 10);

	}

	public static String getHour()
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("H");
		final String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getDay()
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("d");
		final String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getMonth()
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("M");
		final String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getYear()
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy");
		final String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getWeek()
	{
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("E");
		final String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getStringDate(final Date date)
	{

		final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		String stringDate = "";
		stringDate = dateformat.format(date);
		return stringDate;
	}

	/**
	 * 计算时间区间内相差天数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long getSubtractionDate(final Date begin,final Date end)
	{
		if (begin==null || end==null)
		{
			new InvalidParameterException("date must not be null!");
		}
		return (begin.getTime() - end.getTime())/(24*60*60*1000);
	}


	public static String formatDate(final Date handlingDate)
	{
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		String result = null;
		if (handlingDate != null)
		{
			result = simpleDateFormat.format(handlingDate);
		}
		return result;
	}



}
