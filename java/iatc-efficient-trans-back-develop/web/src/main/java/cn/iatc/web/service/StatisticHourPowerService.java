package cn.iatc.web.service;

import cn.iatc.database.entity.StatisticHourPower;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface StatisticHourPowerService extends IService<StatisticHourPower> {

    List<StatisticHourPower> getStatisticHourPowerOfDay(Long stationId, Date startDate, Date endDate);

    String getMaxActivePower(Long stationId);

}
