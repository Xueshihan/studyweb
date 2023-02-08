package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.Station;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StationService extends IService<Station> {

    public Station findById(Long id);

    public Long countStation();

    public Long countByRegion(Long regionId);

    public List<Long> findIdsByRegion(Long regionId);

    public Station findByCode(String code);

    public List<Station> findByLevel(Integer level);

}
