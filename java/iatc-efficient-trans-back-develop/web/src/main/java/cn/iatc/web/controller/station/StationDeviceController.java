package cn.iatc.web.controller.station;


import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Station;
import cn.iatc.web.bean.station.StationPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.StationService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Data
class CreateStationDeviceRequestBody {
    @Schema(description = "父id", defaultValue = "1")
    private Long upperId;
    @Schema(description = "站点设备名字", defaultValue = "名字")
    private String name;

    @Schema(description = "排序", defaultValue = "1")
    private Integer sort;

    @Schema(description = "容量", defaultValue = "123")
    private String capacity;
    @Schema(description = "pmsid", defaultValue = "123")
    private String pmsId;

    @Schema(description = "智能化时间", defaultValue = "2023-01-10 08:00:00")
    private Date smartTime;

    @Schema(description = "通用类型id", defaultValue = "1")
    private Long generalTypeId;

    public StationPojo fromStationPojo() {
        StationPojo stationPojo = new StationPojo();
        stationPojo.setUpperId(this.upperId);
        stationPojo.setName(this.name);
        stationPojo.setSort(this.sort);
        stationPojo.setCapacity(this.capacity);
        stationPojo.setPmsId(this.pmsId);
        stationPojo.setSmartTime(smartTime);
        stationPojo.setGeneralTypeId(this.generalTypeId);
        return stationPojo;
    }
}

@Data
class UpdateStationDeviceRequestBody extends CreateStationDeviceRequestBody {
    @Schema(description = "站点id", defaultValue = "1")
    private Long stationId;
}


// 站房以下添加和修改 level=6-高压进线柜，7-高压出线柜，8-变压器，40-低压进线柜，41-低压出线柜，42-低压开关，43-开关三相(A,B,C)
@Tag(name = "station", description = "站点相关")
@Slf4j
@RestController
@RequestMapping("/station")
public class StationDeviceController {

    @Autowired
    private StationService stationService;

    // name, sort, capacity, smart_time, pms_id, general_type_id
    @Operation(summary = "添加站点设备信息",description = "登录后访问")
    @PostMapping("/device")
    public RestResponse<String> addStationDevice(@RequestHeader(value = "token") String token, @RequestBody CreateStationDeviceRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCreateRequestBody(requestBody);
            this.createStationDevice(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateStationDeviceRequestBody requestBody) {
        String name = requestBody.getName();
        Integer sort = requestBody.getSort();
        Long upperId = requestBody.getUpperId();
        String capacity = requestBody.getCapacity();

        this.checkUpperId(upperId);
        this.checkName(name);
        this.checkSort(sort);

        Station upperStation = stationService.findById(upperId);
        Integer upperLevel = upperStation.getLevel();
        Integer nextLevel = Station.LevelRelation.levelMap.get(upperLevel);
        if (!Station.LevelConstant.stationDeviceLevels.contains(nextLevel)) {
            throw new BaseException(Status.STATION_UPPER_ID_ERROR);
        }

        if (nextLevel.equals(Station.Level.LEVEL8.getValue()) || nextLevel.equals(Station.Level.LEVEL42.getValue())) {
            this.checkCapacity(capacity);
        }
    }

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }
    private void checkName(String name) {
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.STATION_NAME_INVALID);
        }
    }

    private void checkSort(Integer sort) {
        if (sort == null) {
            throw new BaseException(Status.STATION_SORT_INVALID);
        }
    }

    private void checkUpperId(Long upperId) {
        if (upperId == null || upperId <= 0) {
            throw new BaseException(Status.STATION_UPPER_ID_INVALID);
        }
    }

    private void checkCapacity(String capacity) {
        if (StrUtil.isBlank(capacity)) {
            throw new BaseException(Status.STATION_CAPACITY_INVALID);
        }
    }

    private void createStationDevice(CreateStationDeviceRequestBody requestBody) {
        stationService.createDevice(requestBody.fromStationPojo());
    }

    @Operation(summary = "修改站点设备信息",description = "登录后访问")
    @PutMapping("/device")
    public RestResponse<String> updateStationDevice(@RequestHeader(value = "token") String token, @RequestBody UpdateStationDeviceRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            this.handleUpdateStationDevice(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkUpdateRequestBody(UpdateStationDeviceRequestBody requestBody) {
        Long stationId = requestBody.getStationId();
        String name = requestBody.getName();
        Integer sort = requestBody.getSort();
        String capacity = requestBody.getCapacity();

        this.checkStationId(stationId);
        this.checkName(name);
        this.checkSort(sort);

        Station station = stationService.findById(stationId);
        Integer curLevel = station.getLevel();
        if (curLevel.equals(Station.Level.LEVEL8.getValue()) || curLevel.equals(Station.Level.LEVEL42.getValue())) {
            this.checkCapacity(capacity);
        }
    }

    private void handleUpdateStationDevice(UpdateStationDeviceRequestBody requestBody) {
        StationPojo stationPojo = requestBody.fromStationPojo();
        stationPojo.setId(requestBody.getStationId());
        stationService.updateDevice(stationPojo);
    }
}
