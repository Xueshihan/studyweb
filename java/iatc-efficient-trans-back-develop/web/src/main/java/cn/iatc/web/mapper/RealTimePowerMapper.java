package cn.iatc.web.mapper;

import cn.iatc.database.entity.RealTimePower;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public interface RealTimePowerMapper extends BaseMapper<RealTimePower> {

    public default List<RealTimePower> getRealTimePowerOfPeriod(Long stationId, Date startDate, Date endDate) {
        LambdaQueryWrapper<RealTimePower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RealTimePower::getStationId, stationId);
        wrapper.ge(RealTimePower::getCreatedTime, startDate);
        wrapper.le(RealTimePower::getCreatedTime, endDate);
        wrapper.orderByAsc(RealTimePower::getHour,RealTimePower::getPhase);
        return this.selectList(wrapper);
    }
}
