package cn.iatc.web.mapper;

import cn.iatc.database.entity.DeviceStation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeviceStationMapper extends BaseMapper<DeviceStation> {

    public default void deleteByDeviceIds(List<Long> deviceIds) {
        LambdaQueryWrapper<DeviceStation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DeviceStation::getDeviceId, deviceIds);
        this.delete(wrapper);
    }
}
