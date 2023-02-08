package cn.iatc.web.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.iatc.database.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {


    public void createBatch(JSONArray jsonArray);

    public Role createRole(String name, String remark, List<Long> menus);

    public void updateRole(Long roleId, String name, String remark, List<Long> menuIds);
    public void deleteRole(Long roleId);

    public Long countRoleByType(Integer type);

    public Role findById(Long id);

    public List<Role> findAll();

    public List<Role> findListLikeByName(String name);

    public List<Role> findByType(Integer type);
}
