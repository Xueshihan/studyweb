package cn.iatc.web.helper.mqtt;

import cn.iatc.mqtt.MqttManager;
import cn.iatc.web.common.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// 不同mqtt建立不同类
@Slf4j
public class MqttClientHelper implements MqttCallbackExtended {

    private String beanName;

    public MqttClientHelper(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("连接成功 connectComplete....");
        // 连接成功后，注册topic
        MqttManager mqttManager = SpringContextHolder.getBean(this.beanName);
        mqttManager.subscribe("/iatc/test");
    }

    //连接异常断开后，调用
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("连接断开connectionLost=======throwable:{}", throwable.toString());
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        // TODO 根据不同topic处理不同业务逻辑
        String message = new String(mqttMessage.getPayload());
        log.info("消息接收成功 messageArrived topic:{}, message:{}", topic, message);
    }

    //消息发送成功后，调用
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("===== deliveryComplete:{}", iMqttDeliveryToken.getMessageId());
    }

    public void sendMessage(String topic, String message) {
        MqttManager mqttManager = SpringContextHolder.getBean(this.beanName);
        mqttManager.publish(topic, message);
    }

}
