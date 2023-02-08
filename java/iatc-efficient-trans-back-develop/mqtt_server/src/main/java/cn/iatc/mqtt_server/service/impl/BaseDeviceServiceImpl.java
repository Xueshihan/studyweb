package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.BaseDevice;
import cn.iatc.mqtt_server.constants.CommonConstants;
import cn.iatc.mqtt_server.mapper.BaseDeviceMapper;
import cn.iatc.mqtt_server.service.BaseDeviceService;
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
public class BaseDeviceServiceImpl extends ServiceImpl<BaseDeviceMapper, BaseDevice> implements BaseDeviceService {
    @Autowired
    private BaseDeviceMapper baseDeviceMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            String name = ((JSONObject)jsonObject).getString("name");
            String property = ((JSONObject)jsonObject).getString("property");
            Integer sort = ((JSONObject)jsonObject).getInteger("sort");
            BaseDevice baseDevice = baseDeviceMapper.findByProperty(property);

            Date createdTime = new Date();
            if (baseDevice == null) {
                createStatus = true;
                baseDevice = new BaseDevice();
                baseDevice.setCreatedTime(createdTime);
            }

            baseDevice.setName(name);
            baseDevice.setProperty(property);
            baseDevice.setSort(sort);
            baseDevice.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            baseDevice.setUpdatedTime(createdTime);
            if (createStatus) {
                baseDeviceMapper.insert(baseDevice);
            } else {
                baseDeviceMapper.updateById(baseDevice);
            }
        }
    }

    @Override
    public Long countAll() {
        return baseDeviceMapper.countAll();
    }
}
