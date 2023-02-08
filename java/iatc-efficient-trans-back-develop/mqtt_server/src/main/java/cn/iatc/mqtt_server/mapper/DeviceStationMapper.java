package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.DeviceStation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceStationMapper extends BaseMapper<DeviceStation> {

    public List<Long> findDevicesByStation(Long stationId);
}
