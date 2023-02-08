package cn.iatc.mqtt_server.mapper;

import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.mqtt_server.bean.transformerInfo.TransformerInfoPojo;
import cn.iatc.mqtt_server.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TransformerInfoMapper extends BaseMapper<TransformerInfo> {

    public default TransformerInfo findByStation(Long stationId) {
        LambdaQueryWrapper<TransformerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransformerInfo::getStationId, stationId);
        wrapper.eq(TransformerInfo::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
    public List<Long> findStationIds();

    public List<TransformerInfoPojo> findAll();
}
