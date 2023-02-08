package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.Station;
import cn.iatc.mqtt_server.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public interface StationMapper extends BaseMapper<Station> {

    public default void updateCountByStation(Long stationId, Integer count) {
        LambdaUpdateWrapper<Station> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Station::getStationCount, count);
        wrapper.set(Station::getId, stationId);
        this.update(null, wrapper);
    }
    public default void deleteById(Long id) {
        LambdaUpdateWrapper<Station> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Station::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(Station::getUpdatedTime, new Date());
        wrapper.eq(Station::getId, id);
        this.update(null, wrapper);
    }
    public default Station findByCode(String code) {
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Station::getCode, code);
        wrapper.eq(Station::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }

    public default List<Long> findIdsByRegion(Long regionId) {
        LambdaQueryWrapper<Station> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Station::getId);
        queryWrapper.eq(Station::getRegionId, regionId);
        queryWrapper.eq(Station::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectObjs(queryWrapper).stream().map(item -> (Long) item).collect(Collectors.toList());
    }

    public default List<Station> findByLevel(Integer level) {
        LambdaQueryWrapper<Station> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Station::getLevel, level);
        queryWrapper.eq(Station::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(queryWrapper);
    }

    public Station findById(Long id);

    public default Long countByRegion(Long regionId) {
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Station::getRegionId, regionId);
        wrapper.eq(Station::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectCount(wrapper);
    }
}
