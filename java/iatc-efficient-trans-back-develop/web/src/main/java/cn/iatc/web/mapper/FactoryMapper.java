package cn.iatc.web.mapper;

import cn.iatc.database.entity.Factory;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FactoryMapper extends BaseMapper<Factory> {

    public Factory findById(Long id);

    public default List<Factory> findAll() {
        LambdaQueryWrapper<Factory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Factory::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default Long countAll() {
        LambdaQueryWrapper<Factory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Factory::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectCount(wrapper);
    }
}
