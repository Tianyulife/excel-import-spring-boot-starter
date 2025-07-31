package io.github.tianyulife.excelimport.util;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static  String YYYYMM = "yyyyMM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyy-MM-ddHH:mm:ss","yyyy/MM/ddHH:mm:ss","yyyy.MM.ddHH:mm:ss"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    public static Date parseDateUtil(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            throw new RuntimeException("时间格式错误");
        }
    }

    public static LocalDateTime parseStringToLocalDateTime(String dateTimeString,String pattern) {
        // Define the pattern that matches the input string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Parse the string to LocalDateTime
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static Date parseLoaclDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime parseDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 判断日期是否符合mysql datetime 类型的值
     * @param date
     * @return true 符合，false 不符合
     */
    public static boolean checkMysqlDateTime(Date date){
        if(date==null) return true;
        int year= DateUtil.year(date);
        if (year<1000 || year>9999) return false;
        return true;
    }

    /**
     * 一天的开始 <br/>
     * 结果：2017-03-01 00:00:00
     * @param date
     * @return
     */
    public static Date beginOfDay(Date date){
        if (date==null) return null;
        return DateUtil.beginOfDay(date);
    }
    public static Date beginOfDayObj(Object dateStr){
        if(StringUtils.isNull(dateStr)) return null;
        Date date=DateUtils.parseDate(dateStr);
        return beginOfDay(date);
    }


    /**
     * 一天的结束，<br/>
     * 结果：2017-03-01 23:59:59
     * @param date
     * @return
     */
    public static Date endOfDay(Date date){
        if (date==null) return null;
        return DateUtil.endOfDay(date);
    }

    public static Date endOfDayObj(Object dateStr){
        if (StringUtils.isNull(dateStr)) return null;
        Date date=DateUtils.parseDate(dateStr);
        return endOfDay(date);
    }

    /**
     * 判断日期是否为今天
     */
    public static boolean isNow(Date date) {
        Date now = new Date();
        String day = dateTime(date);
        String nowDay = dateTime(now);
        return day.equals(nowDay);
    }

    public static boolean compareDate(Date source, Date target){
      return   DateUtil.betweenDay(source,target, true) == 0;
    }


    /**
     * 判断日期是否为昨天
     */

    public static boolean isYesterday(Date date){
        if (ObjectUtils.isEmpty(date)) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,  -1);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return calendar.get(Calendar.YEAR) == instance.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == instance.get(Calendar.MONTH) && calendar.get(Calendar.DATE) == instance.get(Calendar.DATE);
    }

    /**
     * 得到两个日期之间的季度的最后一天
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getSeasonLastDayBetween(Date startDate,Date endDate){
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(startDate);
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        while (c.getTime().getTime() < endDate.getTime()){
            int month = c.get(Calendar.MONTH);
            switch (month) {
                case Calendar.JANUARY:
                case Calendar.FEBRUARY:
                case Calendar.MARCH:
                    c.set(Calendar.MONTH, Calendar.MARCH);
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    if (c.getTime().getTime() < endDate.getTime()){
                        dates.add(c.getTime());
                    }
                    c.add(Calendar.DATE, 1);
                    break;
                case Calendar.APRIL:
                case Calendar.MAY:
                case Calendar.JUNE:
                    c.set(Calendar.MONTH, Calendar.JUNE);
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    if (c.getTime().getTime() < endDate.getTime()){
                        dates.add(c.getTime());
                    }
                    c.add(Calendar.DATE, 1);
                    break;
                case Calendar.JULY:
                case Calendar.AUGUST:
                case Calendar.SEPTEMBER:
                    c.set(Calendar.MONTH, Calendar.SEPTEMBER);
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    if (c.getTime().getTime() < endDate.getTime()){
                        dates.add(c.getTime());
                    }
                    c.add(Calendar.DATE, 1);
                    break;
                case Calendar.OCTOBER:
                case Calendar.NOVEMBER:
                case Calendar.DECEMBER:
                    c.set(Calendar.MONTH, Calendar.DECEMBER);
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    if (c.getTime().getTime() < endDate.getTime()){
                        dates.add(c.getTime());
                    }
                    c.add(Calendar.DATE, 1);
                    break;
            }
        }
        dates.add(endDate);
        return dates;
    }

    /**
     * 根据季度得到开始本季度的开始日期和结束日期
     * @param quarter
     * @return
     */
    public static Map<String,Date> getStartAndEndDateBySeason(String quarter){
        String[] parts = quarter.split("-");
        int year = Integer.parseInt(parts[0]);
        String quarterCode = parts[1];
        Date startDate = getStartDate(year, quarterCode);
        Date endDate = getEndDate(year, quarterCode);
        Map<String, Date> dateHashMap = new HashMap<>();
        dateHashMap.put("startDate",startDate);
        dateHashMap.put("endDate", endDate);
        return dateHashMap;
    }

    private static Date getEndDate(int year,String quarterCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        switch (quarterCode) {
            case "Q1":
                calendar.set(Calendar.MONTH,Calendar.MARCH);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case "Q2":
                calendar.set(Calendar.MONTH,Calendar.JUNE);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case "Q3":
                calendar.set(Calendar.MONTH,Calendar.SEPTEMBER);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case "Q4":
                calendar.set(Calendar.MONTH,Calendar.DECEMBER);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            default:
                throw new IllegalArgumentException("Invalid quarter code: " + quarterCode);
        }

        return endOfDay(calendar.getTime());
    }

    private static Date getStartDate(int year,String quarterCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        switch (quarterCode) {
            case "Q1":
                calendar.set(Calendar.MONTH,Calendar.JANUARY);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case "Q2":
                calendar.set(Calendar.MONTH,Calendar.APRIL);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case "Q3":
                calendar.set(Calendar.MONTH,Calendar.JULY);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case "Q4":
                calendar.set(Calendar.MONTH,Calendar.OCTOBER);
                calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            default:
                throw new IllegalArgumentException("Invalid quarter code: " + quarterCode);
        }

        return beginOfDay(calendar.getTime());
    }

    //获取某年某月的第一天
    public static Date getFirstDayOfMonth(String month){
        int year = Integer.parseInt(month.substring(0,4));  //截取出年份，并将其转化为int
        int month1 = Integer.parseInt(month.substring(5,7));    //截去除月份，并将其转为int

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);    //设置年份
        cal.set(Calendar.MONTH, month1-1);  //设置月份
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH); //获取某月最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);   //设置日历中月份的最小天数
        return beginOfDay(cal.getTime());

    }

    //获取某年某月最后一天
    public static Date getLastDayOfMonth(String month){
        int year = Integer.parseInt(month.substring(0,4));  //截取出年份，并将其转化为int
        int month1 = Integer.parseInt(month.substring(5,7));    //截去除月份，并将其转为int

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);    //设置年份
        cal.set(Calendar.MONTH, month1-1);  //设置月份
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  //获取某月最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);    //设置日历中月份的最大天数
        return endOfDay(cal.getTime());
    }

    public static Date getSpecificDay(Date date,int day){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date getMaxDate(){
        return new Date(new GregorianCalendar(9999, Calendar.DECEMBER, 31).getTimeInMillis());
    }


    public static Date addTime(Date now, int years, int months, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);// 设置起时间
        if (years != 0) {
            cal.add(Calendar.YEAR, years);
        }
        if (months != 0) {
            cal.add(Calendar.MONTH, months);
        }
        if (days != 0) {
            cal.add(Calendar.DATE, days);
        }

        return cal.getTime();
    }

}
