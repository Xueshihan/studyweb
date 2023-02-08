package cn.iatc.mqtt_server.config;

import cn.hutool.core.util.StrUtil;
import cn.iatc.mqtt.MqttManager;
import cn.iatc.mqtt_server.helper.mqtt.MqttClientHelper;
import cn.iatc.mqtt_server.utils.SnowFlakeFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt-client")
public class MqttConfig {

    @Autowired
    private SnowFlakeFactory snowFlakeFactory;

    private String hostUrl;

    private String clientId;

    private String userName;

    private String password;

    private int connectTimeout;

    private int keepalive;

    private String caCrt;

    private String[] topics;

    @Bean(name = "mqttManager")
    public MqttManager mqttManager() {
        MqttManager mqttManager = null;
        try {
            if (StrUtil.isBlank(clientId)) {
                clientId = snowFlakeFactory.nextIdStr();
            }
            mqttManager  = new MqttManager(hostUrl, clientId);
            log.info("==== topics:{}", (Object) topics);
            MqttClientHelper mqttClientHelper = new MqttClientHelper("mqttManager");
            mqttClientHelper.setTopics(topics);
            mqttManager.setMqttCallback(mqttClientHelper);
            String caPath = null;
            log.info("==== user.dir:{}",System.getProperty("user.dir") );
            if (StrUtil.isNotBlank(caCrt)) {
                caPath = StrUtil.format("{}{}{}", System.getProperty("user.dir"), File.separator, caCrt);
            }
            mqttManager.connect(userName, password, connectTimeout, keepalive, caPath);
            log.info("mqtt connect caPath:{}", caPath);
        } catch (MqttException e) {
            log.error("MqttManager error:{}", e);
        }
        return mqttManager;
    }

}
