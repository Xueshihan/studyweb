package cn.iatc.web.controller.region;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Region;
import cn.iatc.database.entity.Station;
import cn.iatc.web.bean.region.RegionPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RegionService;
import cn.iatc.web.service.StationRelationService;
import cn.iatc.web.service.StationService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
class DeleteRegionRequestBody {
    @Schema(description = "区域id列表")
    private List<Long> regionIds;
}

@Data
class CreateRegionRequestBody {

    @Schema(description = "区域名字", defaultValue = "辽宁省")
    private String name;
    @Schema(description = "区域名字拼音", defaultValue = "liaoningsheng")
    private String pinyin;
    @Schema(description = "区域编号", defaultValue = "001001001")
    private String code;
    @Schema(description = "区域级别", defaultValue = "1")
    private Integer level;
    @Schema(description = "行政区编号", defaultValue = "210000")
    private String adcode;

    @Schema(description = "父级id", defaultValue = "1")
    private Long upperId;

    @Schema(description = "站点code")
    private String stationCode;

    @Schema(defaultValue = "站点名字")
    private String stationName;

    @Schema(defaultValue = "国标列")
    private String nationalCode;

    @Schema(defaultValue = "顺序")
    private Integer sort;

}
@Tag(name = "region", description = "区域相关")
@Slf4j
@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private StationService stationService;

    @Autowired
    private StationRelationService stationRelationService;

    // 删除区域
    @Operation(summary = "删除区域",description = "登录后访问")
    @PostMapping("/region/delete")
    public RestResponse<String> deleteRegion(@RequestHeader(value = "token") String token, @RequestBody DeleteRegionRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.handleDeleteRegion(requestBody.getRegionIds());
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleDeleteRegion(List<Long> regionIds) {

        if (regionIds.size() == 0) {
            return;
        }

        List<Region> regions = regionService.findListByIds(regionIds);
        if (regions.size() == 0) {
            return;
        }

        for (Region region: regions) {
            // 查看自己本身是否有子集
            Long regionCount = regionService.countNextRegionByCode(region.getCode());
            if (regionCount > 0) {
                throw new BaseException(Status.DELETE_ERROR);
            }

            // 查看下级是否有其他子集
            List<Long> stationIds = stationService.findIdsByRegion(region.getId());
            Long count = 0L;
            if(stationIds.size() > 0) {
                count = stationRelationService.countByUpper(stationIds);
            }
            if (count > 0) {
                throw new BaseException(Status.DELETE_ERROR);
            }
        }
        regionService.deleteBatchByIds(regionIds);
    }


    // 创建区域
    @Operation(summary = "创建区域",description = "登录后访问")
    @PostMapping("/region")
    public RestResponse<String> createRegion(@RequestHeader(value = "token") String token, @RequestBody CreateRegionRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCreateRequestBody(requestBody);
            this.addRegion(requestBody);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateRegionRequestBody requestBody) {
        String name = requestBody.getName();
        String pinyin = requestBody.getPinyin();
        String code = requestBody.getCode();
        Integer level = requestBody.getLevel();
        Long upperId = requestBody.getUpperId();
        String stationCode = requestBody.getStationCode();
        String nationalCode = requestBody.getNationalCode();
        Integer sort = requestBody.getSort();
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.REGION_NAME_INVALID);
        }
        if (StrUtil.isBlank(pinyin)) {
            throw new BaseException(Status.REGION_NAME_PINYIN_INVALID);
        }
        if (StrUtil.isBlank(code)) {
            throw new BaseException(Status.REGION_CODE_INVALID);
        }

        this.checkLevelUpperId(code, level, upperId);
        this.checkStationCode(stationCode);

        if(sort == null || sort < 0) {
            throw new BaseException(Status.REGION_SORT_INVALID);
        }
    }

    private void checkLevelUpperId(String code, Integer level, Long upperId) {
        if (level == null) {
            throw new BaseException(Status.REGION_LEVEL_INVALID);
        }

        if (upperId == null && !level.equals(Region.Level.PROVINCE.getValue())) {
            throw new BaseException(Status.REGION_UPPER_INVALID);
        } else {
            Region upperRegion = regionService.findById(upperId);
            if (upperRegion != null) {
                if (!code.startsWith(upperRegion.getCode())) {
                    throw new BaseException(Status.REGION_UPPER_INVALID);
                }
            }
        }
    }

    private void checkStationCode(String stationCode) {
        // 为空可以插入
        if(StrUtil.isNotBlank(stationCode)) {
            // 无某个station报错
            Station station = stationService.findByCode(stationCode);
            if (station == null) {
                throw new BaseException(Status.STATION_CODE_INVALID);
            } else {
                // 判断添加的stationCode是否在level=0,1,2中
                if(!Station.LevelConstant.regionLevels.contains(station.getLevel())) {
                    throw new BaseException(Status.STATION_CODE_INVALID);
                }
            }
        }
    }

    private void addRegion(CreateRegionRequestBody requestBody) {
        RegionPojo regionPojo = new RegionPojo();
        regionPojo.setName(requestBody.getName());
        regionPojo.setPinyin(requestBody.getPinyin());
        regionPojo.setCode(requestBody.getCode());
        regionPojo.setAdCode(requestBody.getAdcode());
        regionPojo.setLevel(requestBody.getLevel());
        regionPojo.setUpperId(requestBody.getUpperId());
        regionPojo.setStationCode(requestBody.getStationCode());
        regionPojo.setStationName(requestBody.getStationName());
        regionPojo.setNationalCode(requestBody.getNationalCode());
        regionPojo.setSort(requestBody.getSort());
        regionService.createRegion(regionPojo);
    }
}
