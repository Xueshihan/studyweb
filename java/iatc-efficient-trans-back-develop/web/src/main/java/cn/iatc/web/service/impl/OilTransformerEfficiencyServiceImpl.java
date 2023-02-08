package cn.iatc.web.service.impl;

import cn.iatc.database.entity.OilTransformerEfficiency;
import cn.iatc.web.bean.transformer.OilTransformerEfficiencyPojo;
import cn.iatc.web.mapper.OilTransformerEfficiencyMapper;
import cn.iatc.web.service.OilTransformerEfficiencyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class OilTransformerEfficiencyServiceImpl extends ServiceImpl<OilTransformerEfficiencyMapper, OilTransformerEfficiency> implements OilTransformerEfficiencyService {

    @Autowired
    private OilTransformerEfficiencyMapper oilTransformerEfficiencyMapper;


    @Override
    public List<OilTransformerEfficiency> findByLevel(Integer level) {
        return oilTransformerEfficiencyMapper.findByLevel(level);
    }

    @Override
    public OilTransformerEfficiency findById(Long id) {
        return oilTransformerEfficiencyMapper.findById(id);
    }

    @Override
    public List<OilTransformerEfficiencyPojo> getKzsh(Long stationId) {
        return oilTransformerEfficiencyMapper.getKzsh(stationId);
    }
}
