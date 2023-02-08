package cn.iatc.web.service.impl;

import cn.iatc.database.entity.RoleMenu;
import cn.iatc.web.mapper.RoleMenuMapper;
import cn.iatc.web.service.RoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);

        List<RoleMenu> roleMenuList = new ArrayList<>();
        for(Long menuId: menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        if (roleMenuList.size() > 0) {
            this.saveBatch(roleMenuList);
        }
    }

    @Override
    public List<RoleMenu> findListByRole(Long roleId) {
        return roleMenuMapper.findListByRole(roleId);
    }
}
