package cn.iatc.mqtt_server.service.impl;

import cn.iatc.database.entity.DevicePropertyModel;
import cn.iatc.mqtt_server.mapper.DevicePropertyModelMapper;
import cn.iatc.mqtt_server.service.DevicePropertyModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DevicePropertyModelServiceImpl extends ServiceImpl<DevicePropertyModelMapper, DevicePropertyModel> implements DevicePropertyModelService {
}
