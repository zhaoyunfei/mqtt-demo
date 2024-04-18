package com.zhaoyunfei.mqtt.mqtt;


import cn.hutool.core.thread.ThreadUtil;
import com.zhaoyunfei.mqtt.exceptions.CustomException;
import com.zhaoyunfei.mqtt.mqtt.constant.MqttTopicConstant;
import com.zhaoyunfei.mqtt.properties.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Mqtt的配置
 *
 * @author xingshuang
 */
@Configuration
@Slf4j
public class MqttConfig {

    private final ReconnectMessageHandler mqttMessageHandler;

    public MqttConfig(ReconnectMessageHandler mqttMessageHandler) {
        this.mqttMessageHandler = mqttMessageHandler;
    }


    @Bean
    public CustomMqttClient mqttClient(MqttProperties mqttProperties,
                                       MqttConnectOptions options) throws MqttException {
        // 添加连接判断:只有mqttProperties的enable为true时才做连接
        String topic = MqttTopicConstant.DATA_ONLINE_INFORMATION;
        CustomMqttClient client = new CustomMqttClient(mqttProperties.getHost(),
                mqttProperties.getClientId(), new MemoryPersistence());
        if (mqttProperties.isEnable()) {
            try {
                // 创建连接
                boolean result = this.doConnect(client, options);
                if (!result) {
                    tryConnect(mqttProperties, topic, client, options);
                }
                client.setCallback(new CustomMqttCallback(topic, client, mqttMessageHandler, options));
                if (result) {
                    client.publish(topic, "online".getBytes(StandardCharsets.UTF_8), 1, true);
                    log.info("连接Mqtt服务器成功，地址={}", mqttProperties.getHost());
                } else {
                    log.error("连接Mqtt服务器失败，地址={}", mqttProperties.getHost());
                }
                return client;
            } catch (MqttException e) {
                log.error("MQTT连接失败" + e.getMessage(), e);
                return new CustomMqttClient(mqttProperties.getHost(), mqttProperties.getClientId(), new MemoryPersistence());
            }
        }
        return client;
    }


    /**
     * 启动时连接不成功
     *
     * @param mqttProperties 配置信息
     * @param topic          话题
     * @param client         mqtt客户端
     * @param options        连接属性
     */
    private void tryConnect(MqttProperties mqttProperties, String topic, MqttClient client, MqttConnectOptions options) {
        // 如果启动时连接不成功则新开线程每隔20s重连一次.直到连接为止
        ThreadUtil.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    log.error("延时等待失败!");
                }
                boolean doConnect = this.doConnect(client, options);
                if (doConnect) {
                    try {
                        client.publish(topic, "online".getBytes(StandardCharsets.UTF_8), 1, true);
                    } catch (Exception e) {
                        log.error("重连Mqtt服务器成功，推送在线消息失败!");
                    }
                    log.info("重连Mqtt服务器成功，地址={}", mqttProperties.getHost());
                    break;
                }
            }
        });
    }

    private boolean doConnect(MqttClient client, MqttConnectOptions options) {
        try {
            client.connect(options);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    @Bean
    public MqttConnectOptions options(MqttProperties mqttProperties) {
        try {
            String topic = String.format(MqttTopicConstant.DATA_ONLINE_INFORMATION);
            MqttConnectOptions options = new MqttConnectOptions();
            // 清空session
            options.setCleanSession(false);
            // 用户名
            options.setUserName(mqttProperties.getUsername());
            // 密码
            options.setPassword(mqttProperties.getPassword().toCharArray());
            // 自动重连
            options.setAutomaticReconnect(true);
            // 设置超时时间
            options.setConnectionTimeout(10);
            // 设置会话心跳时间
            options.setKeepAliveInterval(20);
            // 设置同时发送消息数
            options.setMaxInflight(200);
            // 遗嘱模式
            options.setWill(topic, "offline".getBytes(StandardCharsets.UTF_8), 1, true);
            return options;
        } catch (Exception e) {
            throw new CustomException("创建mqtt连接对象失败,失败原因:" + e.getMessage());
        }

    }
}
