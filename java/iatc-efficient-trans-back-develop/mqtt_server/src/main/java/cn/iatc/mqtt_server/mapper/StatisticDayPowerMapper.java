package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.StatisticDayPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public interface StatisticDayPowerMapper extends BaseMapper<StatisticDayPower> {
    public void insertBatch(List<StatisticPojo> statisticPojoList);

    public List<StatisticPojo> statisticMonthData(Integer year, Integer month);
}
