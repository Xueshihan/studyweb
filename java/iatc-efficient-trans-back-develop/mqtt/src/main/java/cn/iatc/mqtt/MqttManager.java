package cn.iatc.mqtt;

import cn.hutool.core.util.StrUtil;
import cn.iatc.mqtt.data.QosInfo;
import cn.iatc.mqtt.utils.SslUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLSocketFactory;

@Slf4j
@Data
public class MqttManager {

    private MqttAsyncClient mqttClient;

    private MqttCallback mqttCallback;

    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken iMqttToken) {
            log.info("connect success");
        }

        @Override
        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
            log.info("connect fail throwable:{}", throwable.toString());
            throwable.printStackTrace();
        }
    };

    public MqttManager(String host, String clientId) throws MqttException {
        mqttClient = new MqttAsyncClient(host, clientId, new MemoryPersistence());
    }

    public void connect(String userName, String password, int timeout, int keepAlive, String caPath) {
        try {
            if (mqttClient == null) {
                return;
            }
            log.info("======= mqtt 1 ");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            if (StrUtil.isBlank(userName)) {
                options.setUserName("");
            } else {
                options.setUserName(userName);
            }

            if (StrUtil.isBlank(password)) {
                options.setPassword("".toCharArray());
            } else {
                options.setPassword(password.toCharArray());
            }

            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepAlive);
            //自动重连
            options.setAutomaticReconnect(true);
            /**
             * 设置为true后意味着：客户端断开连接后emq不保留会话，否则会产生订阅共享队列的存活
             客户端收不到消息的情况
             * 因为断开的连接还被保留的话，emq会将队列中的消息负载到断开但还保留的客户端，导致存活的客户
             端收不到消息
             * 解决该问题有两种方案:1.连接断开后不要保持；2.保证每个客户端有固定的clientId
             */
            options.setCleanSession(true);

            if (StrUtil.isNotBlank(caPath)) {
                SSLSocketFactory res = SslUtil.getSocketFactory(caPath);
                log.info("======= SSLSocketFactory res:" + res);
                options.setSocketFactory(res);
            }
            log.info("======= mqtt 2 ");
            mqttClient.setCallback(mqttCallback);
            // 暂时不要回调监听，在带有证书的时候，回调会报错，证书报错
//            mqttClient.connect(options, iMqttActionListener);
            mqttClient.connect(options);
            log.info("======= mqtt 3 ");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("mqtt connect error :{}", e);
        }
    }

    public void subscribe(String topic){
        log.info("开始订阅主题" + topic);
        subscribe(topic,2);
    }

    public void subscribe(String topic,int qos){
        try {
            if (mqttClient != null) {
                mqttClient.subscribe(topic, qos);
            }
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void publish(String topic, String pushMessage){
        log.info("pushlish=====pushlish topic:{}, pushMessage:{}",topic, pushMessage);
        this.publish(QosInfo.Type.Qos2,false, topic, pushMessage);
    }

    public void publish(QosInfo.Type qosType, String topic, String pushMessage){
        log.info("pushlish=====pushlish topic:{}, pushMessage:{}",topic, pushMessage);
        this.publish(qosType,false, topic, pushMessage);
    }

    private void publish(QosInfo.Type qosType, boolean retained, String topic, String pushMessage){
        MqttDeliveryToken token = null;

        try {
            MqttMessage message=new MqttMessage();
            message.setQos(qosType.getValue());
            message.setRetained(retained);
            message.setPayload(pushMessage.getBytes());

            if (mqttClient != null) {
                mqttClient.publish(topic, message);
            }
        }catch (MqttPersistenceException e){
            e.printStackTrace();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}
