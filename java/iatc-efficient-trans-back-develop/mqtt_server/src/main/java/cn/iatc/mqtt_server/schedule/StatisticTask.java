package cn.iatc.mqtt_server.schedule;

import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import cn.iatc.mqtt_server.common.context.SpringContextHolder;
import cn.iatc.mqtt_server.constants.CommonConstants;
import cn.iatc.mqtt_server.service.RealTimePowerService;
import cn.iatc.mqtt_server.service.StatisticDayPowerService;
import cn.iatc.mqtt_server.service.StatisticMonthPowerService;
import cn.iatc.mqtt_server.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

// 统计数据处理定时器
@Slf4j
@Component
public class StatisticTask {

    // 1号
    private static final Integer FIRST_DAY = 1;
    private static final RealTimePowerService realTimePowerService;
    private static final StatisticDayPowerService statisticDayPowerService;
    private static final StatisticMonthPowerService statisticMonthPowerService;

    static {
        realTimePowerService = SpringContextHolder.getBean(RealTimePowerService.class);
        statisticDayPowerService = SpringContextHolder.getBean(StatisticDayPowerService.class);
        statisticMonthPowerService = SpringContextHolder.getBean(StatisticMonthPowerService.class);
    }

    private static Integer ONE_TIME = 60 * 60 * 24 * 1000;
    // 每天0时32分统计数据
//    @Scheduled(cron = "0 32 0 * * ?")
    @Scheduled(cron = "0 32 0 * * ?")
    public void handle() {
        log.info("StatisticTask time:{}", new Date());
        this.handleData();
    }

    private void handleData() {
        Calendar calendar = TimeUtil.getCurCalendar();
        long curTimeSample = TimeUtil.getCurTimeSample();
        calendar.setTimeInMillis(curTimeSample);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        List<StatisticPojo> statisticHourPojoList = this.handleHourStatistic(curTimeSample);
        List<StatisticPojo> statisticDayPojoList = this.handleDayStatistic(curTimeSample);
        log.info("statisticHourPojoList:{}", statisticHourPojoList);
        log.info("statisticDayPojoList:{}", statisticDayPojoList);
        realTimePowerService.insertStatisticDayHour(statisticDayPojoList, statisticHourPojoList);
        if (day.equals(FIRST_DAY)) {
            // 处理月统计
            List<StatisticPojo> statisticMonthPojoList = this.handleMonthStatistic(curTimeSample);
            if(statisticMonthPojoList.size() > 0) {
                statisticMonthPowerService.insertBatch(statisticMonthPojoList);
            }
        }
    }

    // 处理小时统计数据
    private List<StatisticPojo> handleHourStatistic(long curTimeSample) {
        long lastTimeSample = curTimeSample - ONE_TIME;
        Calendar calendar = TimeUtil.getCurCalendar();
        calendar.setTimeInMillis(lastTimeSample);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        List<StatisticPojo> resultList = realTimePowerService.statisticHourData(year, month, day);
        for (StatisticPojo statisticPojo: resultList) {
            statisticPojo.setYear(year);
            statisticPojo.setMonth(month);
            statisticPojo.setDay(day);
            statisticPojo.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            calendar.set(Calendar.HOUR, statisticPojo.getHour());
            Date createTime = calendar.getTime();
            statisticPojo.setCreatedTime(createTime);
            statisticPojo.setUpdatedTime(new Date());
        }
        return resultList;
    }

    // 处理天统计数据
    private List<StatisticPojo> handleDayStatistic(long curTimeSample) {
        long lastTimeSample = curTimeSample - ONE_TIME;
        Calendar calendar = TimeUtil.getCurCalendar();
        calendar.setTimeInMillis(lastTimeSample);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        List<StatisticPojo> resultList = realTimePowerService.statisticDayData(year, month, day);
        for (StatisticPojo statisticPojo: resultList) {
            statisticPojo.setYear(year);
            statisticPojo.setMonth(month);
            statisticPojo.setDay(day);
            statisticPojo.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            Date createTime = calendar.getTime();
            statisticPojo.setCreatedTime(createTime);
            statisticPojo.setUpdatedTime(new Date());
        }
        return resultList;
    }

    // 处理月统计数据
    private List<StatisticPojo> handleMonthStatistic(long curTimeSample) {
        long lastTimeSample = curTimeSample - ONE_TIME;
        Calendar calendar = TimeUtil.getCurCalendar();
        calendar.setTimeInMillis(lastTimeSample);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        List<StatisticPojo> resultList = statisticDayPowerService.statisticMonthData(year, month);
        for (StatisticPojo statisticPojo: resultList) {
            statisticPojo.setYear(year);
            statisticPojo.setMonth(month);
            statisticPojo.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            Date createTime = calendar.getTime();
            statisticPojo.setCreatedTime(createTime);
            statisticPojo.setUpdatedTime(new Date());
        }
        return resultList;
    }
}
