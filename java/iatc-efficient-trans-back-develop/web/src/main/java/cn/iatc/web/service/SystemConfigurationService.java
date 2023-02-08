package cn.iatc.web.service;

import cn.iatc.database.entity.SystemConfiguration;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SystemConfigurationService extends IService<SystemConfiguration> {

    public void create(SystemConfiguration systemConfiguration);

    public SystemConfiguration findByType(String type);
}
