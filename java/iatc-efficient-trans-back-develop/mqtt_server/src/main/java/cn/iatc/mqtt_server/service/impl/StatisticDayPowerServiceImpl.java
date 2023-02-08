package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.StatisticDayPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import cn.iatc.mqtt_server.mapper.StatisticDayPowerMapper;
import cn.iatc.mqtt_server.service.StatisticDayPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class StatisticDayPowerServiceImpl extends ServiceImpl<StatisticDayPowerMapper, StatisticDayPower> implements StatisticDayPowerService {

    @Autowired
    private StatisticDayPowerMapper statisticDayPowerMapper;

    @Override
    public void insertBatch(List<StatisticPojo> statisticPojoList) {
        statisticDayPowerMapper.insertBatch(statisticPojoList);
    }

    @Override
    public List<StatisticPojo> statisticMonthData(Integer year, Integer month) {
        return null;
    }
}
