package cn.iatc.web.service;

import cn.iatc.database.entity.BaseDevice;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseDeviceService extends IService<BaseDevice> {

    public void createBatch(JSONArray jsonArray);

    public Long countAll();
}
