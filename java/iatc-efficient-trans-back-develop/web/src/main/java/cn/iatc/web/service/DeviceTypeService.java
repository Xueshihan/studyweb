package cn.iatc.web.service;

import cn.iatc.database.entity.DeviceType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeviceTypeService extends IService<DeviceType> {

    public List<DeviceType> findListByFactory(Long factoryId);
}
