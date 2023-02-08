package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.DevicePoint;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DevicePointService extends IService<DevicePoint> {

    public List<DevicePoint> findByDevicesBaseProperties(List<Long> deviceIds, List<Long> basePropertyIds);
    public List<DevicePoint> findByDeviceTypesBaseProperties(List<Long> deviceTypeIds, List<Long> basePropertyIds);

    public List<DevicePoint> findByDeviceType(Long deviceTypeId);

    public List<DevicePoint> findByDevice(Long deviceId);
}
