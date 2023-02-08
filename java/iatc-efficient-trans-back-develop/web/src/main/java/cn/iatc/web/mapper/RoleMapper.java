package cn.iatc.web.mapper;

import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.iatc.database.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMapper extends BaseMapper<Role> {

    public void updateRole(Long roleId, String name, String remark);
    public Role findById(Long id);

    public default List<Role> findAll() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default List<Role> findListLikeByName(String name) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Role::getName, name);
        wrapper.eq(Role::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default List<Role> findByType(Integer type) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getType, type);
        wrapper.eq(Role::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default Long countRoleByType(Integer type) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getType, type);
        wrapper.eq(Role::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectCount(wrapper);
    }
}
