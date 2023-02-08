package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.StatisticMonthPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StatisticMonthPowerService extends IService<StatisticMonthPower> {
    public void insertBatch(List<StatisticPojo> statisticPojoList);
}
