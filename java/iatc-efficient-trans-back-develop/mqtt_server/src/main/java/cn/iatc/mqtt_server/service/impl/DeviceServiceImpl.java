package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.DeviceStation;
import cn.iatc.mqtt_server.constants.CommonConstants;
import cn.iatc.mqtt_server.mapper.DeviceMapper;
import cn.iatc.mqtt_server.mapper.DeviceStationMapper;
import cn.iatc.mqtt_server.service.DeviceService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Device findById(Long id) {
        return deviceMapper.findById(id);
    }

    @Override
    public Device findBySn(String sn) {
        return deviceMapper.findBySn(sn);
    }

    @Override
    public List<Device> findListByStation(Long stationId) {
        return deviceMapper.findListByStation(stationId);
    }

}
