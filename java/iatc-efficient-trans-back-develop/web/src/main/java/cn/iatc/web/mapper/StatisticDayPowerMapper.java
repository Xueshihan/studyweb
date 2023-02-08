package cn.iatc.web.mapper;

import cn.iatc.database.entity.StatisticDayPower;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface StatisticDayPowerMapper extends BaseMapper<StatisticDayPower> {

    public default List<StatisticDayPower> getStatisticDayPowerOfMonth(Long stationId, Integer year, Integer month) {
        LambdaQueryWrapper<StatisticDayPower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticDayPower::getStationId, stationId);
        wrapper.eq(StatisticDayPower::getYear, year);
        wrapper.eq(StatisticDayPower::getMonth, month);
        wrapper.orderByAsc(StatisticDayPower::getDay,StatisticDayPower::getPhase);
        return this.selectList(wrapper);
    }

    public default List<StatisticDayPower> getStatisticDayPowerOfDay(Long stationId, Integer year, Integer month, Integer day) {
        LambdaQueryWrapper<StatisticDayPower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticDayPower::getStationId, stationId);
        wrapper.eq(StatisticDayPower::getYear, year);
        wrapper.eq(StatisticDayPower::getMonth, month);
        wrapper.eq(StatisticDayPower::getDay, day);
        wrapper.orderByAsc(StatisticDayPower::getDay,StatisticDayPower::getPhase);
        return this.selectList(wrapper);
    }
}
