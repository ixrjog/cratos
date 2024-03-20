package com.baiyi.cratos.event.listener;

import com.baiyi.cratos.event.factory.EventConsumerFactory;
import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.consumer.IEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/20 15:41
 * @Version 1.0
 */
@Slf4j
@Component
public class EventListener<T> implements ApplicationListener<Event<T>> {

    @Override
    @Async
    public void onApplicationEvent(Event<T> event) {
        IEventConsumer<T> consumer = EventConsumerFactory.getConsumer(event.getEventType());
        if (consumer != null) {
            log.debug("On application event: eventType={}", event.getEventType());
            consumer.onMessage(event);
        } else {
            log.debug("On application event, not consumer: eventType={}", event.getEventType());
        }
    }

}