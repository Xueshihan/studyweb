package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.StatisticDayPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

public interface StatisticDayPowerService extends IService<StatisticDayPower> {
    public void insertBatch(List<StatisticPojo> statisticPojoList);

    public List<StatisticPojo> statisticMonthData(Integer year, Integer month);
}
