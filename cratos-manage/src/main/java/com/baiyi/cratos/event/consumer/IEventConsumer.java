package com.baiyi.cratos.event.consumer;

import com.baiyi.cratos.common.event.topic.ITopicAnnotate;
import com.baiyi.cratos.domain.message.IEventMessage;
import com.baiyi.cratos.event.Event;

/**
 * @Author baiyi
 * @Date 2024/3/20 15:56
 * @Version 1.0
 */
public interface IEventConsumer<T extends IEventMessage> extends ITopicAnnotate {

    void onMessage(Event<T> event);

}