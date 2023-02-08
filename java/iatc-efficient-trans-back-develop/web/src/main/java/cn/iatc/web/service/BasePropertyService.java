package cn.iatc.web.service;

import cn.iatc.database.entity.BaseProperty;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BasePropertyService extends IService<BaseProperty> {
    public void createBatch(JSONArray jsonArray);

    public Long countAll();
}
