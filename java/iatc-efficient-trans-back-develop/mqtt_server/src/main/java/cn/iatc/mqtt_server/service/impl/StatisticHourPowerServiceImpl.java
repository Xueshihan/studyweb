package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.StatisticHourPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import cn.iatc.mqtt_server.mapper.StatisticHourPowerMapper;
import cn.iatc.mqtt_server.service.StatisticHourPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class StatisticHourPowerServiceImpl extends ServiceImpl<StatisticHourPowerMapper, StatisticHourPower> implements StatisticHourPowerService {
    @Autowired
    private StatisticHourPowerMapper statisticHourPowerMapper;

    @Override
    public void insertBatch(List<StatisticPojo> statisticPojoList) {
        statisticHourPowerMapper.insertBatch(statisticPojoList);
    }
}
