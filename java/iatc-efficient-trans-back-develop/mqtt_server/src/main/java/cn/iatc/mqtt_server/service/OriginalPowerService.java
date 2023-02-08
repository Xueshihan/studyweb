package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.OriginalPower;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface OriginalPowerService extends IService<OriginalPower> {

    public void insertBatch(List<OriginalPower> originalPowerList);

    public OriginalPower findByCodeTypeTime(String code, String type, Date startTime, Date endTime);

    public OriginalPower findLatestByCodeType(String code, String type, Date startTime);

}
