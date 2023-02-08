package cn.iatc.web.service.impl;

import cn.iatc.database.entity.StatisticMonthPower;
import cn.iatc.web.mapper.StatisticMonthPowerMapper;
import cn.iatc.web.service.StatisticMonthPowerService;
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
    public List<StatisticMonthPower> getStatisticMonthPowerOfYear(List<Long> stationId, Integer year) {
        return statisticMonthPowerMapper.getStatisticMonthPowerOfYear(stationId, year);
    }

    @Override
    public List<StatisticMonthPower> getStatisticMonthPowerOfMonth(Long stationId, Integer year, Integer month) {
        return statisticMonthPowerMapper.getStatisticMonthPowerOfMonth(stationId, year, month);
    }

    @Override
    public List<String> getStatisticMonthPowerOfAll(Long stationId) {
        return statisticMonthPowerMapper.getStatisticMonthPowerOfAll(stationId);
    }
}
