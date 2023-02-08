package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.DeviceStation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeviceStationService extends IService<DeviceStation> {
    public List<Long> findDevicesByStation(Long stationId);
}
