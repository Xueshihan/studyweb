package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.StatisticHourPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StatisticHourPowerService extends IService<StatisticHourPower> {

    public void insertBatch(List<StatisticPojo> statisticPojoList);

}
