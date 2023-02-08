package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.RealTimePower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RealTimePowerService extends IService<RealTimePower> {

    public void insertBatch(List<RealTimePower> realTimePowerList);

    public void insertStatisticDayHour(List<StatisticPojo> statisticDayList, List<StatisticPojo> statisticHourList);
    // 统计小时数据
    public List<StatisticPojo> statisticHourData(Integer year, Integer month, Integer day);

    // 统计小时数据
    public List<StatisticPojo> statisticDayData(Integer year, Integer month, Integer day);
}
