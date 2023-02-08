package cn.iatc.mqtt_server.service;

import cn.iatc.database.entity.BaseProperty;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BasePropertyService extends IService<BaseProperty> {

    public BaseProperty findByProperty(String property);
}
