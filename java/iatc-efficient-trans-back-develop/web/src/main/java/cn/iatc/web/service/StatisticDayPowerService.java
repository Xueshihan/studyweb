package cn.iatc.web.service;

import cn.iatc.database.entity.StatisticDayPower;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface StatisticDayPowerService extends IService<StatisticDayPower> {

    List<StatisticDayPower> getStatisticDayPowerOfMonth(Long stationId, Integer year, Integer month);

    List<StatisticDayPower> getStatisticDayPowerOfDay(Long stationId, Integer year, Integer month, Integer day);
}
