package cn.iatc.web.mapper;

import cn.iatc.database.entity.StatisticHourPower;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface StatisticHourPowerMapper extends BaseMapper<StatisticHourPower> {

    public default List<StatisticHourPower> getStatisticHourPowerOfDay(Long stationId, Date startDate, Date endDate) {
        LambdaQueryWrapper<StatisticHourPower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatisticHourPower::getStationId, stationId);
        wrapper.ge(StatisticHourPower::getCreatedTime, startDate);
        wrapper.le(StatisticHourPower::getCreatedTime, endDate);
        wrapper.orderByAsc(StatisticHourPower::getHour,StatisticHourPower::getPhase);
        return this.selectList(wrapper);
    }

    public String getMaxActivePower(Long stationId);

}
