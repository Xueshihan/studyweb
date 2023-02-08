package cn.iatc.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.StationRelation;
import cn.iatc.web.mapper.StationRelationMapper;
import cn.iatc.web.service.StationRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StationRelationServiceImpl extends ServiceImpl<StationRelationMapper, StationRelation> implements StationRelationService {
    @Autowired
    private StationRelationMapper stationRelationMapper;

    @Override
    public void migrateStation(Long oldUpperId, Long newUpperId, Long curStationId) {

        // 上级是旧id只取第一个值，有且只有一个值
        List<StationRelation> curStationRelations = stationRelationMapper.findByCurUpperStation(curStationId, oldUpperId);
        if (curStationRelations.size() == 0) {
            return;
        }
        StationRelation curStationRelation = curStationRelations.get(0);
        List<StationRelation> oldStationRelations = stationRelationMapper.findByIdInSet(oldUpperId);

        for (StationRelation updateStationRelation: oldStationRelations) {
            // 更新新的
            String upperIdSet = updateStationRelation.getUpperIdSet();
            List<Long> upperIds = Arrays.stream(upperIdSet.split(",")).map(id -> {
                Long idL = Long.parseLong(id);
                if (idL.equals(oldUpperId)) {
                    return newUpperId;
                }
                return idL;
            }).collect(Collectors.toList());

            if (curStationRelation.getUpperId().equals(updateStationRelation.getUpperId())) {
                updateStationRelation.setUpperId(newUpperId);
            }
            updateStationRelation.setUpperIdSet(StrUtil.join(",", upperIds));

            Long curId = updateStationRelation.getCurStationId();
            Long curNewUpperId = updateStationRelation.getUpperId();

            StationRelation newStationRelation = stationRelationMapper.findByCurUpperMigUpperStation(curId, curNewUpperId, newUpperId);
            if (newStationRelation == null) {
                log.info("=== update null:{}", updateStationRelation);
                stationRelationMapper.updateById(updateStationRelation);
            } else {
                StationRelation oldUpdateStationRelation = stationRelationMapper.findByCurUpperMigUpperStation(curId, curNewUpperId, oldUpperId);
                if (oldUpdateStationRelation != null) {
                    log.info("=== delete:{}", updateStationRelation);
                        stationRelationMapper.deleteById(updateStationRelation);
                } else {
                    log.info("=== oldStationRelation is null delete curStationRelation");
                    stationRelationMapper.deleteById(curStationRelation);
                }
            }
        }
    }

    @Override
    public Long countByUpper(List<Long> upperIds) {
        return stationRelationMapper.countByUpper(upperIds);
    }
}
