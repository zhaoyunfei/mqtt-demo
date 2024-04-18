package com.zhaoyunfei.mqtt.mqtt.entity;


import lombok.Data;

import java.util.Date;

/**
 * 消息响应对象
 *
 * @author xingshuang
 */
@Data
public class MqttResponse {

    public static final String PROTOCOL_NAME ="MQTT-V0.1";

    /**
     * 消息id
     */
    private String id;

    /**
     * 消息主题
     */
    private String responseTopic;

    /**
     * 消息等级
     */
    private int responseQos;

    /**
     * 消息内容
     */
    private String data;

    /**
     * 时间
     */
    private Date time;

    /**
     * 协议
     */
    private String protocol;
}
