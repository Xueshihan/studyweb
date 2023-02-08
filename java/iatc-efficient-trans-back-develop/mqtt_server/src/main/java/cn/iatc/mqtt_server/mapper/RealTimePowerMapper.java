package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.RealTimePower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RealTimePowerMapper extends BaseMapper<RealTimePower> {
    public void insertBatch(List<RealTimePower> realTimePowerList);

    public List<StatisticPojo> statisticHourData(Integer year, Integer month, Integer day);

    public List<StatisticPojo> statisticDayData(Integer year, Integer month, Integer day);
}
