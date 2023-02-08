package cn.iatc.web.service.impl;

import cn.iatc.database.entity.SystemConfiguration;
import cn.iatc.web.mapper.SystemConfigurationMapper;
import cn.iatc.web.service.SystemConfigurationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SystemConfigurationServiceImpl extends ServiceImpl<SystemConfigurationMapper, SystemConfiguration> implements SystemConfigurationService {

    @Autowired
    private SystemConfigurationMapper systemConfigurationMapper;
    @Override
    public void create(SystemConfiguration systemConfiguration) {
        systemConfigurationMapper.create(systemConfiguration);
    }

    @Override
    public SystemConfiguration findByType(String type) {
        return systemConfigurationMapper.findByType(type);
    }
}
