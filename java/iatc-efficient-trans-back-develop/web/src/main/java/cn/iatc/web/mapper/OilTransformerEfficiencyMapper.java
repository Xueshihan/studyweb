package cn.iatc.web.mapper;

import cn.iatc.database.entity.OilTransformerEfficiency;
import cn.iatc.web.bean.transformer.OilTransformerEfficiencyPojo;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public interface OilTransformerEfficiencyMapper extends BaseMapper<OilTransformerEfficiency> {

    public default void deleteById(Long id) {
        LambdaUpdateWrapper<OilTransformerEfficiency> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OilTransformerEfficiency::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(OilTransformerEfficiency::getUpdatedTime, new Date());
        wrapper.eq(OilTransformerEfficiency::getId, id);
        this.update(null, wrapper);
    }

    public default List<OilTransformerEfficiency> findByLevel(Integer level) {
        LambdaQueryWrapper<OilTransformerEfficiency> queryWrapper = new LambdaQueryWrapper<>();
        if(level>0) {
            queryWrapper.eq(OilTransformerEfficiency::getLevel, level);
        }
        queryWrapper.eq(OilTransformerEfficiency::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(queryWrapper);
    }

    public OilTransformerEfficiency findById(Long id);

    List<OilTransformerEfficiencyPojo> getKzsh(Long stationId);

}
