package com.zhaoyunfei.mqtt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZhaoYunFei
 * @since 2024/4/17 14:07
 */
@SpringBootApplication(scanBasePackages = {"com.zhaoyunfei.mqtt"})
public class MqttReconnectionApplication {
    public static void main(String[] args) {

        SpringApplication.run(MqttReconnectionApplication.class, args);
    }
}