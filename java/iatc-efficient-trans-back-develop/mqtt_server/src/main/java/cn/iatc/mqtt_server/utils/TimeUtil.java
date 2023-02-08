package cn.iatc.mqtt_server.utils;

import cn.hutool.core.date.DateUtil;
import cn.iatc.mqtt_server.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TimeUtil {

    public static Calendar getCurCalendar() {
        return Calendar.getInstance();
    }

    public static String dateFormat(Date date, String format) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//        return simpleDateFormat.format(date);
        return DateUtil.format(date, format);
    }

    public static Long getCurTimeSample() {
        Date date = new Date();
        return date.getTime();
    }

    public static Long dateToTime(Date date) {
        return date.getTime();
    }

    public static Date timeToDate(long time) {
        String timeStr = String.valueOf(time);
        if (timeStr.length() == 10) {
            time = time * 1000;
        }
         return DateUtil.date(time);
    }

    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("parseDate execption:{}", e.toString());
        }
        return date;
    }

    public static void main(String[] args) {
//        String year = DateUtil.format(new Date(), "yyyy-MM-dd");
//        String year = dateFormat(new Date(), "HH:mm:ss");
//        System.out.println(year);
//        Date date = timeToDate(1672118608019L);
//        Date curDate = new Date();
//        Date curDate2 = new Date();
//        System.out.println( date.compareTo(curDate));

        Date date1 = parseDate("2023-01-29 09:21:14", "yyyy-MM-dd HH:mm:ss");
        System.out.println( date1);

        Long curTimeSample = TimeUtil.getCurTimeSample();
        Long startTimeSample = curTimeSample - CommonConstants.COLLECT_BASE_PERIOD * CommonConstants.COLLECT_NUM;
        Long endTimeSample = curTimeSample - CommonConstants.COLLECT_BASE_PERIOD;
        Calendar calendar = getCurCalendar();
        calendar.setTimeInMillis(startTimeSample);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();
        calendar.setTimeInMillis(endTimeSample);
        calendar.set(Calendar.SECOND, 0);
        Date end = calendar.getTime();

        System.out.println("start:" + start + "end:" + end);
    }
}
