package com.zhaoyunfei.mqtt.module.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhaoYunFei
 * @since 2024/4/18 10:03
 */
@Data
@ApiModel(value = "UnpublishedMessage对象", description = "未推送消息")
public class UnpublishedMessage {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id", example = "1")
    private Long id;

    @ApiModelProperty(value = "话题", example = "")
    private String topic;

    @ApiModelProperty(value = "消息质量", example = "1")
    private Integer qs;

    @ApiModelProperty(value = "消息是否保留", example = "1")
    private boolean retained;

    @ApiModelProperty(value = "消息内容", example = "")
    private String message;
}
