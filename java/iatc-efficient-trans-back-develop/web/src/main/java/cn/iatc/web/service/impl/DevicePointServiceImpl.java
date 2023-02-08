package cn.iatc.web.service.impl;

import cn.iatc.database.entity.DevicePoint;
import cn.iatc.web.mapper.DevicePointMapper;
import cn.iatc.web.service.DevicePointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DevicePointServiceImpl extends ServiceImpl<DevicePointMapper, DevicePoint> implements DevicePointService {
}
