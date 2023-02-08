package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.DeviceType;
import cn.iatc.mqtt_server.mapper.DeviceTypeMapper;
import cn.iatc.mqtt_server.service.DeviceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements DeviceTypeService {

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<DeviceType> findListByFactory(Long factoryId) {
        return deviceTypeMapper.findListByFactory(factoryId);
    }
}
