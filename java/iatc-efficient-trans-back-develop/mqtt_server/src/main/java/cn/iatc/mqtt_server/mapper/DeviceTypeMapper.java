package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.DeviceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    public DeviceType findById(Long id);

    public List<DeviceType> findListByFactory(Long factoryId);
}
