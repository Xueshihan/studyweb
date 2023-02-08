package cn.iatc.web.mapper;

import cn.iatc.database.entity.StationType;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface StationTypeMapper extends BaseMapper<StationType> {

    public StationType findById(Long id);

    public default StationType findByType(Integer type) {
        LambdaQueryWrapper<StationType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StationType::getType, type);
        wrapper.eq(StationType::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
}
