package com.baiyi.cratos.event.factory;

import com.baiyi.cratos.event.consumer.IEventConsumer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/3/20 15:56
 * @Version 1.0
 */
public class EventConsumerFactory {

    private EventConsumerFactory() {
    }

    private final static ConcurrentHashMap<String, IEventConsumer<?>> CONTEXT = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> IEventConsumer<T> getConsumer(String eventType) {
        return (IEventConsumer<T>) CONTEXT.get(eventType);
    }

    public static <T> void register(IEventConsumer<T> bean) {
        CONTEXT.put(bean.getEventType(), bean);
    }

}
