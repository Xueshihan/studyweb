package cn.iatc.web.mapper;

import cn.iatc.database.entity.SystemConfiguration;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface SystemConfigurationMapper extends BaseMapper<SystemConfiguration> {
    public default void create(SystemConfiguration systemConfiguration) {
        this.insert(systemConfiguration);
    }

    public default SystemConfiguration findByType(String type) {
        LambdaQueryWrapper<SystemConfiguration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfiguration::getType, type);
        wrapper.eq(SystemConfiguration::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
}
