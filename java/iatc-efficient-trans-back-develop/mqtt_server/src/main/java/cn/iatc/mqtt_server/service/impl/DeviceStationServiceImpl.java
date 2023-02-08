package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.DeviceStation;
import cn.iatc.mqtt_server.mapper.DeviceStationMapper;
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
public class DeviceStationServiceImpl extends ServiceImpl<DeviceStationMapper, DeviceStation> implements DeviceStationService {

    @Autowired
    private DeviceStationMapper deviceStationMapper;

    @Override
    public List<Long> findDevicesByStation(Long stationId) {
        return deviceStationMapper.findDevicesByStation(stationId);
    }
}
