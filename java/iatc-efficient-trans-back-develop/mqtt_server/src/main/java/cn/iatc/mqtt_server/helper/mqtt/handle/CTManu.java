package cn.iatc.mqtt_server.helper.mqtt.handle;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.DevicePoint;
import cn.iatc.database.entity.OriginalPower;
import cn.iatc.mqtt_server.utils.TimeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//CT 厂商, 处理高压数据
@Data
public class CTManu extends Manufacturer{
    @Override
    public void handleData(JSONObject jsonObject, List<OriginalPower> originalPowerList) {
        if (device == null) {
            return;
        }
        List<DevicePoint> devicePoints = devicePointService.findByDeviceType(device.getDeviceTypeId());
        if (devicePoints.size() == 0) {
            return;
        }
        JSONObject deviceObj = jsonObject.getJSONObject("device");
        if (deviceObj != null) {
            String deviceCode = deviceObj.getString("device_id");
            JSONArray jsonArray = jsonObject.getJSONArray("devPointList");
            if (jsonArray != null && jsonArray.size() > 0) {
                for (Object item: jsonArray) {
                    for (DevicePoint devicePoint: devicePoints) {
                        String property = devicePoint.getProperty();
                        String value = ((JSONObject)item).getString(property);
                        if (StrUtil.isBlank(value)) {
                            continue;
                        }
                        OriginalPower originalPower = new OriginalPower();
                        originalPower.setCode(deviceCode);
                        originalPower.setType(property);
                        originalPower.setValue(value);
                        long time = ((JSONObject)item).getLong("timestamp");
                        originalPower.setCreatedTime(TimeUtil.timeToDate(time));
                        originalPowerList.add(originalPower);
                    }
                }
            }
        }
    }
}
