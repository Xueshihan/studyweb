package cn.iatc.web.service;

import cn.iatc.database.entity.OilTransformerEfficiency;
import cn.iatc.web.bean.transformer.OilTransformerEfficiencyPojo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface OilTransformerEfficiencyService extends IService<OilTransformerEfficiency> {


    public List<OilTransformerEfficiency> findByLevel(Integer level);

    public OilTransformerEfficiency findById(Long id);

    List<OilTransformerEfficiencyPojo> getKzsh(Long stationId);
}
