package cn.iatc.web.mapper;

import cn.iatc.database.entity.Device;
import cn.iatc.web.bean.device.DevicePojo;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface DeviceMapper extends BaseMapper<Device> {

    public Device findById(Long id);

    public default void deleteByIds(List<Long> ids) {
        LambdaUpdateWrapper<Device> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Device::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(Device::getUpdatedTime, new Date());
        wrapper.in(Device::getId, ids);
        this.update(null, wrapper);
    }

    public default Device findBySn(String sn) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getSn, sn);
        wrapper.eq(Device::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }

    public IPage<DevicePojo> findListByNameBase(IPage<DevicePojo> page, String name, Long baseDeviceId);

    public List<DevicePojo> findListByStation(Long stationId);
}
