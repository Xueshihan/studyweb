package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.StationRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StationRelationService extends IService<StationRelation> {

    // 迁移站房
    public void migrateStation(Long oldUpperId, Long newUpperId, Long curStationId);

    public Long countByUpper(List<Long> upperIds);
}
