package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.BaseProperty;
import cn.iatc.mqtt_server.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface BasePropertyMapper extends BaseMapper<BaseProperty> {
    public BaseProperty findById(Long id);

    public default BaseProperty findByProperty(String property) {
        LambdaQueryWrapper<BaseProperty> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseProperty::getProperty, property);
        wrapper.eq(BaseProperty::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
}
