package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.StatisticHourPower;
import cn.iatc.mqtt_server.bean.statistic.StatisticPojo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StatisticHourPowerMapper extends BaseMapper<StatisticHourPower> {
    public void insertBatch(List<StatisticPojo> statisticPojoList);
}
