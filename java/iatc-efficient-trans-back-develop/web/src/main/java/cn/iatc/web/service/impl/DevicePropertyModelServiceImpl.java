package cn.iatc.web.service.impl;

import cn.iatc.database.entity.DevicePropertyModel;
import cn.iatc.web.mapper.DevicePropertyModelMapper;
import cn.iatc.web.service.DevicePropertyModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DevicePropertyModelServiceImpl extends ServiceImpl<DevicePropertyModelMapper, DevicePropertyModel> implements DevicePropertyModelService {
}
