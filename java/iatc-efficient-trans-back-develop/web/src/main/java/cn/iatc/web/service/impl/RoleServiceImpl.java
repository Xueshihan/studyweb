package cn.iatc.web.service.impl;

import cn.iatc.database.entity.Menu;
import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.RoleMenu;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.MenuMapper;
import cn.iatc.web.mapper.RoleMapper;
import cn.iatc.web.mapper.RoleMenuMapper;
import cn.iatc.web.service.RoleService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            String name = ((JSONObject)jsonObject).getString("name");
            Integer type = ((JSONObject)jsonObject).getInteger("type");
            String remark = ((JSONObject)jsonObject).getString("remark");
            Date createdTime = new Date();
            Role role = new Role();
            role.setName(name);
            role.setType(type);
            role.setRemark(remark);
            role.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            role.setCreatedTime(createdTime);
            role.setUpdatedTime(createdTime);
            roleMapper.insert(role);

            QueryWrapper<Menu> menuWrapper = new QueryWrapper<>();
            menuWrapper.lambda().eq(Menu::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
            List<Menu> menuList = menuMapper.selectList(menuWrapper);

            for (Menu menu: menuList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menu.getId());
                roleMenuMapper.insert(roleMenu);
            }
        }
    }

    @Override
    public Role createRole(String name, String remark, List<Long> menuIds) {
        Date createdTime = new Date();
        Role role = new Role();
        role.setName(name);
        role.setType(Role.Type.COMMON.getValue());
        role.setRemark(remark);
        role.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        role.setCreatedTime(createdTime);
        role.setUpdatedTime(createdTime);
        roleMapper.insert(role);

        List<RoleMenu> roleMenuList = new ArrayList<>();
        for(Long menuId: menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        if (roleMenuList.size() > 0) {
            roleMenuMapper.createBatch(roleMenuList);
        }
        return role;
    }

    @Override
    public void updateRole(Long roleId, String name, String remark, List<Long> menuIds) {
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        roleMapper.updateRole(roleId, name, remark);
        List<RoleMenu> roleMenuList = new ArrayList<>();
        for(Long menuId: menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        if (roleMenuList.size() > 0) {
            roleMenuMapper.createBatch(roleMenuList);
        }
    }


    @Override
    public void deleteRole(Long roleId) {
        // 删除关联表信息
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        // 更新role表enabled_status
        LambdaUpdateWrapper<Role> wrapperRole = new LambdaUpdateWrapper<>();
        wrapperRole.set(Role::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapperRole.eq(Role::getId, roleId);
        roleMapper.update(null, wrapperRole);
        this.update(wrapperRole);
    }

    @Override
    public Long countRoleByType(Integer type) {
        return roleMapper.countRoleByType(type);
    }

    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public List<Role> findListLikeByName(String name) {
        return roleMapper.findListLikeByName(name);
    }

    @Override
    public List<Role> findByType(Integer type) {
        return roleMapper.findByType(type);
    }
}
