package com.baiyi.cratos.event.listener;

import com.baiyi.cratos.domain.message.IEventMessage;
import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.consumer.IEventConsumer;
import com.baiyi.cratos.event.factory.EventConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
        List<IEventConsumer<T>> consumers = EventConsumerFactory.getConsumers(event.getTopic());
        if (CollectionUtils.isEmpty(consumers)) {
            log.debug("On application event, not consumer: topic={}", event.getTopic());
        } else {
            consumers.forEach(consumer -> consumer.onMessage(event));
        }
    }

}