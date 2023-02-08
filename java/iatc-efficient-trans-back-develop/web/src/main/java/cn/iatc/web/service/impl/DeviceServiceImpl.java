package cn.iatc.web.service.impl;

import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.DeviceStation;
import cn.iatc.web.bean.device.DevicePojo;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.DeviceMapper;
import cn.iatc.web.mapper.DeviceStationMapper;
import cn.iatc.web.service.DeviceService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Autowired
    private DeviceStationMapper deviceStationMapper;


    @Override
    public Device createDevice(Long stationId, Device device) {
        Date createdTime = new Date();
        device.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        device.setCreatedTime(createdTime);
        device.setUpdatedTime(createdTime);
        deviceMapper.insert(device);

        DeviceStation deviceStation = new DeviceStation();
        deviceStation.setDeviceId(device.getId());
        deviceStation.setStationId(stationId);
        deviceStationMapper.insert(deviceStation);
        return device;
    }

    @Override
    public void updateDevice(Device device) {
        // 分步写是因为用对象的话，为空字段不会更新为NULL
        LambdaUpdateWrapper<Device> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Device::getName, device.getName());
        wrapper.set(Device::getSn, device.getSn());
        wrapper.set(Device::getDeviceTypeId, device.getDeviceTypeId());
        wrapper.set(Device::getPointId, device.getPointId());
        wrapper.set(Device::getCapacity, device.getCapacity());
        wrapper.set(Device::getGatewayId, device.getGatewayId());
        wrapper.set(Device::getAddress, device.getAddress());
        wrapper.set(Device::getRemark, device.getRemark());
        wrapper.set(Device::getUpdatedTime, new Date());
        wrapper.eq(Device::getId, device.getId());
        deviceMapper.update(null, wrapper);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        deviceStationMapper.deleteByDeviceIds(ids);
        deviceMapper.deleteByIds(ids);
    }

    @Override
    public Device findById(Long id) {
        return deviceMapper.findById(id);
    }

    @Override
    public Device findBySn(String sn) {
        return deviceMapper.findBySn(sn);
    }

    @Override
    public IPage<DevicePojo> findListLike(String name, Long baseDeviceId, Integer pageIndex, Integer num) {
        IPage<DevicePojo> page = new Page<>(pageIndex, num);
        return deviceMapper.findListByNameBase(page, name, baseDeviceId);
    }

    @Override
    public List<DevicePojo> findListByStation(Long stationId) {
        return deviceMapper.findListByStation(stationId);
    }
}
