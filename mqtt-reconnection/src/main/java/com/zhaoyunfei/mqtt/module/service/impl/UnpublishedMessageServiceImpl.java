package com.zhaoyunfei.mqtt.module.service.impl;


import com.google.common.collect.Lists;
import com.zhaoyunfei.mqtt.module.entity.UnpublishedMessage;
import com.zhaoyunfei.mqtt.module.service.IUnpublishedMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhaoYunFei
 * @since 2022/7/13 10:15
 */
@Service
public class UnpublishedMessageServiceImpl implements IUnpublishedMessageService {
    @Override
    public List<UnpublishedMessage> list() {
        // 这里应该从数据库中查询
        return Lists.newArrayList();
    }

    @Override
    public boolean removeByIds(List<Long> successIds) {
        // 这里应该从数据库中删除
        return true;
    }
}
