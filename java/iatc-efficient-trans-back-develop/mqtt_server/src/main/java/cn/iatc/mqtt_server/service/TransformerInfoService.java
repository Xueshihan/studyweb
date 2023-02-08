package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.mqtt_server.bean.transformerInfo.TransformerInfoPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TransformerInfoService extends IService<TransformerInfo> {

    public TransformerInfo findByStation(Long stationId);

    public List<Long> findStationIds();

    public List<TransformerInfoPojo> findAll();
}
