package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.Device;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeviceService extends IService<Device> {

    public Device findById(Long id);
    public Device findBySn(String sn);
    public List<Device> findListByStation(Long stationId);
}
