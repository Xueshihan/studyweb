package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.RealTimePower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import cn.iatc.mqtt_server.mapper.RealTimePowerMapper;
import cn.iatc.mqtt_server.mapper.StatisticDayPowerMapper;
import cn.iatc.mqtt_server.mapper.StatisticHourPowerMapper;
import cn.iatc.mqtt_server.service.RealTimePowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class RealTimePowerServiceImpl extends ServiceImpl<RealTimePowerMapper, RealTimePower> implements RealTimePowerService {

    @Autowired
    private RealTimePowerMapper realTimePowerMapper;

    @Autowired
    private StatisticDayPowerMapper statisticDayPowerMapper;

    @Autowired
    private StatisticHourPowerMapper statisticHourPowerMapper;

    @Override
    public void insertBatch(List<RealTimePower> realTimePowerList) {
        realTimePowerMapper.insertBatch(realTimePowerList);
    }

    @Override
    public void insertStatisticDayHour(List<StatisticPojo> statisticDayList, List<StatisticPojo> statisticHourList) {
        if (statisticDayList.size() > 0) {
            statisticDayPowerMapper.insertBatch(statisticDayList);
        }

        if (statisticHourList.size() > 0) {
            statisticHourPowerMapper.insertBatch(statisticHourList);
        }
    }

    @Override
    public List<StatisticPojo> statisticHourData(Integer year, Integer month, Integer day) {
        return realTimePowerMapper.statisticHourData(year, month, day);
    }

    @Override
    public List<StatisticPojo> statisticDayData(Integer year, Integer month, Integer day) {
        return realTimePowerMapper.statisticDayData(year, month, day);
    }
}
