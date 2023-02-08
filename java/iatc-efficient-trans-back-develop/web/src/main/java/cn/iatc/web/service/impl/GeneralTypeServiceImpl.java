package cn.iatc.web.service.impl;

import cn.iatc.database.entity.BaseElec;
import cn.iatc.database.entity.GeneralType;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.BaseElecMapper;
import cn.iatc.web.mapper.GeneralTypeMapper;
import cn.iatc.web.service.GeneralTypeService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class GeneralTypeServiceImpl extends ServiceImpl<GeneralTypeMapper, GeneralType> implements GeneralTypeService {

    @Autowired
    private BaseElecMapper baseElecMapper;

    @Autowired
    private GeneralTypeMapper generalTypeMapper;

    @Override
    public void createBatch(JSONArray jsonArray) {
        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            String name = ((JSONObject)jsonObject).getString("name");
            String code = ((JSONObject)jsonObject).getString("code");
            String baseTypeStr = ((JSONObject)jsonObject).getString("base_type");
            Integer sort = ((JSONObject)jsonObject).getInteger("sort");
            BaseElec baseElec = baseElecMapper.findByType(baseTypeStr);
            if (baseElec == null) {
                continue;
            }

            GeneralType generalType = generalTypeMapper.findByCode(code);

            Date createdTime = new Date();
            if (generalType == null) {
                createStatus = true;
                generalType = new GeneralType();
                generalType.setCreatedTime(createdTime);
            }

            generalType.setName(name);
            generalType.setCode(code);
            generalType.setBaseElecId(baseElec.getId());
            generalType.setSort(sort);
            generalType.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            generalType.setUpdatedTime(createdTime);
            if (createStatus) {
                generalTypeMapper.insert(generalType);
            } else {
                generalTypeMapper.updateById(generalType);
            }
        }
    }

    @Override
    public Long countAll() {
        LambdaQueryWrapper<GeneralType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GeneralType::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return generalTypeMapper.selectCount(wrapper);
    }

    @Override
    public List<GeneralType> findListByBaseType(Long baseTypeId) {
        return generalTypeMapper.findListByBaseType(baseTypeId);
    }
}
