package com.baiyi.cratos.event.factory;

import com.baiyi.cratos.domain.message.IEventMessage;
import com.baiyi.cratos.event.consumer.IEventConsumer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/20 15:56
 * &#064;Version  1.0
 */
public class EventConsumerFactory {

    private EventConsumerFactory() {
    }

    private final static ConcurrentHashMap<String, IEventConsumer<?>> CONTEXT = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends IEventMessage> IEventConsumer<T> getConsumer(String topic) {
        return (IEventConsumer<T>) CONTEXT.get(topic);
    }

    public static <T extends IEventMessage> void register(IEventConsumer<T> bean) {
        CONTEXT.put(bean.getTopic(), bean);
    }

}
