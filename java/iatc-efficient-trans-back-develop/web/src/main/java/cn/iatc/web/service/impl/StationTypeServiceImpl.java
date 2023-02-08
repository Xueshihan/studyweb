package cn.iatc.web.service.impl;

import cn.iatc.database.entity.StationType;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.StationTypeMapper;
import cn.iatc.web.service.StationTypeService;
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

@Slf4j
@Service
@Transactional
public class StationTypeServiceImpl extends ServiceImpl<StationTypeMapper, StationType> implements StationTypeService {
    @Autowired
    private StationTypeMapper stationTypeMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        UpdateWrapper<StationType> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(StationType::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        this.update(updateWrapper);

        for (Object jsonObject : jsonArray) {
            boolean createStatus = false;
            Date createdTime = new Date();
            String name = ((JSONObject) jsonObject).getString("name");
            Integer type = ((JSONObject) jsonObject).getInteger("type");

            QueryWrapper<StationType> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(StationType::getType, type);
            StationType stationType = stationTypeMapper.selectOne(wrapper);
            if (stationType == null) {
                createStatus = true;
                stationType = new StationType();
                stationType.setType(type);
                stationType.setCreatedTime(createdTime);
            }

            stationType.setName(name);
            stationType.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            stationType.setUpdatedTime(createdTime);

            if (createStatus) {
                stationTypeMapper.insert(stationType);
            } else {
                stationTypeMapper.updateById(stationType);
            }
        }
    }
}
