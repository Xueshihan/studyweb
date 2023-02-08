package cn.iatc.web.service.impl;

import cn.iatc.database.entity.StatisticDayPower;
import cn.iatc.web.mapper.StatisticDayPowerMapper;
import cn.iatc.web.service.StatisticDayPowerService;
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
    public List<StatisticDayPower> getStatisticDayPowerOfMonth(Long stationId, Integer year, Integer month) {
        return statisticDayPowerMapper.getStatisticDayPowerOfMonth(stationId, year, month);
    }

    @Override
    public List<StatisticDayPower> getStatisticDayPowerOfDay(Long stationId, Integer year, Integer month, Integer day) {
        return statisticDayPowerMapper.getStatisticDayPowerOfDay(stationId, year, month, day);
    }
}
