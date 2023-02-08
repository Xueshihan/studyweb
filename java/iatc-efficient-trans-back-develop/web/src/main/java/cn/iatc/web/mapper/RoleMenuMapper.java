package cn.iatc.web.mapper;

import cn.iatc.database.entity.RoleMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    public void createBatch(List<RoleMenu> roleMenuList);

    public default List<RoleMenu> findListByRole(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        return this.selectList(wrapper);
    }
}
