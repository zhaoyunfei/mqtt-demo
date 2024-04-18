package com.zhaoyunfei.mqtt.mqtt;

import com.zhaoyunfei.mqtt.mqtt.listener.AbstractMqttListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author ZhaoYunFei
 * @since 2023/12/22 11:20
 */
public class CustomMqttClient extends MqttClient {

    private final List<AbstractMqttListener<?>> topicInfoList=new ArrayList<>();

    public CustomMqttClient(String serverURI, String clientId) throws MqttException {
        super(serverURI, clientId);
    }

    public CustomMqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        super(serverURI, clientId, persistence);
    }

    public CustomMqttClient(String serverURI, String clientId, MqttClientPersistence persistence, ScheduledExecutorService executorService) throws MqttException {
        super(serverURI, clientId, persistence, executorService);
    }

    public List<AbstractMqttListener<?>> getTopicInfoList() {
        return topicInfoList;
    }

    public void addTopicInfo(AbstractMqttListener<?> abstractMqttListener) {
        topicInfoList.add(abstractMqttListener);
    }

    public void removeTopicInfo(String topic) {
        topicInfoList.removeIf(info -> info.getTopic().equals(topic));
    }
}
