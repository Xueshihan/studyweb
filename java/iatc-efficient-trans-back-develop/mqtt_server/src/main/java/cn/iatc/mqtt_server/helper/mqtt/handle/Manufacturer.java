package cn.iatc.mqtt_server.helper.mqtt.handle;

import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.OriginalPower;
import cn.iatc.mqtt_server.common.context.SpringContextHolder;
import cn.iatc.mqtt_server.service.DevicePointService;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public abstract class Manufacturer {
    protected static DevicePointService devicePointService;
    static {
        devicePointService = SpringContextHolder.getBean(DevicePointService.class);
    }

    protected Device device;
    public abstract void handleData(JSONObject jsonObject, List<OriginalPower> originalPowerList);
}
