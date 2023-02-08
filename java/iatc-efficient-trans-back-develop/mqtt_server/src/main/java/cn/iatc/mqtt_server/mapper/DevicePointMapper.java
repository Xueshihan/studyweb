package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.DevicePoint;
import cn.iatc.mqtt_server.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DevicePointMapper extends BaseMapper<DevicePoint> {

    public default List<DevicePoint> findByDevicesBaseProperties(List<Long> deviceIds, List<Long> basePropertyIds) {
        LambdaQueryWrapper<DevicePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DevicePoint::getDeviceId, deviceIds);
        wrapper.in(DevicePoint::getBasePropertyId, basePropertyIds);
        wrapper.eq(DevicePoint::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default List<DevicePoint> findByDeviceTypesBaseProperties(List<Long> deviceTypeIds, List<Long> basePropertyIds) {
        LambdaQueryWrapper<DevicePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DevicePoint::getDeviceTypeId, deviceTypeIds);
        wrapper.in(DevicePoint::getBasePropertyId, basePropertyIds);
        wrapper.eq(DevicePoint::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default List<DevicePoint> findByDeviceType(Long deviceTypeId) {
        LambdaQueryWrapper<DevicePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DevicePoint::getDeviceTypeId, deviceTypeId);
        wrapper.eq(DevicePoint::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default List<DevicePoint> findByDevice(Long deviceId) {
        LambdaQueryWrapper<DevicePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DevicePoint::getDeviceId, deviceId);
        wrapper.eq(DevicePoint::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }
}
