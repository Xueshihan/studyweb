package cn.iatc.web.service.impl;

import cn.iatc.database.entity.StatisticHourPower;
import cn.iatc.web.mapper.StatisticHourPowerMapper;
import cn.iatc.web.service.StatisticHourPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Slf4j
@Service
@Transactional
public class StatisticHourPowerServiceImpl extends ServiceImpl<StatisticHourPowerMapper, StatisticHourPower> implements StatisticHourPowerService {

    @Autowired
    private StatisticHourPowerMapper statisticHourPowerMapper;

    @Override
    public List<StatisticHourPower> getStatisticHourPowerOfDay(Long stationId, Date startDate, Date endDate) {
        return statisticHourPowerMapper.getStatisticHourPowerOfDay(stationId, startDate, endDate);
    }

    @Override
    public String getMaxActivePower(Long stationId) {
        return statisticHourPowerMapper.getMaxActivePower(stationId);
    }
}
