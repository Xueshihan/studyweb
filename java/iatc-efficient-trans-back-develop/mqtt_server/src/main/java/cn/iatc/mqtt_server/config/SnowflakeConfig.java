package cn.iatc.mqtt_server.config;

import cn.iatc.mqtt_server.utils.SnowFlakeFactory;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "snowflake")
@AutoConfigureBefore(MqttConfig.class)
public class SnowflakeConfig {

    private long datacenterId;
    private long machineId;

    @Bean
    public SnowFlakeFactory getSnowFlakeFactory() {
        return new SnowFlakeFactory(datacenterId, machineId);
    }
}
