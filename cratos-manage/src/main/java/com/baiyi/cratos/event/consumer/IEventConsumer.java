package com.baiyi.cratos.event.consumer;

import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.IEventType;

/**
 * @Author baiyi
 * @Date 2024/3/20 15:56
 * @Version 1.0
 */
public interface IEventConsumer<T> extends IEventType {

    void onMessage(Event<T> event);

}