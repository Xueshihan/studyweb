package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.BaseDevice;
import cn.iatc.mqtt_server.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface BaseDeviceMapper extends BaseMapper<BaseDevice> {

    public BaseDevice findById(Long id);

    public default Long countAll() {
        LambdaQueryWrapper<BaseDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseDevice::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectCount(wrapper);
    }

    public default BaseDevice findByProperty(String property) {
        LambdaQueryWrapper<BaseDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseDevice::getProperty, property);
        wrapper.eq(BaseDevice::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
}
