package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.mqtt_server.bean.transformerInfo.TransformerInfoPojo;
import cn.iatc.mqtt_server.mapper.TransformerInfoMapper;
import cn.iatc.mqtt_server.service.TransformerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TransformerInfoServiceImpl extends ServiceImpl<TransformerInfoMapper, TransformerInfo> implements TransformerInfoService {

    @Autowired
    private TransformerInfoMapper transformerInfoMapper;

    @Override
    public TransformerInfo findByStation(Long stationId) {
        return transformerInfoMapper.findByStation(stationId);
    }

    @Override
    public List<Long> findStationIds() {
        return transformerInfoMapper.findStationIds();
    }

    @Override
    public List<TransformerInfoPojo> findAll() {
        return transformerInfoMapper.findAll();
    }
}
