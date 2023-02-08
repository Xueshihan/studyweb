package cn.iatc.web.mapper;

import cn.iatc.database.entity.GeneralType;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GeneralTypeMapper extends BaseMapper<GeneralType> {

    public default GeneralType findByCode(String code) {
        LambdaQueryWrapper<GeneralType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GeneralType::getCode, code);
        wrapper.eq(GeneralType::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }

    public GeneralType findById(Long id);

    public default List<GeneralType> findListByBaseType(Long baseTypeId) {
        LambdaQueryWrapper<GeneralType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GeneralType::getBaseElecId, baseTypeId);
        wrapper.eq(GeneralType::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        wrapper.orderByAsc(GeneralType::getSort);
        return this.selectList(wrapper);
    }
}
