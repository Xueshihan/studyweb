package cn.iatc.web.service;

import cn.iatc.database.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {

    public void updateRoleMenus(Long roleId, List<Long> menuIds);
    public List<RoleMenu> findListByRole(Long roleId);
}
