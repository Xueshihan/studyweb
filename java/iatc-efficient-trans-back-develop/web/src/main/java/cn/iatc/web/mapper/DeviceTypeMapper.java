package cn.iatc.web.mapper;

import cn.iatc.database.entity.DeviceType;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    public DeviceType findById(Long id);

    public List<DeviceType> findListByFactory(Long factoryId);
}
