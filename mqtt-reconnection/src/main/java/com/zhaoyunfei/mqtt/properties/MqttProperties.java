package com.zhaoyunfei.mqtt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ZhaoYunFei
 * @since 2024/3/4 17:01
 */
@Data
@ConfigurationProperties(prefix = MqttProperties.PREFIX)
public class MqttProperties {
    public static final String PREFIX="mqtt";

    private boolean enable;

    private String host;

    private String clientId;

    private String username;

    private String password;
}
