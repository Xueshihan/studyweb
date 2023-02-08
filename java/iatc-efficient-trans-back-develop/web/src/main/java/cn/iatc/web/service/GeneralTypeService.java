package cn.iatc.web.service;

import cn.iatc.database.entity.GeneralType;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GeneralTypeService extends IService<GeneralType> {

    public void createBatch(JSONArray jsonArray);

    public Long countAll();

    public List<GeneralType> findListByBaseType(Long baseTypeId);
}
