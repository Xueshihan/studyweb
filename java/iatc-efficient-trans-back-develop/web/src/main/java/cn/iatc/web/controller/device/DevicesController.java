package cn.iatc.web.controller.device;

import cn.iatc.web.bean.device.DevicePojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.DeviceService;
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
class DevicesRequestBody {
    private String name;

    private Long baseDeviceId;

    @Schema(description = "页数", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "数量", defaultValue = "10")
    private Integer num = 10;
}

@Data
class DevicesResponseData {
    @JSONField(name = "devices")
    @Schema(name = "devices")
    List<DevicePojo> devicePojoList;

    Long pages;

    Long curPage;

    Long total;

    Long size;
}

@Data
class DevicesStationResponseData {
    @JSONField(name = "devices")
    @Schema(name = "devices")
    List<DevicePojo> devicePojoList;
}

@Tag(name = "device", description = "设备相关")
@Slf4j
@RestController
public class DevicesController {

    @Autowired
    private DeviceService deviceService;

    // 查询所有设备
    @Operation(summary = "根据条件查询所有设备")
    @PostMapping("/devices/all")
    public RestResponse<DevicesResponseData> getDevicesAll(@RequestHeader(value = "token") String token, @RequestBody DevicesRequestBody requestBody) {
        RestResponse<DevicesResponseData> response = new RestResponse<>();
        try {
            this.handleDevicesAll(requestBody, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleDevicesAll(DevicesRequestBody requestBody, RestResponse<DevicesResponseData> response ) {
        DevicesResponseData data = new DevicesResponseData();
        String name = requestBody.getName();
        Long baseDeviceId = requestBody.getBaseDeviceId();
        Integer page = requestBody.getPage();
        Integer num = requestBody.getNum();
        Page<DevicePojo> devicePojoPage = (Page<DevicePojo>) deviceService.findListLike(name, baseDeviceId,page, num);
        data.setDevicePojoList(devicePojoPage.getRecords());
        data.setPages(devicePojoPage.getPages());
        data.setCurPage(devicePojoPage.getCurrent());
        data.setTotal(devicePojoPage.getTotal());
        data.setSize(devicePojoPage.getSize());
        response.setSuccess(data);
    }

    // 查询某个站点下的所有设备
    @Operation(summary = "根据站点查询设备列表")
    @GetMapping("/devices/station")
    public RestResponse<DevicesStationResponseData> getDevicesStation(@RequestHeader(value = "token") String token,
                                                               @RequestParam Long stationId) {
        RestResponse<DevicesStationResponseData> response = new RestResponse<>();
        try {
            this.handleDevicesStation(stationId, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleDevicesStation(Long stationId, RestResponse<DevicesStationResponseData> response) {
        DevicesStationResponseData data = new DevicesStationResponseData();
        data.setDevicePojoList(deviceService.findListByStation(stationId));
        response.setSuccess(data);
    }
}
