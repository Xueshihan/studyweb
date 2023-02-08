package cn.iatc.web.service.impl;

import cn.iatc.database.entity.StationVideo;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.StationVideoMapper;
import cn.iatc.web.service.StationVideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class StationVideoServiceImpl extends ServiceImpl<StationVideoMapper, StationVideo> implements StationVideoService {

    @Autowired
    private StationVideoMapper stationVideoMapper;

    @Override
    public StationVideo createVideo(Long stationId, String name, String url) {
        Date createdTime = new Date();
        StationVideo stationVideo = new StationVideo();
        stationVideo.setStationId(stationId);
        stationVideo.setName(name);
        stationVideo.setUrl(url);
        stationVideo.setType(StationVideo.Type.EZVIZ.getValue());
        stationVideo.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        stationVideo.setCreatedTime(createdTime);
        stationVideo.setUpdatedTime(createdTime);
        stationVideoMapper.insert(stationVideo);
        return stationVideo;
    }

    @Override
    public void updateVideo(Long stationVideoId, String name, String url) {
        LambdaUpdateWrapper<StationVideo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StationVideo::getName, name);
        wrapper.set(StationVideo::getUrl, url);
        wrapper.set(StationVideo::getUpdatedTime, new Date());
        wrapper.eq(StationVideo::getId, stationVideoId);
        stationVideoMapper.update(null, wrapper);
    }

    @Override
    public void deleteVideos(List<Long> ids) {
        LambdaUpdateWrapper<StationVideo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StationVideo::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(StationVideo::getUpdatedTime, new Date());
        wrapper.in(StationVideo::getId, ids);
        stationVideoMapper.update(null, wrapper);
    }

    @Override
    public IPage<StationVideo> findListByStation(Long stationId, Integer pageIndex, Integer num) {
        return stationVideoMapper.findListByStation(stationId, pageIndex, num);
    }
}
