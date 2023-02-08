package cn.iatc.web.service.impl;

import cn.iatc.database.entity.OriginalPower;
import cn.iatc.web.mapper.OriginalPowerMapper;
import cn.iatc.web.service.OriginalPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class OriginalPowerServiceImpl extends ServiceImpl<OriginalPowerMapper, OriginalPower> implements OriginalPowerService {

}
