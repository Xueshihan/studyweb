package cn.iatc.web.service;

import cn.iatc.database.entity.StatisticMonthPower;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface StatisticMonthPowerService extends IService<StatisticMonthPower> {


    List<StatisticMonthPower> getStatisticMonthPowerOfYear(List<Long> stationId, Integer year);

    List<StatisticMonthPower> getStatisticMonthPowerOfMonth(Long stationId, Integer year, Integer month);

    List<String> getStatisticMonthPowerOfAll(Long stationId);
}
