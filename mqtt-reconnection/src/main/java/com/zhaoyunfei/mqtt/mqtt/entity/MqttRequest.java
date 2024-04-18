package com.zhaoyunfei.mqtt.mqtt.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息请求对象
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MqttRequest extends MqttResponse {

    /**
     * 请求发布的主题
     */
    private String publishTopic;

    /**
     * 请求消息的等级
     */
    private int publishQos;

    /**
     * 是否同步请求
     */
    private boolean sync;
}
