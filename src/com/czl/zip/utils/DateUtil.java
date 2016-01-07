package com.czl.zip.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class DateUtil
{
	/**
	 * 日期格式，年份，例如：2004，2008
	 */
	public static final String DATE_FORMAT_YYYY = "yyyy";

	/**
	 * 日期格式，年份和月份，例如：200707，200808
	 */
	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

	/**
	 * 日期格式，年份和月份，例如：200707，2008-08
	 */
	public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";

	/**
	 * 日期格式，年月日，例如：20050630，20080808
	 */
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	//
	public static final String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

	/**
	 * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
	 */
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	
	/**
	 * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
	 */
	public static final String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

	/**
	 * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
	 */
	public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

	/**
	 * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
	 */
	public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";

	/**
	 * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
	 */
	public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

	/**
	 * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开，
	  例如：2005-05-10 23：20：00，2008-08-08 20:08:08
	 */
	public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
	 */
	public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";
    

    /**
	 * 将Date转换为指定格式的时间字符串，如YYYYMMDDhhmmss或yyyy-MM-dd HH:mm
	 * 
	 * @param time
	 * @return
	 */
    public static String parseDateToStr(Date time, String timeFromat)
    {
    	// REVIEW-ACCEPT 增加传入参数非空判断
    	DateFormat dateFormat=new SimpleDateFormat(timeFromat);
    	return dateFormat.format(time);
    }
    
    /**
     * 
     * @param time
     * @param timeFromat
     * @param defaultValue 默认值,如果为空返回当前时间的对应格式日期。
     * @author zhenhui
     * @return
     */
    public static String parseDateToStr(Date time, String timeFromat, final Date defaultValue)
    {
    	// REVIEW-ACCEPT 增加传入参数非空判断
    	//FIXED
    	try{
    		DateFormat dateFormat=new SimpleDateFormat(timeFromat);
        	return dateFormat.format(time);
    	}catch (Exception e){
    		if(defaultValue!=null)
				return parseDateToStr(defaultValue, timeFromat);
			else
				return parseDateToStr(new Date(), timeFromat);
    	}
    }
    /**
     * 
     * @param time
     * @param timeFromat
     * @param defaultValue
     * @author zhenhui
     * @return
     */
    public static String parseDateToStr(Date time, String timeFromat, final String defaultValue)
    {
    	// REVIEW-ACCEPT 增加传入参数非空判断
    	//FIXED
    	try{
    		DateFormat dateFormat=new SimpleDateFormat(timeFromat);
        	return dateFormat.format(time);
    	}catch (Exception e){
    		return defaultValue;
    	}
    }
    
    public static String dateToStr(Date time, String timeFromat,
			final String defaultValue) {
    	// REVIEW-ACCEPT 增加传入参数非空判断
    	//FIXED
    	
    	try{
    		DateFormat dateFormat=new SimpleDateFormat(timeFromat);
        	return dateFormat.format(time);
    	}catch (Exception e){
    		return defaultValue;
    	}
    }
    
    /**
     * 按指定时间格式的字符串转换为Date，如yyyy-MM-dd HH:mm
     * @标题:
     * @描述:
     * @param time
     * @param timeFromat
     * @return
     * @throws ParseException
     * @公司:广东数据通信网络有限公司
     * @作者:ljt
     * @日期:2007-10-23
     */
    public static Date parseStrToDate(String time, String timeFromat) throws ParseException
    {
    	if (time == null || time.equals("")) {
    		return null;
    	}
    	DateFormat dateFormat=new SimpleDateFormat(timeFromat);
    	return dateFormat.parse(time);
    }
    
    
    public static Date strToDate(String strTime, String timeFromat) {
    	return strToDate(strTime, timeFromat, null);
	}
    
    public static Date strToDate(String strTime, String timeFromat,
			Date defaultValue) {
    	// REVIEW-ACCEPT 增加传入参数非空判断
    	//FIXED
		try {
			DateFormat dateFormat = new SimpleDateFormat(timeFromat);
			return dateFormat.parse(strTime);
		} catch (Exception e) {
			return defaultValue;
		}
	}
    
    /**
     * 当strTime为2008-9时返回为2008-9-1 00:00是日期时间
     * @标题:
     * @描述:无法转换返回null.
     * @param strTime
     * @return
     * @公司:广东数据通信网络有限公司
     * @作者:lxm
     * @日期:2009-11-25
     */
    public static Date strToDate(String strTime) {
    	if(strTime==null || strTime.trim().length()<=0)
    		return null;
    	
		Date date = null;
		List<String> list = new ArrayList<String>(0);
		
		list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
		list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
		list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
		list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
		list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
		list.add(DATE_FORMAT_YYYY_MM_DD);
		//list.add(DATE_FORMAT_YY_MM_DD);
		list.add(DATE_FORMAT_YYYYMMDD);
		list.add(DATE_FORMAT_YYYY_MM);
		list.add(DATE_FORMAT_YYYYMM);
		list.add(DATE_FORMAT_YYYY);
		
		// REVIEW 这个循环没有什么意义，仅仅是作为格式匹配，根据长度判断就可以了
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String format = (String) iter.next();
			if(strTime.indexOf("-")>0 && format.indexOf("-")<0)
				continue;
			// REVIEW 无效语句，根据list的定义，format.indexOf("-")永远<0，
			if(strTime.indexOf("-")<0 && format.indexOf("-")>0)
				continue;
			if(strTime.length()>format.length())
				continue;
			date = strToDate(strTime, format);
			if (date != null)
				break;
		}

		return date;
	}
    
    /**
	 * 解析一个日期之间的所有月份
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */  
    public static List<String> getMonthList(String beginDateStr, String endDateStr) {  
        // 指定要解析的时间格式  
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");  
        // 返回的月份列表  
        String sRet = "";  
        
        // 定义一些变量  
        Date beginDate = null;  
        Date endDate = null;  
  
        GregorianCalendar beginGC = null;  
        GregorianCalendar endGC = null;  
        List<String> list = new ArrayList<String>();  
        
        try {  
            // 将字符串parse成日期  
            beginDate = f.parse(beginDateStr);  
            endDate = f.parse(endDateStr);  
  
            // 设置日历  
            beginGC = new GregorianCalendar();  
            beginGC.setTime(beginDate);  
  
            endGC = new GregorianCalendar();  
            endGC.setTime(endDate);  
  
            // 直到两个时间相同  
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {  
                sRet = beginGC.get(Calendar.YEAR) + "-"  
                        + (beginGC.get(Calendar.MONTH) + 1);  
                list.add(sRet);  
                // 以月为单位，增加时间  
                beginGC.add(Calendar.MONTH, 1);  
            }  
            return list;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * 解析一个日期段之间的所有日期 
     *  
     * @param beginDateStr 
     *            开始日期 
     * @param endDateStr 
     *            结束日期 
     * @return 
     */  
    public static ArrayList getDayList(String beginDateStr, String endDateStr) {  
        // 指定要解析的时间格式  
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
  
        // 定义一些变量  
        Date beginDate = null;  
        Date endDate = null;  
  
        Calendar beginGC = null;  
        Calendar endGC = null;  
        ArrayList list = new ArrayList();  
  
        try {  
            // 将字符串parse成日期  
            beginDate = f.parse(beginDateStr);  
            endDate = f.parse(endDateStr);  
  
            // 设置日历  
            beginGC = Calendar.getInstance();  
            beginGC.setTime(beginDate);  
  
            endGC = Calendar.getInstance();  
            endGC.setTime(endDate);  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
            // 直到两个时间相同  
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {  
  
                list.add(sdf.format(beginGC.getTime()));  
                // 以日为单位，增加时间  
                beginGC.add(Calendar.DAY_OF_MONTH, 1);  
            }  
            return list;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
   
    public static List getYearList() {  
        List<Integer> list = new ArrayList<Integer>();  
        Calendar c = null;  
        c = Calendar.getInstance();  
        c.setTime(new Date());  
        int currYear = Calendar.getInstance().get(Calendar.YEAR);  
  
        int startYear = currYear - 5;  
        int endYear = currYear + 10;  
        for (int i = startYear; i < endYear; i++) {  
            list.add(new Integer(i));  
        }  
        return list;  
    }

	public static int getCurrYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}  
  
    /**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */  
    public static LinkedHashMap getWeekList(int year) {  
        LinkedHashMap map = new LinkedHashMap();  
        Calendar c = new GregorianCalendar();  
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);  
        int count = getWeekOfYear(c.getTime());  
  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dayOfWeekStart = "";  
        String dayOfWeekEnd = "";  
        for (int i = 1; i <= count; i++) {  
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));  
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));  
            map.put(new Integer(i), "第"+i+"周(从"+dayOfWeekStart + "至" + dayOfWeekEnd+")");  
        }  
        return map;  
  
    }  
      
    /** 
     * 得到一年的总周数 
     * @param year 
     * @return 
     */  
    public static int getWeekCountInYear(int year){  
        Calendar c = new GregorianCalendar();  
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);  
        int count = getWeekOfYear(c.getTime());  
        return count;  
    }  
  
    /** 
     * 取得当前日期是多少周 
     *  
     * @param date 
     * @return 
     */  
    public static int getWeekOfYear(Date date) {  
        Calendar c = new GregorianCalendar();  
        c.setFirstDayOfWeek(Calendar.MONDAY);  
        c.setMinimalDaysInFirstWeek(7);  
        c.setTime(date);  
  
        return c.get(Calendar.WEEK_OF_YEAR);  
    }  
  
    /** 
     * 得到某年某周的第一天 
     *  
     * @param year 
     * @param week 
     * @return 
     */  
    public static Date getFirstDayOfWeek(int year, int week) {  
        Calendar c = new GregorianCalendar();  
        c.set(Calendar.YEAR, year);  
        c.set(Calendar.MONTH, Calendar.JANUARY);  
        c.set(Calendar.DATE, 1);  
  
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);  
  
        return getFirstDayOfWeek(cal.getTime());  
    }  
  
    /** 
     * 得到某年某周的最后一天 
     *  
     * @param year 
     * @param week 
     * @return 
     */  
    public static Date getLastDayOfWeek(int year, int week) {  
        Calendar c = new GregorianCalendar();  
        c.set(Calendar.YEAR, year);  
        c.set(Calendar.MONTH, Calendar.JANUARY);  
        c.set(Calendar.DATE, 1);  
  
        Calendar cal = (GregorianCalendar) c.clone();  
        cal.add(Calendar.DATE, week * 7);  
  
        return getLastDayOfWeek(cal.getTime());  
    }  
      
    /** 
     * 得到某年某月的第一天 
     * @param year 
     * @param month 
     * @return 
     */  
    public static Date getFirestDayOfMonth(int year,int month){  
        month = month-1;  
        Calendar   c   =   Calendar.getInstance();     
        c.set(Calendar.YEAR, year);  
        c.set(Calendar.MONTH, month);  
     
        int day = c.getActualMinimum(c.DAY_OF_MONTH);  
  
        c.set(Calendar.DAY_OF_MONTH, day);  
        return c.getTime();  
          
    }  
      
    /** 
     * 提到某年某月的最后一天 
     * @param year 
     * @param month 
     * @return 
     */  
    public static Date getLastDayOfMonth(int year,int month){  
        month = month-1;  
        Calendar   c   =   Calendar.getInstance();     
        c.set(Calendar.YEAR, year);  
        c.set(Calendar.MONTH, month);  
        int day = c.getActualMaximum(c.DAY_OF_MONTH);  
        c.set(Calendar.DAY_OF_MONTH, day);  
        return c.getTime();  
    }  
  
    /** 
     * 取得当前日期所在周的第一天 
     *  
     * @param date 
     * @return 
     */  
    public static Date getFirstDayOfWeek(Date date) {  
        Calendar c = new GregorianCalendar();  
        c.setFirstDayOfWeek(Calendar.MONDAY);  
        c.setTime(date);  
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday  
        return c.getTime();  
    }  
  
    /** 
     * 取得当前日期所在周的最后一天 
     *  
     * @param date 
     * @return 
     */  
    public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	
    public static void main(String [] args){
    	Date date = strToDate("20099");
    	System.out.println(date.toLocaleString());
    }
    
	public static String defaultDatePattern = "yyyy-MM-dd";

	public static String dateTimePattern = "yyyy-MM-dd HH:mm";

	/*
	 * static { //尝试试从messages_zh_Cn.properties中获取defaultDatePattner. try {
	 * Locale locale = LocaleContextHolder.getLocale(); defaultDatePattern =
	 * ResourceBundle.getBundle(Constants.MESSAGE_BUNDLE_KEY,
	 * locale).getString("date.default_format"); } catch
	 * (MissingResourceException mse) { do nothing } }
	 */
	/**
	 * 获得默认的 date pattern
	 */
	public static String getDatePattern() {
		return defaultDatePattern;
	}

	/**
	 * 现在是Date,只是为了代码统一而以
	 * @return
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 返回预设Format的当前日期字符串
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * 返回预设Format的当前日期字符串
	 */
	public static String getTodayTime() {
		Date today = new Date();
		return format(today, dateTimePattern);
	}

	/**
	 * 返回自定义当前pattern的当前日期字符串
	 */
	public static String getToday(String pattern) {
		Date today = new Date();
		return format(today, pattern);
	}

	/**
	 * 使用预设Format格式化Date成字符串
	 */
	public static String format(Date date) {
		return date == null ? "" : format(date, getDatePattern());
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 使用预设格式将字符串转为Date
	 */
	public static Date parse(String strDate) throws ParseException {
		 return (strDate != null && !"".equals(strDate)) ? parse(strDate, getDatePattern()) : null;
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		return (strDate != null && !"".equals(strDate)) ? new SimpleDateFormat(pattern).parse(strDate) : null;
	}

	/**
	 * 在日期上增加数个整月
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}
    /** 
     * 根据日期取得星期 
     *  add by wds
     * @param date 
     * @return 
     */  
    public static String getDayBydate(Date date) {  
    	 String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    	 Calendar cal = Calendar.getInstance();
         cal.setTime(date);

         int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
         if (w < 0)
             w = 0;
         
         return weekDays[w];
    }  
    /** 
     * 某日期是哪一年
     *  add by wds
     * @param date 
     * @return 
     */
	public static int getMyYear(Date date) {
   	 	Calendar cal = Calendar.getInstance();
   	 	cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}  
	
	public static int getMyMonth(Date date) {
		Calendar cal = Calendar.getInstance();
   	 	cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}
	
	public static Date getTomorrow(Date date) {
		 Calendar cld = Calendar.getInstance();
		  
		 cld.setTime(date);
		  
		 cld.add(Calendar.DAY_OF_MONTH, 1);
		 
		 return cld.getTime();
	}
	
	/**
	 * 验证str是否为日期
	 * @标题:
	 * @描述:验证格式:YYYYMMDD、YYYY_MM_DD、YYYYMMDDHHMISS、YYYYMMDD_HH_MI、YYYY_MM_DD_HH_MI、YYYYMMDDHHMISSSSS、YYYY_MM_DD_HH_MI_SS
	 * @param strTime
	 * @return null时返回false;true为日期，false不为日期
	 * @公司:广东数据通信网络有限公司
	 * @作者:lxm
	 * @日期:2009-11-24
	 */
	public static boolean strValidateDate(String strTime) {
    	if (strTime == null || strTime.trim().length() <= 0)
    		return false;
    	
		Date date = null;
		List<String> list = new ArrayList<String>(0);
		
		list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
		list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
		list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
		list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
		list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
		list.add(DATE_FORMAT_YYYY_MM_DD);
		//list.add(DATE_FORMAT_YY_MM_DD);
		list.add(DATE_FORMAT_YYYYMMDD);
		//list.add(DATE_FORMAT_YYYY_MM);
		//list.add(DATE_FORMAT_YYYYMM);
		//list.add(DATE_FORMAT_YYYY);
		
		// REVIEW 这个循环没有什么意义，仅仅是作为格式匹配，根据长度判断就可以了
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String format = (String) iter.next();
			if(strTime.indexOf("-")>0 && format.indexOf("-")<0)
				continue;
			if(strTime.indexOf("-")<0 && format.indexOf("-")>0)
				continue;
			if(strTime.length()>format.length())
				continue;
			date = strToDate(strTime.trim(), format);
			if (date != null)
				break;
		}
		
		if (date != null) {
			System.out.println("生成的日期:"+DateUtil.dateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS, "--null--"));
			return true;
		}
		return false;
	}
	
	/**
	 * 验证字符是否符合int数据
	 * @标题:
	 * @描述:
	 * @param strInt
	 * @return
	 * @公司:广东数据通信网络有限公司
	 * @作者:lxm
	 * @日期:2009-11-24
	 */
	public static boolean strValidateInt(String strInt){
		boolean rtn = false;
		
		if (strInt == null || strInt.trim().length() <= 0)
    		return false;
		
		try{
			Integer.parseInt(strInt.trim());
			rtn = true;
		}catch(Exception e){
			return false;
		}
		
		return rtn;
	}
	
	/**
	 * 验证字符是否符合float数据
	 * @标题:
	 * @描述:
	 * @param strInt
	 * @return
	 * @公司:广东数据通信网络有限公司
	 * @作者:lxm
	 * @日期:2009-11-24
	 */
	public static boolean strValidateFloat(String strFloat){
		boolean rtn = false;
		
		if (strFloat == null || strFloat.trim().length() <= 0)
    		return false;
		
		try{
			Float.parseFloat(strFloat.trim());
			rtn = true;
		}catch(Exception e){
			return false;
		}
		
		return rtn;
	}
}
