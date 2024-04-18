package com.zhaoyunfei.mqtt.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhaoYunFei
 * @since 2023/12/22 15:26
 */
@Slf4j
public class CustomMqttCallback implements MqttCallbackExtended {

    public static final Integer RECONNECT_LIMIT = 30;

    private final String topic;
    private final MqttClient client;
    private final ReconnectMessageHandler mqttMessageHandler;
    private final MqttConnectOptions options;

    public CustomMqttCallback(String topic,
                              MqttClient client,
                              ReconnectMessageHandler mqttMessageHandler,
                              MqttConnectOptions options) {
        this.topic = topic;
        this.client = client;
        this.mqttMessageHandler = mqttMessageHandler;
        this.options = options;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("[MQTT]连接断开，1分钟之后尝试重连");
        for (int j = 0; j < RECONNECT_LIMIT; j++) {
            try {
                TimeUnit.SECONDS.sleep(60);
                client.connect(options);
                client.publish(topic, "online".getBytes(StandardCharsets.UTF_8), 1, true);
                log.info("[MQTT]断线重连成功！重连结果={}", client.isConnected());

                break;
            } catch (MqttException e) {
                log.error("第" + j + "次断线重连失败!失败原因：", e);
            } catch (InterruptedException e) {
                log.error("线程异常", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        // 暂时不需要处理

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // 暂时不需要处理
    }

    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        if (client.isConnected()) {
            log.info("重连之后重新订阅所有的话题");
            mqttMessageHandler.resubscribeTopics(client);
            // 这里还可以处理其他事情,比如未推送成功的话题重新推送等等
            mqttMessageHandler.handleUnpublishedMessages(client);
        }
    }
}
