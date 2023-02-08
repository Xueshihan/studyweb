package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.Station;
import cn.iatc.mqtt_server.mapper.StationMapper;
import cn.iatc.mqtt_server.service.StationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Autowired
    private StationMapper stationMapper;

    @Override
    public Station findById(Long id) {
        return stationMapper.selectById(id);
    }

    @Override
    public Long countStation() {
        return stationMapper.selectCount(null);
    }

    @Override
    public Long countByRegion(Long regionId) {
        return stationMapper.countByRegion(regionId);
    }

    @Override
    public List<Long> findIdsByRegion(Long regionId) {
        return stationMapper.findIdsByRegion(regionId);
    }

    @Override
    public Station findByCode(String code) {
        return stationMapper.findByCode(code);
    }

    @Override
    public List<Station> findByLevel(Integer level) {
        return stationMapper.findByLevel(level);
    }

}
