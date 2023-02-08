package cn.iatc.web.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtil {

    public static Calendar getCurCalendar() {
        return Calendar.getInstance();
    }

    public static String dateFormat(Date date, String format) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//        return simpleDateFormat.format(date);
        return DateUtil.format(date, format);
    }
    /**
     * 转化开始时间
     * @return
     */
    public static Date formatStartDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);//System.err.println("startTime:"+dateFormat.format(new Date()));
        String startTime = format.format(date)+" 00:00:00";//System.err.println("startTime:"+startTime);
        return dateFormat.parse(startTime);
    }
    /**
     * 转化结束时间
     * @return
     */
    public static Date formatEndDate(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);//System.err.println("endTime:"+dateFormat.format(new Date()));
        String endTime = format.format(date)+" 23:59:59";//System.err.println("endTime:"+endTime);
        return dateFormat.parse(endTime);
    }

    public static Long dateToTime(Date date) {
        return date.getTime();
    }

    public static Date timeToDate(long time) {
        return DateUtil.date(time);
    }

    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void main(String[] args) {
//        String year = DateUtil.format(new Date(), "yyyy-MM-dd");
        String year = dateFormat(new Date(), "HH:mm:ss");
        System.out.println(year);
        Date date = timeToDate(1672118608019L);
        Date curDate = new Date();
        Date curDate2 = new Date();
        System.out.println( date.compareTo(curDate));
    }

    /**
     * 计算两时间之间的天数
     * @return
     */
    public static int getDiffDays(Date d1, Date d2) {
        try {
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return (int) days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 获取指定日期当前年的第一天
     * @param date
     * @return
     */
    public static Date getYearFirstDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }
    /**
     * 判断时间范围是否不合法
     * @return
     */
    public static void judgeTimePeriodValid(Date startDate,Date endDate) {
        if (startDate!=null&&endDate!=null) {
            if(startDate.compareTo(endDate)>0){
                throw new BaseException(Status.PARAMETER_INVALID);
            }
        }
    }

    /**
     * 获取时间截取值
     * @return
     */
    public static int getDateFormatByType(Date date, String type) {
        if (date!=null) {
            Calendar calendar = TimeUtil.getCurCalendar();
            calendar.setTime(date);
            if(type.equals("year")){
                return calendar.get(Calendar.YEAR);
            }else if(type.equals("month")){
                return calendar.get(Calendar.MONTH) + 1;
            }else{
                return calendar.get(Calendar.DAY_OF_MONTH);
            }
        }else{
            return 0;
        }
    }

    /**
     * 获取上个月的时间
     * @return
     */
    public static Date getLastMonth() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.add(Calendar.MONTH,-1);
        date = calendar.getTime();
        return date;
    }
    /**
     * 获取昨天的时间
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }

}
