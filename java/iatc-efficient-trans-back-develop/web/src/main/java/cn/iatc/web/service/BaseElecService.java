package cn.iatc.web.service;

import cn.iatc.database.entity.BaseElec;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseElecService extends IService<BaseElec> {

    public void createBatch(JSONArray jsonArray);

    public Long countAll();

    public BaseElec findByType(String type);
}
