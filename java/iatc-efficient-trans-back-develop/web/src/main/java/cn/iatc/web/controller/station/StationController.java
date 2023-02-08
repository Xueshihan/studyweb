package cn.iatc.web.controller.station;

import cn.iatc.database.entity.Station;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.StationRelationService;
import cn.iatc.web.service.StationService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
class StationDeleteRequestBody {
    @Schema(description = "站点id列表", defaultValue = "[1]")
    List<Long> stationIds;
}

@Data
class StationMigrateRequestBody {
    //原来上级站点id
    @Schema(description = "老上级站点id", defaultValue = "1")
    private Long oldUpperId;

    //新上级站点id
    @Schema(description = "新上级站点id", defaultValue = "2")
    private Long newUpperId;

    //要迁移的站点id
    @Schema(description = "当前要变更的站点id", defaultValue = "3")
    private Long curStationId;
}

@Data
class StationCountRequestBody {

    @Schema(description = "站点id", defaultValue = "2")
    private Long stationId;

    @JSONField(name = "count")
    @Schema(name = "count", description = "站点数量", defaultValue = "200")
    Integer count = 0;
}



@Tag(name = "station", description = "站点相关")
@Slf4j
@RestController
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;
    @Autowired
    private StationRelationService stationRelationService;

    @Operation(summary = "删除某个站点",description = "登录后访问")
    @PostMapping("/delete")
    public RestResponse<String> deleteStation(@RequestHeader(value = "token") String token, @RequestBody StationDeleteRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkDeleteRequestBody(requestBody.getStationIds());
            this.handleDeleteStation(requestBody.getStationIds());
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkDeleteRequestBody(List<Long> stationIds) {
        if (stationIds == null || stationIds.size() == 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    private void handleDeleteStation(List<Long> stationIds) {
        Long count = stationRelationService.countByUpper(stationIds);
        if (count > 0) {
            throw new BaseException(Status.STATION_NEXT_LEVEL_EXIST);
        }
        stationService.deleteByIds(stationIds);
    }

    // 站房迁移
    @Operation(summary = "迁移站点",description = "登录后访问")
    @PostMapping("/migrate")
    public RestResponse<String> stationMigrate(@RequestHeader(value = "token") String token, @RequestBody StationMigrateRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkMigrateRequestBody(requestBody);
            this.handleMigrate(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkMigrateRequestBody(StationMigrateRequestBody requestBody) {
        Long oldUpperId = requestBody.getOldUpperId();
        Long newUpperId = requestBody.getNewUpperId();
        Long curStationId = requestBody.getCurStationId();
        if (oldUpperId == null || newUpperId == null || curStationId == null) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
        Station newStation = stationService.findById(newUpperId);
        if (newStation == null) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }

        Station curStation = stationService.findById(curStationId);
        if (curStation == null) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
        Integer newLevel = newStation.getLevel();
        Integer nextLevel = Station.LevelRelation.levelMap.get(newLevel);
        if (!nextLevel.equals(curStation.getLevel())) {
            throw new BaseException(Status.STATION_MIGRATE_LEVEL);
        }
    }

    private void handleMigrate(StationMigrateRequestBody requestBody) {
        Long oldUpperId = requestBody.getOldUpperId();
        Long newUpperId = requestBody.getNewUpperId();
        Long curStationId = requestBody.getCurStationId();
        stationRelationService.migrateStation(oldUpperId, newUpperId, curStationId);
    }

    @Operation(summary = "修改站点中站房个数",description = "登录后访问")
    @PostMapping("/count")
    public RestResponse<String> updateCount(@RequestHeader(value = "token") String token, @RequestBody StationCountRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCountRequestBody(requestBody);
            this.handleUpdateCount(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCountRequestBody(StationCountRequestBody requestBody) {
        Long stationId = requestBody.getStationId();
        this.checkStationId(stationId);
    }

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    private void handleUpdateCount(StationCountRequestBody requestBody) {
        Long stationId = requestBody.getStationId();
        Integer count = requestBody.getCount();
        stationService.updateCountByStation(stationId, count);
    }
 }
