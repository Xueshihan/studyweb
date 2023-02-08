package cn.iatc.web.mapper;

import cn.iatc.database.entity.Menu;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface MenuMapper extends BaseMapper<Menu> {

    public default List<Menu> findAll() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        wrapper.orderByAsc(Menu::getType);
        return this.selectList(wrapper);
    }

    public List<Menu> findListByRole(Long roleId);
}
