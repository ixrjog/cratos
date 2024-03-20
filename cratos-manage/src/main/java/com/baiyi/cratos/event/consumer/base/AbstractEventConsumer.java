package com.baiyi.cratos.event.consumer.base;

import com.baiyi.cratos.event.factory.EventConsumerFactory;
import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.consumer.IEventConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author baiyi
 * @Date 2024/3/20 16:01
 * @Version 1.0
 */
public abstract class AbstractEventConsumer<T> implements IEventConsumer<T>, InitializingBean {

    @Override
    @Async
    public void onMessage(Event<T> noticeEvent) {

    }


//    protected T toEventData(IEvent<T> event) {
//        return event.getBody();
//    }

    @Override
    public void afterPropertiesSet() {
        EventConsumerFactory.register(this);
    }
}
