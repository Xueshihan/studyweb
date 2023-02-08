package cn.iatc.web.mapper;

import cn.iatc.database.entity.StationVideo;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StationVideoMapper extends BaseMapper<StationVideo> {

    public default IPage<StationVideo> findListByStation( Long stationId, Integer pageIndex, Integer num) {
        LambdaQueryWrapper<StationVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StationVideo::getStationId, stationId);
        wrapper.eq(StationVideo::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectPage(new Page<>(pageIndex, num), wrapper);
    }
}
