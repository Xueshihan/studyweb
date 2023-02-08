package cn.iatc.web.service;

import cn.iatc.database.entity.StationType;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StationTypeService extends IService<StationType> {

    public void createBatch(JSONArray jsonArray);
}
