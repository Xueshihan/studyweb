package cn.iatc.web.controller.region;

import cn.iatc.database.entity.Station;
import cn.iatc.web.bean.region.RegionPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RegionService;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
class RegionsRequestBody {
    @Schema(description = "级别", defaultValue = "1")
    private Integer level;

    @Schema(description = "名字", defaultValue = "辽宁省")
    private String name;

    @Schema(description = "页数", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "数量", defaultValue = "10")
    private Integer num = 10;
}

@Data
class RegionListData {
    @JSONField(name = "regions")
    @Schema(name = "regions")
    List<RegionPojo> regionPojoList;

    Long pages;

    Long curPage;

    Long total;

    Long size;
}

@Tag(name = "region", description = "区域相关")
@Slf4j
@RestController
public class RegionsController {

    @Autowired
    private RegionService regionService;

    @Operation(summary = "获取区域列表",description = "登录后访问")
    @PostMapping("/region/list")
    public RestResponse<RegionListData> getRegionList(@RequestHeader(value = "token") String token, @RequestBody(required = false) RegionsRequestBody requestBody) {

        RestResponse<RegionListData> response = new RestResponse<>();
        try {
            this.handleRegionList(requestBody, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleRegionList(RegionsRequestBody requestBody, RestResponse<RegionListData> response) {
        Integer level = null;
        String name = null;
        Integer pageIndex = 1;
        Integer num = 10;
        log.info("===== requestBody:{}", requestBody);
        if (requestBody != null) {
            level = requestBody.getLevel();
            name = requestBody.getName();
            pageIndex = requestBody.getPage();
            num = requestBody.getNum();
        }
        IPage<RegionPojo> regionPojoPage = regionService.findListLike(level, name, pageIndex, num);
        List<RegionPojo> regionPojoList = regionPojoPage.getRecords();
        log.info("====regions regionPojoList:{}", regionPojoList);
        for(RegionPojo regionPojo: regionPojoList) {
            Station station = new Station();
            if (regionPojo.getStationId() != null) {
                station.setId(regionPojo.getStationId());
                station.setCode(regionPojo.getStationCode());
                station.setName(regionPojo.getStationName());
            }
            regionPojo.setStation(station);
        }
        RegionListData regionListData = new RegionListData();
        regionListData.setRegionPojoList(regionPojoList);
        regionListData.setPages(regionPojoPage.getPages());
        regionListData.setCurPage(regionPojoPage.getCurrent());
        regionListData.setTotal(regionPojoPage.getTotal());
        regionListData.setSize(regionPojoPage.getSize());
        response.setSuccess(regionListData);
    }
}
