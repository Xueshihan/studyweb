package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.StatisticMonthPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import cn.iatc.mqtt_server.mapper.StatisticMonthPowerMapper;
import cn.iatc.mqtt_server.service.StatisticMonthPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class StatisticMonthPowerServiceImpl extends ServiceImpl<StatisticMonthPowerMapper, StatisticMonthPower> implements StatisticMonthPowerService {
    @Autowired
    private StatisticMonthPowerMapper statisticMonthPowerMapper;
    @Override
    public void insertBatch(List<StatisticPojo> statisticPojoList) {
        statisticMonthPowerMapper.insertBatch(statisticPojoList);
    }
}
