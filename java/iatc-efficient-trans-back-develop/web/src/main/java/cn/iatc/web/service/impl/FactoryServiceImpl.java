package cn.iatc.web.service.impl;

import cn.iatc.database.entity.Factory;
import cn.iatc.database.entity.Menu;
import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.RoleMenu;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.FactoryMapper;
import cn.iatc.web.service.FactoryService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class FactoryServiceImpl extends ServiceImpl<FactoryMapper, Factory> implements FactoryService {

    @Autowired
    private FactoryMapper factoryMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            String name = ((JSONObject)jsonObject).getString("name");
            Integer type = ((JSONObject)jsonObject).getInteger("type");
            Date createdTime = new Date();
            Factory factory = new Factory();
            factory.setName(name);
            factory.setType(type);
            factory.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            factory.setCreatedTime(createdTime);
            factory.setUpdatedTime(createdTime);
            factoryMapper.insert(factory);
        }
    }

    @Override
    public Long countAll() {
        return factoryMapper.countAll();
    }


    @Override
    public List<Factory> findAll() {
        return factoryMapper.findAll();
    }
}
