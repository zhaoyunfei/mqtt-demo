package com.zhaoyunfei.mqtt.config;


import com.zhaoyunfei.mqtt.properties.MqttProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhaoYunFei
 */
@EnableConfigurationProperties
@Configuration
public class PropertiesConfig {

    @Bean
    public MqttProperties mqttProperties() {
        return new MqttProperties();
    }

}
