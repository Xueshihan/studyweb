package cn.iatc.web.service;

import cn.iatc.database.entity.RealTimePower;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Date;
import java.util.List;

public interface RealTimePowerService extends IService<RealTimePower> {
    List<RealTimePower> getRealTimePowerOfPeriod(Long stationId, Date startDate, Date endDate);
}
