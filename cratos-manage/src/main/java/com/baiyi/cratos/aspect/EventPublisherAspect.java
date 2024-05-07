package com.baiyi.cratos.aspect;

import com.baiyi.cratos.common.annotation.EventPublisher;
import com.baiyi.cratos.domain.message.IEventMessage;
import com.baiyi.cratos.event.Event;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:23
 * &#064;Version 1.0
 */
@Aspect
@Component
@RequiredArgsConstructor
public class EventPublisherAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Pointcut(value = "@annotation(com.baiyi.cratos.common.annotation.EventPublisher)")
    public void annotationPoint() {
    }

    @After("@annotation(eventPublisher)")
    public void afterAdvice(JoinPoint joinPoint, EventPublisher eventPublisher) {
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args)
                .filter(arg -> arg instanceof IEventMessage)
                .map(arg -> (IEventMessage) arg)
                .map(eventMessage -> new Event<>(eventMessage, eventPublisher.topic()))
                .forEach(applicationEventPublisher::publishEvent);
    }

}
