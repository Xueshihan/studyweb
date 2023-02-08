package cn.iatc.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Menu;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.MenuMapper;
import cn.iatc.web.service.MenuService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void createBatch(JSONArray jsonArray, Menu upperMenu, Boolean isInit) {
        if (isInit) {
            UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Menu::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
            this.update(updateWrapper);
        }

        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            Date createdTime = new Date();
            String name = ((JSONObject)jsonObject).getString("name");
            String title = ((JSONObject)jsonObject).getString("title");
            String icon = ((JSONObject)jsonObject).getString("icon");
            String path = ((JSONObject)jsonObject).getString("path");
            Integer type = ((JSONObject)jsonObject).getInteger("type");
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Menu::getType, type);
            Menu menu = menuMapper.selectOne(queryWrapper);
            if (menu == null) {
                createStatus = true;
                menu = new Menu();
                menu.setCreatedTime(createdTime);
            }
            menu.setName(name);
            menu.setTitle(title);
            menu.setType(type);
            menu.setIcon(icon);
            menu.setPath(path);
            menu.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            menu.setUpdatedTime(createdTime);

            if(upperMenu != null) {
                Long upperId = upperMenu.getId();
                menu.setUpperId(upperId);
                String upperIdSet = upperMenu.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                menu.setUpperIdSet(upperIdSet);
            } else {
                menu.setUpperId(null);
                menu.setUpperIdSet(null);
            }
            JSONArray children = ((JSONObject)jsonObject).getJSONArray("children");

            if (createStatus) {
                menuMapper.insert(menu);
            } else {
                menuMapper.updateById(menu);
            }

            if (children.size() > 0) {
                createBatch(children, menu, false);
            }
        }
    }
    @Override
    public List<Menu> findAll() {
        return menuMapper.findAll();
    }

    @Override
    public List<Menu> findListByRole(Long roleId) {
        return menuMapper.findListByRole(roleId);
    }
}
