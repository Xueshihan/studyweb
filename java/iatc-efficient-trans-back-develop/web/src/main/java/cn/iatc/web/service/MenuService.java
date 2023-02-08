package cn.iatc.web.service;

import cn.iatc.database.entity.Menu;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

    public void createBatch(JSONArray jsonArray, Menu upperMenu, Boolean isInit);

    public List<Menu> findAll();

    public List<Menu> findListByRole(Long roleId);
}
