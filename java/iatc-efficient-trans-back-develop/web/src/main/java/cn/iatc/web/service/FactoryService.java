package cn.iatc.web.service;

import cn.iatc.database.entity.Factory;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FactoryService extends IService<Factory> {

    public void createBatch(JSONArray jsonArray);

    public Long countAll();
    public List<Factory> findAll();
}
