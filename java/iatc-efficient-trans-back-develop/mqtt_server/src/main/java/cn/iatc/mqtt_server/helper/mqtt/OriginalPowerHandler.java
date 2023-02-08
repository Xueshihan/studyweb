package cn.iatc.mqtt_server.helper.mqtt;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.OriginalPower;
import cn.iatc.mqtt_server.common.context.SpringContextHolder;
import cn.iatc.mqtt_server.helper.mqtt.handle.IatcFactory;
import cn.iatc.mqtt_server.helper.mqtt.handle.Manufacturer;
import cn.iatc.mqtt_server.service.DevicePointService;
import cn.iatc.mqtt_server.service.DeviceService;
import cn.iatc.mqtt_server.service.OriginalPowerService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OriginalPowerHandler {

    // 解决自启动线程时，定义的service层时，对象为空问题
    private static OriginalPowerService originalPowerService;
    private static DeviceService deviceService;
    private static DevicePointService devicePointService;
    static {
        originalPowerService = SpringContextHolder.getBean(OriginalPowerService.class);
        deviceService = SpringContextHolder.getBean(DeviceService.class);
        devicePointService = SpringContextHolder.getBean(DevicePointService.class);
    }

    public void handleSensorData(String message) {
        List<OriginalPower> originalPowerList = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(message);
        String manuStr = null;
        String deviceCode = null;
        if (jsonObject.get("device") != null) {
            JSONObject deviceObj = jsonObject.getJSONObject("device");
            deviceCode = deviceObj.getString("device_id");
            manuStr = IatcFactory.CT_MAU;
        } else if (jsonObject.get("dev_id") != null) {
            deviceCode = jsonObject.getString("dev_id");
            manuStr = IatcFactory.ELECTRICITY_MAU;
        }
        if (StrUtil.isBlank(deviceCode)) {
            return;
        }
        Device device = deviceService.findBySn(deviceCode);
        if (device == null) {
            return;
        }

        this.handleOriginalData(manuStr, jsonObject, originalPowerList, device);
    }

    private void handleOriginalData(String manuStr, JSONObject jsonObject, List<OriginalPower> originalPowerList, Device device) {
        IatcFactory iatcFactory = new IatcFactory();
        Manufacturer manufacturer = iatcFactory.getManufacturer(manuStr);
        if (manufacturer == null) {
            return;
        }
        try {
            manufacturer.setDevice(device);
            manufacturer.handleData(jsonObject, originalPowerList);
            log.info("处理后结果 originalPowerList:{}, null:{}", originalPowerList.toString(), (originalPowerService == null));
            if (originalPowerList.size() > 0) {
                originalPowerService.insertBatch(originalPowerList);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
