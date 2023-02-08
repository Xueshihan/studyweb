package cn.iatc.mqtt_server.helper.mqtt.handle;

import cn.iatc.database.entity.Device;
import lombok.Data;

@Data
public class IatcFactory extends AbstractFactory{
    public static String ELECTRICITY_MAU = "ElectricityMau";
    public static String CT_MAU = "CTMau";

    private Device device;

    @Override
    public Manufacturer getManufacturer(String name) {
        if (name == null) {
            return null;
        }
        if (name.equals(ELECTRICITY_MAU)) {
            return new ElectricityManu();
        } else if (name.equals(CT_MAU)) {
            return new CTManu();
        }
        return null;
    }
}
