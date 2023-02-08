package cn.iatc.web.controller.station;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.StationVideo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.StationVideoService;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
class BaseStationVideoBody {
    @Schema(description = "视频名字", required = true)
    private String name;

    @Schema(description = "视频地址", required = true)
    private String url;
}
@Data
class CreateStationVideoRequestBody extends BaseStationVideoBody {
    @Schema(description = "站点id", required = true)
    private Long stationId;
}

@Data
class UpdateStationVideoRequestBody extends BaseStationVideoBody {
    @Schema(description = "站点视频id", required = true)
    private Long stationVideoId;
}

@Data
class DeleteStationVideoRequestBody {
    @Schema(description = "站点视频id", defaultValue = "[1]", required = true)
    private List<Long> stationVideoIds;
}

@Data
class StationVideosRequestBody {
    @Schema(description = "站点id", required = true)
    private Long stationId;

    @Schema(description = "页数", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "数量", defaultValue = "10")
    private Integer num = 10;
}

@Data
class VideosResponseData {
    List<StationVideo> stationVideoList;

    Long pages;

    Long curPage;

    Long total;

    Long size;
}

@Tag(name = "video", description = "站点视频相关")
@Slf4j
@RestController
@RequestMapping("/station")
public class StationVideoController {

    @Autowired
    private StationVideoService stationVideoService;

    @Operation(summary = "创建站点视频",description = "登录后访问")
    @PostMapping("/video")
    public RestResponse<String> createStationVideo(@RequestHeader(value = "token") String token, @RequestBody CreateStationVideoRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCreateRequestBody(requestBody);
            this.handleCreateVideo(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateStationVideoRequestBody requestBody) {
        this.checkStationId(requestBody.getStationId());
        this.checkName(requestBody.getName());
        this.checkUrl(requestBody.getUrl());
    }

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    private void checkName(String name) {
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.STATION_VIDEO_NAME_INVALID);
        }
    }

    private void checkUrl(String url) {
        if (StrUtil.isBlank(url)) {
            throw new BaseException(Status.STATION_VIDEO_URL_INVALID);
        }
    }

    private void handleCreateVideo(CreateStationVideoRequestBody requestBody) {
        stationVideoService.createVideo(requestBody.getStationId(), requestBody.getName(), requestBody.getUrl());
    }

    @Operation(summary = "更新站点视频",description = "登录后访问")
    @PutMapping("/video")
    public RestResponse<String> updateStationVideo(@RequestHeader(value = "token") String token, @RequestBody UpdateStationVideoRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            this.handleUpdateVideo(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkUpdateRequestBody(UpdateStationVideoRequestBody requestBody) {
        this.checkStationVideo(requestBody.getStationVideoId());
        this.checkName(requestBody.getName());
        this.checkUrl(requestBody.getUrl());
    }

    private void checkStationVideo(Long stationVideoId) {
        if (stationVideoId == null || stationVideoId <= 0) {
            throw new BaseException(Status.STATION_VIDEO_ID_INVALID);
        }
    }

    private void handleUpdateVideo(UpdateStationVideoRequestBody requestBody) {
        stationVideoService.updateVideo(requestBody.getStationVideoId(), requestBody.getName(), requestBody.getUrl());
    }

    @Operation(summary = "删除站点视频",description = "登录后访问")
    @PostMapping("/video/delete")
    public RestResponse<String> deleteStationVideo(@RequestHeader(value = "token") String token, @RequestBody DeleteStationVideoRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkDeleteRequestBody(requestBody);
            this.handleDeleteVideos(requestBody.getStationVideoIds());
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkDeleteRequestBody(DeleteStationVideoRequestBody requestBody) {
        List<Long> ids = requestBody.getStationVideoIds();
        if (ids == null || ids.size() == 0) {
            throw new BaseException(Status.STATION_VIDEO_ID_INVALID);
        }
    }

    private void handleDeleteVideos(List<Long> ids) {
        stationVideoService.deleteVideos(ids);
    }

    @Operation(summary = "获取站点视频列表",description = "登录后访问")
    @PostMapping("/video/list")
    public RestResponse<VideosResponseData> getVideos(@RequestHeader(value = "token") String token, @RequestBody StationVideosRequestBody requestBody) {
        RestResponse<VideosResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            this.handleVideos(requestBody, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleVideos(StationVideosRequestBody requestBody, RestResponse<VideosResponseData> response) {
        Long stationId = requestBody.getStationId();
        Integer page = requestBody.getPage();
        Integer num = requestBody.getNum();
        VideosResponseData data = new VideosResponseData();
        Page<StationVideo> stationVideoPage = (Page<StationVideo>) stationVideoService.findListByStation(stationId ,page, num);
        data.setStationVideoList(stationVideoPage.getRecords());
        data.setPages(stationVideoPage.getPages());
        data.setCurPage(stationVideoPage.getCurrent());
        data.setTotal(stationVideoPage.getTotal());
        data.setSize(stationVideoPage.getSize());
        response.setSuccess(data);
    }

}
