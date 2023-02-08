package cn.iatc.web.service.impl;

import cn.iatc.database.entity.BaseProperty;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.BasePropertyMapper;
import cn.iatc.web.service.BasePropertyService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
public class BasePropertyServiceImpl extends ServiceImpl<BasePropertyMapper, BaseProperty> implements BasePropertyService {
    @Autowired
    private BasePropertyMapper basePropertyMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            String name = ((JSONObject)jsonObject).getString("name");
            String property = ((JSONObject)jsonObject).getString("property");
            Integer sort = ((JSONObject)jsonObject).getInteger("sort");
            BaseProperty baseProperty = basePropertyMapper.findByProperty(property);

            Date createdTime = new Date();
            if (baseProperty == null) {
                createStatus = true;
                baseProperty = new BaseProperty();
                baseProperty.setCreatedTime(createdTime);
            }

            baseProperty.setName(name);
            baseProperty.setProperty(property);
            baseProperty.setSort(sort);
            baseProperty.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            baseProperty.setUpdatedTime(createdTime);
            if (createStatus) {
                basePropertyMapper.insert(baseProperty);
            } else {
                basePropertyMapper.updateById(baseProperty);
            }
        }
    }

    @Override
    public Long countAll() {
        return baseMapper.countAll();
    }
}
