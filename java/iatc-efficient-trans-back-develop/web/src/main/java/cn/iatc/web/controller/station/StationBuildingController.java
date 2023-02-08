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
class CreateRequestBody {
    @Schema(description = "站点级别", defaultValue = "1")
    Integer level;

    @Schema(description = "站点code", defaultValue = "12344")
    String code;

    @Schema(description = "站点名字", defaultValue = "站房")
    String name;

    @Schema(description = "站点位置", defaultValue = "和平区振兴街")
    String address;

    @Schema(description = "父id", defaultValue = "1")
    Long upperId;

    @Schema(description = "站点类型id， level=4", defaultValue = "1")
    Long stationTypeId;

    @Schema( description = "站房数量， level=0,1,2,3,4", defaultValue = "100")
    Integer stationCount;

    @Schema(description = "pmsid", defaultValue = "123")
    String pmsId;

    @Schema(description = "智能化时间", defaultValue = "2023-01-10 08:00:00")
    private Date smartTime;

    // 联系人名
    @Schema(description = "联系人名字", defaultValue = "Tom")
    private String contactName;

    // 联系电话
    @Schema(description = "联系人手机号", defaultValue = "1880000000")
    private String contactPhone;

    // 全景url
    @Schema(description = "全景图url", defaultValue = "www.baidu.com")
    private String panoramacaUrl;

    // 东环状态
    @Schema(description = "动环 0-不启动，1-启动", defaultValue = "0")
    private Integer powerEnvFlag;

    // 网管状态
    @Schema(description = "网关 0-不启动，1-启动", defaultValue = "0")
    private Integer gatewayFlag;

    // 是否是地上状态
    @Schema(description = "在地上 0-不启动，1-启动", defaultValue = "0")
    private Integer onlandsFlag;

    public StationPojo fromStationPojo() {
        StationPojo stationPojo = new StationPojo();
        stationPojo.setLevel(this.level);
        stationPojo.setCode(this.code);
        stationPojo.setName(this.name);
        stationPojo.setAddress(this.address);
        stationPojo.setUpperId(this.upperId);
        stationPojo.setStationTypeId(this.stationTypeId);
        stationPojo.setStationCount(this.stationCount);
        stationPojo.setPmsId(this.pmsId);
        stationPojo.setSmartTime(this.smartTime);
        stationPojo.setContactName(this.contactName);
        stationPojo.setContactPhone(this.contactPhone);
        stationPojo.setPanoramacaUrl(this.panoramacaUrl);
        stationPojo.setPowerEnvFlag(this.powerEnvFlag);
        stationPojo.setGatewayFlag(this.gatewayFlag);
        stationPojo.setOnlandsFlag(this.onlandsFlag);
        return stationPojo;
    }
}

@Data
class UpdateRequestBody extends CreateRequestBody {
    @Schema(description = "站点id", defaultValue = "1")
    private Long stationId;
}

// 站房及以上添加和修改 level=0-省公司，1-市公司，2-区供电公司，3-线路，4-站房类型，5-具体站房
@Tag(name = "station", description = "站点相关")
@Slf4j
@RestController
@RequestMapping("/station")
public class StationBuildingController {

    @Autowired
    private StationService stationService;

