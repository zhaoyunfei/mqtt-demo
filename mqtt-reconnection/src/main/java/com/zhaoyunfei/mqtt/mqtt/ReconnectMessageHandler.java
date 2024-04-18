package com.zhaoyunfei.mqtt.mqtt;

import com.google.common.collect.Lists;
import com.zhaoyunfei.mqtt.exceptions.CustomException;
import com.zhaoyunfei.mqtt.module.entity.UnpublishedMessage;
import com.zhaoyunfei.mqtt.module.service.IUnpublishedMessageService;
import com.zhaoyunfei.mqtt.mqtt.listener.AbstractMqttListener;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ZhaoYunFei
 * @since 2023/12/22 9:47
 */
@Slf4j
@Service
public class ReconnectMessageHandler {

    private final IUnpublishedMessageService unpublishedMessageService;

    public ReconnectMessageHandler(IUnpublishedMessageService unpublishedMessageService) {
        this.unpublishedMessageService = unpublishedMessageService;
    }

    public void handleUnpublishedMessages(MqttClient mqttClient) {
        // 处理未发布的消息
        List<UnpublishedMessage> list = unpublishedMessageService.list();
        List<Long> successIds = Lists.newArrayList();
        for (UnpublishedMessage unpublishedMessage : list) {
            try {
                mqttClient.publish(unpublishedMessage.getTopic(),
                        unpublishedMessage.getMessage().getBytes(StandardCharsets.UTF_8),
                        unpublishedMessage.getQs(),
                        unpublishedMessage.isRetained());
                successIds.add(unpublishedMessage.getId());
            } catch (MqttException e) {
                log.error("Failed to publish unpublished message with ID: {}", unpublishedMessage.getId());
            }
        }
        // 删除已成功发布的消息
        unpublishedMessageService.removeByIds(successIds);
    }


    public void resubscribeTopics(MqttClient mqttClient) {
        CustomMqttClient client = (CustomMqttClient) mqttClient;
        List<AbstractMqttListener<?>> topicInfoList = client.getTopicInfoList();
        topicInfoList.forEach(x -> {
            try {
                client.subscribe(x.getTopic(), x.getQos(), x);
            } catch (MqttException e) {
                throw new CustomException("重连之后重新订阅话题失败");
            }
        });
    }
}
