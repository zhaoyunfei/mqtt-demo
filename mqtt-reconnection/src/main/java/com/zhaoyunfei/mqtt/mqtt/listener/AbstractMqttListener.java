package com.zhaoyunfei.mqtt.mqtt.listener;

import cn.hutool.core.thread.ThreadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhaoyunfei.mqtt.mqtt.CustomMqttClient;
import com.zhaoyunfei.mqtt.mqtt.entity.MqttRequest;
import com.zhaoyunfei.mqtt.mqtt.entity.MqttResponse;
import com.zhaoyunfei.mqtt.mqtt.entity.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author ZhaoYunFei
 */
@Slf4j
public abstract class AbstractMqttListener<T> implements IMqttMessageListener {

    protected ObjectMapper objectMapper;

    protected MqttClient mqttClient;

    protected String topic;

    protected int qos = 1;


    protected AbstractMqttListener(ObjectMapper objectMapper,
                                   MqttClient mqttClient) {
        this.objectMapper = objectMapper;
        this.mqttClient = mqttClient;
    }

    public String getTopic() {
        return this.topic;
    }

    public Integer getQos() {
        return this.qos;
    }

    @PostConstruct
    public void subscribe() {
        try {
            if (this.mqttClient.isConnected()) {
                this.mqttClient.subscribe(this.topic, this.qos, this);
                CustomMqttClient client = (CustomMqttClient) this.mqttClient;
                client.addTopicInfo(this);
            }
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        }
    }


    @PreDestroy
    public void unsubscribe() {
        try {
            if (this.mqttClient.isConnected()) {
                this.mqttClient.unsubscribe(this.topic);
                CustomMqttClient client = (CustomMqttClient) this.mqttClient;
                client.removeTopicInfo(this.topic);
            }
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void messageArrived(String subscribeTopic, MqttMessage message) {
        String content = new String(message.getPayload(), StandardCharsets.UTF_8);
        MqttRequest request = this.preprocessor(content);
        if (request == null || !MqttResponse.PROTOCOL_NAME.equals(request.getProtocol())) {
            log.error("非本协议,不做处理");
            return;
        }
        if (request.isSync()) {
            ThreadUtil.execute(() -> {
                ResponseResult<T> result = null;
                try {
                    T data = this.processor(request);
                    result = ResponseResult.succeed(data);
                } catch (Exception e) {
                    result = ResponseResult.fail(e.getMessage());
                } finally {
                    this.postprocessor(result, request);
                }
            });
        } else {
            ThreadUtil.execute(() -> {
                try {
                    this.processor(request);
                } catch (Exception e) {
                    log.error("实际业务处理异常,话题={}，异常原因={}", request.getPublishTopic(), e);
                }
            });
        }
    }

    private MqttRequest preprocessor(String content) {
        try {
            return this.objectMapper.readValue(content, MqttRequest.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 根据实际业务处理mqtt请求
     *
     * @param request mqtt请求的数据
     * @return 数据处理结果
     * @throws Exception 执行异常
     */
    public abstract T processor(MqttRequest request) throws Exception;

    private void postprocessor(ResponseResult<T> data, MqttRequest request) {
        try {
            String result = this.objectMapper.writeValueAsString(data);
            request.setData(result);
            request.setTime(new Date());
            String json = this.objectMapper.writeValueAsString(request);
            MqttMessage mqttMessage = new MqttMessage(json.getBytes(StandardCharsets.UTF_8));
            mqttMessage.setQos(request.getResponseQos());
            this.mqttClient.publish(request.getResponseTopic(), mqttMessage);
        } catch (Exception e) {
            log.error("同步处理返回数据失败,失败原因", e);
        }
    }

}
