package com.zhaoyunfei.mqtt.module.service;


import com.zhaoyunfei.mqtt.module.entity.UnpublishedMessage;

import java.util.List;

/**
 * @author ZhaoYunFei
 * @since 2022/7/13 10:14
 */
public interface IUnpublishedMessageService {

    /**
     * 查询所有未推送消息
     *
     * @return 消息集合
     */
    List<UnpublishedMessage> list();

    /**
     * 根据消息id移除未推送消息
     *
     * @param successIds 推送成功的消息id
     * @return 是否移除成功
     */
    boolean removeByIds(List<Long> successIds);
}
