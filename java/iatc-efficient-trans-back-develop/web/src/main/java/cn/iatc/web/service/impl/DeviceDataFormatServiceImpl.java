package cn.iatc.web.service.impl;

import cn.iatc.database.entity.DeviceDataFormat;
import cn.iatc.web.mapper.DeviceDataFormatMapper;
import cn.iatc.web.service.DeviceDataFormatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DeviceDataFormatServiceImpl extends ServiceImpl<DeviceDataFormatMapper, DeviceDataFormat> implements DeviceDataFormatService {
}
