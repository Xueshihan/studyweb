package cn.iatc.mqtt_server.helper.mqtt.handle;

import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.DevicePoint;
import cn.iatc.database.entity.OriginalPower;
import cn.iatc.mqtt_server.utils.TimeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 电表厂商 处理低压数据
@Data
public class ElectricityManu extends Manufacturer{


    @Override
    public void handleData(JSONObject jsonObject, List<OriginalPower> originalPowerList) {
        String deviceCode = (String) jsonObject.get("dev_id");
        String dateStr = (String) jsonObject.get("timestamp");
        Date createdTime = TimeUtil.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
        if (createdTime == null) {
            createdTime = new Date();
        }
        JSONObject points = jsonObject.getJSONObject("points");
        if (points == null) {
            return;
        }
        if (device == null) {
            return;
        }
        List<DevicePoint> devicePoints = devicePointService.findByDevice(device.getId());
        if (devicePoints.size() == 0) {
            return;
        }
        List<String> properties = new ArrayList<>();
        for (DevicePoint devicePoint: devicePoints) {
            properties.add(devicePoint.getProperty());
        }
        for (String key : points.keySet()) {
            String val = points.getString(key);

            if (properties.contains(key)) {
                OriginalPower originalPower = new OriginalPower();
                originalPower.setCode(deviceCode);
                originalPower.setType(key);
                originalPower.setValue(val);
                originalPower.setCreatedTime(createdTime);
                originalPowerList.add(originalPower);
            }
        }
    }
}
