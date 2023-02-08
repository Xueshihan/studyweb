package cn.iatc.web.controller.device_type;

import cn.iatc.database.entity.DeviceType;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.DeviceTypeService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
class DeviceTypeResponseData {
    private List<DeviceType> deviceTypes;
}
@Tag(name = "device_type", description = "设备类型相关")
@Slf4j
@RestController
public class DeviceTypesController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Operation(summary = "查询设备类型列表")
    @GetMapping("/device_type/list")
    public RestResponse<DeviceTypeResponseData> getDeviceTypesByFactory(@RequestHeader(value = "token") String token,
                                                  @Schema(name = "factory_id")
                                                  @Parameter(required = true)
                                                  @RequestParam Long factoryId) {

        RestResponse<DeviceTypeResponseData> response = new RestResponse<>();
        try {
            this.checkRequestBody(factoryId);
            this.handleDeviceTypes(factoryId, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkRequestBody(Long factoryId) {
        if (factoryId == null || factoryId <= 0) {
            throw new BaseException(Status.FACTORY_ID_INVALID);
        }
    }

    private void handleDeviceTypes(Long factoryId, RestResponse<DeviceTypeResponseData> response) {
        DeviceTypeResponseData data = new DeviceTypeResponseData();
        data.setDeviceTypes(deviceTypeService.findListByFactory(factoryId));
        response.setSuccess(data);
    }
}
