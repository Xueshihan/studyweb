package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.OriginalPower;
import cn.iatc.mqtt_server.mapper.OriginalPowerMapper;
import cn.iatc.mqtt_server.service.OriginalPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OriginalPowerServiceImpl extends ServiceImpl<OriginalPowerMapper, OriginalPower> implements OriginalPowerService {

    @Autowired
    private OriginalPowerMapper originalPowerMapper;

    @Override
    public void insertBatch(List<OriginalPower> originalPowerList) {
        originalPowerMapper.insertBatch(originalPowerList);
    }

    @Override
    public OriginalPower findByCodeTypeTime(String code, String type, Date startTime, Date endTime) {
        return originalPowerMapper.findByCodeTypeTime(code, type, startTime, endTime);
    }

    @Override
    public OriginalPower findLatestByCodeType(String code, String type, Date startTime) {
        return originalPowerMapper.findLatestByCodeType(code, type, startTime);
    }
}
