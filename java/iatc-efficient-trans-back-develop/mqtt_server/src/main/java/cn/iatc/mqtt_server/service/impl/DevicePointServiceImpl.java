package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.DevicePoint;
import cn.iatc.mqtt_server.mapper.DevicePointMapper;
import cn.iatc.mqtt_server.mapper.DeviceStationMapper;
import cn.iatc.mqtt_server.service.DevicePointService;
import cn.iatc.mqtt_server.service.DeviceStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class DevicePointServiceImpl extends ServiceImpl<DevicePointMapper, DevicePoint> implements DevicePointService {

    @Autowired
    private DevicePointMapper devicePointMapper;
    @Override
    public List<DevicePoint> findByDevicesBaseProperties(List<Long> deviceIds, List<Long> basePropertyIds) {
        return devicePointMapper.findByDevicesBaseProperties(deviceIds, basePropertyIds);
    }

    @Override
    public List<DevicePoint> findByDeviceTypesBaseProperties(List<Long> deviceTypeIds, List<Long> basePropertyIds) {
        return devicePointMapper.findByDeviceTypesBaseProperties(deviceTypeIds, basePropertyIds);
    }

    @Override
    public List<DevicePoint> findByDeviceType(Long deviceTypeId) {
        return devicePointMapper.findByDeviceType(deviceTypeId);
    }

    @Override
    public List<DevicePoint> findByDevice(Long deviceId) {
        return devicePointMapper.findByDevice(deviceId);
    }
}
