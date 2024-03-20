package com.baiyi.cratos.event;

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
public class IEvent<T> extends ApplicationEvent implements IEventType {

    @Serial
    private static final long serialVersionUID = -6293229481889657060L;

    private String eventType;

    /**
     * 接受信息
     */
    private final T message;

    public IEvent(T message, String eventType) {
        super(message);
        this.message = message;
        this.eventType = eventType;
        log.debug("ApplicationEvent: {}", message);
    }

}