    // 添加站点相关信息
    // 需要的参数 code, name, level, stationCount, station_type_id, address, pms_id, longitude, latitude, contact_name, contact_phone,
    // smart_time, panoramaca_url, powerEnvFlag, gateway_flag, onlands_flag
    @Operation(summary = "创建站点(到站房级别)",description = "登录后访问，level=0,1,2,3,4,5.")
    @PostMapping("/building")
    public RestResponse<String> createStation(@RequestHeader(value = "token") String token, @RequestBody CreateRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {

            this.checkCreateRequestBody(requestBody);
            this.addStation(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateRequestBody requestBody) {
        Integer level = requestBody.getLevel();
        String code = requestBody.getCode();
        String name = requestBody.getName();
        String address = requestBody.getAddress();
        Long upperId = requestBody.getUpperId();
        Long stationTypeId = requestBody.getStationTypeId();
        Integer stationCount = requestBody.getStationCount();
        String pmsId = requestBody.getPmsId();

        this.checkLevel(level);
        this.checkName(name);
        if (level.equals(Station.Level.LEVEL0.getValue())) {
            this.checkCode(code);
            this.checkStationCount(stationCount);
        } else if (level.equals(Station.Level.LEVEL1.getValue()) || level.equals(Station.Level.LEVEL2.getValue()) ||
                level.equals(Station.Level.LEVEL3.getValue()) || level.equals(Station.Level.LEVEL4.getValue())) {
            this.checkCode(code);
            this.checkStationCount(stationCount);
            this.checkUpperId(upperId);
            if (level.equals(Station.Level.LEVEL4.getValue())) {
                this.checkStationType(stationTypeId);
            }
        } else if (level.equals(Station.Level.LEVEL5.getValue())) {
            this.checkUpperId(upperId);
            this.checkAddress(address);
            this.checkPmsId(pmsId);
        } else {
            throw new BaseException(Status.STATION_LEVEL_INVALID);
        }
    }

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    private void checkLevel(Integer level) {
        if (level == null || level < 0) {
            throw new BaseException(Status.STATION_LEVEL_INVALID);
        }
    }

    private void checkCode(String code) {
        if (StrUtil.isBlank(code)) {
//            throw new BaseException(Status.STATION_CODE_INVALID);
            return;
        }

        Station station = stationService.findByCode(code);
        if (station != null) {
            throw new BaseException(Status.STATION_EXIST);
        }
    }

    private void checkName(String name) {
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.STATION_NAME_INVALID);
        }
    }

    private void checkAddress(String address) {
        if (StrUtil.isBlank(address)) {
            throw new BaseException(Status.STATION_ADDRESS_INVALID);
        }
    }

    private void checkStationCount(Integer stationCount) {
        if (stationCount == null || stationCount <= 0) {
            throw new BaseException(Status.STATION_COUNT_INVALID);
        }
    }

    private void checkUpperId(Long upperId) {
        if (upperId == null || upperId <= 0) {
            throw new BaseException(Status.STATION_UPPER_ID_INVALID);
        }
    }

    private void checkStationType(Long stationTypeId) {
        if (stationTypeId == null || stationTypeId <= 0) {
            throw new BaseException(Status.STATION_UPPER_ID_INVALID);
        }
    }

    private void checkPmsId(String pmsId) {
        if (StrUtil.isBlank(pmsId)) {
            throw new BaseException(Status.STATION_PMSID_INVALID);
        }
    }

    private void addStation(CreateRequestBody requestBody) {
        StationPojo stationPojo = requestBody.fromStationPojo();
        log.info("==== add station:{}", stationPojo);
        stationService.createBuilding(stationPojo);
    }

    @Operation(summary = "修改站点信息",description = "登录后访问， level=0,1,2,3,4,5")
    @PutMapping("/building")
    public RestResponse<String> updateStation(@RequestHeader(value = "token") String token, @RequestBody UpdateRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {

            this.checkUpdateRequestBody(requestBody);
            this.handleUpdateStation(requestBody);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkUpdateRequestBody(UpdateRequestBody requestBody) {
        Long stationId = requestBody.getStationId();
        String name = requestBody.getName();
        String address = requestBody.getAddress();
        Long stationTypeId = requestBody.getStationTypeId();
        Integer stationCount = requestBody.getStationCount();
        String pmsId = requestBody.getPmsId();
        this.checkStationId(stationId);
        Station station = stationService.findById(stationId);
        if (station == null) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
        Integer level = station.getLevel();

        if (level.equals(Station.Level.LEVEL0.getValue()) || level.equals(Station.Level.LEVEL1.getValue()) || level.equals(Station.Level.LEVEL2.getValue()) ||
                level.equals(Station.Level.LEVEL3.getValue()) || level.equals(Station.Level.LEVEL4.getValue())) {
            this.checkName(name);
            this.checkStationCount(stationCount);
            if (level.equals(Station.Level.LEVEL4.getValue())) {
                this.checkStationType(stationTypeId);
            }

        } else if (level.equals(Station.Level.LEVEL5.getValue())) {
            this.checkName(name);
            this.checkAddress(address);
            this.checkPmsId(pmsId);

        } else {
            throw new BaseException(Status.STATION_LEVEL_INVALID);
        }
    }

    private void handleUpdateStation(UpdateRequestBody requestBody) {
        StationPojo stationPojo = requestBody.fromStationPojo();
        stationPojo.setId(requestBody.getStationId());
        stationService.updateBuilding(stationPojo);
    }


}
