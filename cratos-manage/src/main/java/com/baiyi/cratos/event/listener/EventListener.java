package com.baiyi.cratos.event.listener;

import com.baiyi.cratos.domain.message.IEventMessage;
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
public class EventListener<T extends IEventMessage> implements ApplicationListener<Event<T>> {

    @Override
    @Async
    public void onApplicationEvent(Event<T> event) {
        IEventConsumer<T> consumer = EventConsumerFactory.getConsumer(event.getTopic());
        if (consumer != null) {
            log.debug("On application event: topic={}", event.getTopic());
            consumer.onMessage(event);
        } else {
            log.debug("On application event, not consumer: topic={}", event.getTopic());
        }
    }

}