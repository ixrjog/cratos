package com.baiyi.cratos.event.factory;

import com.baiyi.cratos.domain.message.IEventMessage;
import com.baiyi.cratos.event.consumer.IEventConsumer;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/20 15:56
 * &#064;Version  1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EventConsumerFactory {

    private final static ConcurrentHashMap<String, List<IEventConsumer<? extends IEventMessage>>> CONTEXT = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends IEventMessage> List<IEventConsumer<T>> getConsumers(String topic) {
        List<IEventConsumer<?>> consumers = CONTEXT.get(topic);
        return consumers.stream()
                .map(e -> (IEventConsumer<T>) e)
                .toList();
    }

    public static <T extends IEventMessage> void register(IEventConsumer<T> bean) {
        if (CONTEXT.containsKey(bean.getTopic())) {
            CONTEXT.get(bean.getTopic())
                    .add(bean);
        } else {
            CONTEXT.put(bean.getTopic(), List.of(bean));
        }
    }

}
