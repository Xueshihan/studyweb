package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.BaseProperty;
import cn.iatc.mqtt_server.constants.CommonConstants;
import cn.iatc.mqtt_server.mapper.BasePropertyMapper;
import cn.iatc.mqtt_server.service.BasePropertyService;
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
    public BaseProperty findByProperty(String property) {
        return basePropertyMapper.findByProperty(property);
    }
}
