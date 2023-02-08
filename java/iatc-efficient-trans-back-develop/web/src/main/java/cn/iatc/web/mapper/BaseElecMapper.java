package cn.iatc.web.mapper;

import cn.iatc.database.entity.BaseElec;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface BaseElecMapper extends BaseMapper<BaseElec> {

    public BaseElec findById(Long id);

    public default BaseElec findByType(String type) {
        LambdaQueryWrapper<BaseElec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseElec::getType, type);
        wrapper.eq(BaseElec::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }
}
