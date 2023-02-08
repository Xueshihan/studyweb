package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.StationRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StationRelationMapper extends BaseMapper<StationRelation> {

    public default void deleteByCurStation(Long stationId) {
        LambdaQueryWrapper<StationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StationRelation::getCurStationId, stationId);
        this.delete(wrapper);
    }
    // 多个父节点会有一个子节点
    public default List<StationRelation> findByCurStation(Long curStationId) {
        LambdaQueryWrapper<StationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StationRelation::getCurStationId, curStationId);
        return this.selectList(wrapper);
    }

    public default List<StationRelation> findByCurUpperStation(Long curStationId, Long upperId) {
        LambdaQueryWrapper<StationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StationRelation::getCurStationId, curStationId);
        wrapper.eq(StationRelation::getUpperId, upperId);
        return this.selectList(wrapper);
    }

    // 当前id 上级id，迁移的上级id
    public StationRelation findByCurUpperMigUpperStation(Long curStationId, Long upperId, Long migUpperId);

    public List<StationRelation> findByIdInSet(Long stationId);

    public default Long countByUpper(List<Long> upperIds) {
        LambdaQueryWrapper<StationRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(StationRelation::getUpperId, upperIds);
        return this.selectCount(wrapper);
    }
}
