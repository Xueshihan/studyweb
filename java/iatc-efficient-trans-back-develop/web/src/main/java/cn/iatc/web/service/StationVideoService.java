package cn.iatc.web.service;

import cn.iatc.database.entity.StationVideo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StationVideoService extends IService<StationVideo> {

    public StationVideo createVideo(Long stationId, String name, String url);

    public void updateVideo(Long stationVideoId, String name, String url);

    public void deleteVideos(List<Long> ids);

    public IPage<StationVideo> findListByStation(Long stationId, Integer pageIndex, Integer num);
}
