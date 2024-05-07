package com.baiyi.cratos.event;

import com.baiyi.cratos.common.event.topic.ITopic;
import com.baiyi.cratos.domain.message.IEventMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/3/20 15:42
 * @Version 1.0
 */
@Slf4j
@Getter
@Setter
public class Event<T extends IEventMessage> extends ApplicationEvent implements ITopic {

    @Serial
    private static final long serialVersionUID = -6293229481889657060L;

    private String topic;

    /**
     * 消息体
     */
    private final T message;

    public Event(T message, String topic) {
        super(message);
        this.message = message;
        this.topic = topic;
    }

}