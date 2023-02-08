package cn.iatc.mqtt_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"cn.iatc"})
@MapperScan("cn.iatc.mqtt_server.mapper")
public class MqttServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttServerApplication.class, args);
    }

}
