package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.OriginalPower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface OriginalPowerMapper extends BaseMapper<OriginalPower> {

    public void insertBatch(List<OriginalPower> originalPowerList);

    public OriginalPower findByCodeTypeTime(String code, String type, Date startTime, Date endTime);

    public OriginalPower findLatestByCodeType(String code, String type, Date startTime);
}
