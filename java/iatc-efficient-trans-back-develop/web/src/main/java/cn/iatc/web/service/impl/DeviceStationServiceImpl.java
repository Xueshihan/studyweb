package cn.iatc.web.service.impl;

import cn.iatc.database.entity.DeviceStation;
import cn.iatc.web.mapper.DeviceStationMapper;
import cn.iatc.web.service.DeviceStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DeviceStationServiceImpl extends ServiceImpl<DeviceStationMapper, DeviceStation> implements DeviceStationService {
}
