package cn.iatc.web.mapper;

import cn.iatc.database.entity.StatisticMonthPower;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface StatisticMonthPowerMapper extends BaseMapper<StatisticMonthPower> {

    public default List<StatisticMonthPower> getStatisticMonthPowerOfYear(List<Long> stationIdList, Integer year) {
        LambdaQueryWrapper<StatisticMonthPower> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StatisticMonthPower::getStationId, stationIdList);
        wrapper.eq(StatisticMonthPower::getYear, year);
        wrapper.orderByAsc(StatisticMonthPower::getStationId,StatisticMonthPower::getMonth,StatisticMonthPower::getPhase);
        return this.selectList(wrapper);
    }

    public default List<StatisticMonthPower> getStatisticMonthPowerOfMonth(Long stationId, Integer year, Integer month) {
        LambdaQueryWrapper<StatisticMonthPower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticMonthPower::getStationId, stationId);
        wrapper.eq(StatisticMonthPower::getYear, year);
        wrapper.eq(StatisticMonthPower::getMonth, month);
        wrapper.orderByAsc(StatisticMonthPower::getMonth,StatisticMonthPower::getPhase);
        return this.selectList(wrapper);
    }

    public List<String> getStatisticMonthPowerOfAll(Long stationId);

}
