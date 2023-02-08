package cn.iatc.mqtt_server.helper.mqtt.handle;

public abstract class AbstractFactory {
    public abstract Manufacturer getManufacturer(String name);
}
