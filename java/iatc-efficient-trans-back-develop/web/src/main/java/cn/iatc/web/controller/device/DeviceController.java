package cn.iatc.web.controller.device;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Device;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.DeviceService;
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
class BaseDeviceRequestBody {
    @Schema(description = "设备名字", required = true)
    private String name;

    @Schema(description = "设备唯一码", required = true)
    private String sn;

    @Schema(description = "设备类型id", required = true)
    private Long deviceTypeId;

    @Schema(description = "采集点号")
    private String pointId;

    @Schema(description = "容量")
    private String capacity;

    @Schema(description = "网关id")
    private String gatewayId;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "备注")
    private String remark;
}

@Data
class CreateDeviceRequestBody extends BaseDeviceRequestBody {
    @Schema(description = "站点id", required = true)
    private Long stationId;

}

@Data
class UpdateDeviceRequestBody extends BaseDeviceRequestBody{
    @Schema(description = "设备id")
    private Long deviceId;
}

@Data
class DeleteDeviceRequestBody {
    @Schema(description = "设备id列表")
    private List<Long> deviceIds;
}


@Tag(name = "device", description = "设备相关")
@Slf4j
@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Operation(summary = "新增设备")
    @PostMapping("/device")
    public RestResponse<String> createDevice(@RequestHeader(value = "token") String token, @RequestBody CreateDeviceRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCreateRequestBody(requestBody);
            this.handleAddDevice(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateDeviceRequestBody requestBody) {
        this.checkName(requestBody.getName());
        this.checkSn(requestBody.getSn());
        this.checkSnExist(requestBody.getSn());
        this.checkDeviceType(requestBody.getDeviceTypeId());
    }

    private void checkDeviceId(Long deviceId) {
        if (deviceId == null || deviceId <= 0) {
            throw new BaseException(Status.DEVICE_ID_INVALID);
        }
    }

    private void checkName(String name) {
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.DEVICE_NAME_INVALID);
        }
    }

    private void checkSn(String sn) {
        if (StrUtil.isBlank(sn)) {
            throw new BaseException(Status.DEVICE_SN_INVALID);
        }
    }

    private void checkSnExist(String sn) {
        Device device = deviceService.findBySn(sn);
        if (device != null) {
            throw new BaseException(Status.DEVICE_EXIST);
        }

    }

    private void checkDeviceType(Long deviceTypeId) {
        if (deviceTypeId == null || deviceTypeId <= 0) {
            throw new BaseException(Status.DEVICE_TYPE_ID_INVALID);
        }
    }

    private void handleAddDevice(CreateDeviceRequestBody requestBody) {
        Device device = new Device();
        device.setName(requestBody.getName());
        device.setSn(requestBody.getSn());
        device.setDeviceTypeId(requestBody.getDeviceTypeId());
        device.setPointId(requestBody.getPointId());
        device.setCapacity(requestBody.getCapacity());
        device.setGatewayId(requestBody.getGatewayId());
        device.setAddress(requestBody.getAddress());
        device.setRemark(requestBody.getRemark());
        deviceService.createDevice(requestBody.getStationId(), device);
    }

    @Operation(summary = "修改设备")
    @PutMapping("/device")
    public RestResponse<String> updateDevice(@RequestHeader(value = "token") String token, @RequestBody UpdateDeviceRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            this.handleUpdateDevice(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkUpdateRequestBody(UpdateDeviceRequestBody requestBody) {
        this.checkDeviceId(requestBody.getDeviceId());
        this.checkName(requestBody.getName());
        this.checkUpdateDevice(requestBody.getDeviceId(), requestBody.getSn());
        this.checkDeviceType(requestBody.getDeviceTypeId());
    }

    private void checkUpdateDevice(Long deviceId, String sn) {
        Device device = deviceService.findById(deviceId);
        if (device == null) {
            throw new BaseException(Status.DEVICE_ID_INVALID);
        }
        if (!device.getSn().equals(sn)) {
            this.checkSnExist(sn);
        }
    }

    private void handleUpdateDevice(UpdateDeviceRequestBody requestBody) {
        Device device = new Device();
        device.setId(requestBody.getDeviceId());
        device.setName(requestBody.getName());
        device.setSn(requestBody.getSn());
        device.setDeviceTypeId(requestBody.getDeviceTypeId());
        device.setPointId(requestBody.getPointId());
        device.setCapacity(requestBody.getCapacity());
        device.setGatewayId(requestBody.getGatewayId());
        device.setAddress(requestBody.getAddress());
        device.setRemark(requestBody.getRemark());
        deviceService.updateDevice(device);
    }

    @Operation(summary = "删除设备")
    @PostMapping("/device/delete")
    public RestResponse<String> deleteDevice(@RequestHeader(value = "token") String token, @RequestBody DeleteDeviceRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkDeleteRequestBody(requestBody);
            this.handleDeleteDevice(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkDeleteRequestBody(DeleteDeviceRequestBody requestBody) {
        this.checkDeviceIds(requestBody.getDeviceIds());
    }

    private void checkDeviceIds(List<Long> deviceIds) {
        if (deviceIds.size() == 0) {
            throw new BaseException(Status.DEVICE_ID_INVALID);
        }
    }

    private void handleDeleteDevice(DeleteDeviceRequestBody requestBody) {
        deviceService.deleteByIds(requestBody.getDeviceIds());
    }
}
