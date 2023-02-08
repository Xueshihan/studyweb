package cn.iatc.mqtt.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MqttClientCallback implements MqttCallbackExtended {


    @Override
    public void connectComplete(boolean b, String s) {
        log.info("连接成功 connectComplete....");
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("连接断开connectionLost=======");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        log.info("连接成功 messageArrived topic:{}, message:{}", topic, message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
