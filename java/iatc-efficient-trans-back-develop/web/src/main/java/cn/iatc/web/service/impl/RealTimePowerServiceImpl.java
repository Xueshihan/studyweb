package cn.iatc.web.service.impl;

import cn.iatc.database.entity.RealTimePower;
import cn.iatc.web.mapper.RealTimePowerMapper;
import cn.iatc.web.service.RealTimePowerService;
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
public class RealTimePowerServiceImpl extends ServiceImpl<RealTimePowerMapper, RealTimePower> implements RealTimePowerService {

    @Autowired
    private RealTimePowerMapper realTimePowerMapper;

    @Override
    public List<RealTimePower> getRealTimePowerOfPeriod(Long stationId, Date startDate, Date endDate) {
        return realTimePowerMapper.getRealTimePowerOfPeriod(stationId, startDate, endDate);
    }
}
