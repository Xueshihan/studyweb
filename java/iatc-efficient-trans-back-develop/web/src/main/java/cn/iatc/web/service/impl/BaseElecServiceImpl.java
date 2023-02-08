package cn.iatc.web.service.impl;

import cn.iatc.database.entity.BaseElec;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.BaseElecMapper;
import cn.iatc.web.service.BaseElecService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
public class BaseElecServiceImpl extends ServiceImpl<BaseElecMapper, BaseElec> implements BaseElecService {

    @Autowired
    private BaseElecMapper baseElecMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            String name = ((JSONObject)jsonObject).getString("name");
            String type = ((JSONObject)jsonObject).getString("type");
            Integer sort = ((JSONObject)jsonObject).getInteger("sort");
            BaseElec baseElec = baseElecMapper.findByType(type);

            Date createdTime = new Date();
            if (baseElec == null) {
                createStatus = true;
                baseElec = new BaseElec();
                baseElec.setCreatedTime(createdTime);
            }

            baseElec.setName(name);
            baseElec.setType(type);
            baseElec.setSort(sort);
            baseElec.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            baseElec.setUpdatedTime(createdTime);
            if (createStatus) {
                baseElecMapper.insert(baseElec);
            } else {
                baseElecMapper.updateById(baseElec);
            }
        }
    }

    @Override
    public Long countAll() {
        LambdaQueryWrapper<BaseElec> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseElec::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return baseElecMapper.selectCount(wrapper);
    }

    @Override
    public BaseElec findByType(String type) {
        return baseElecMapper.findByType(type);
    }
}
