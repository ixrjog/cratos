package com.baiyi.cratos.eds.gitlab.event;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
@Slf4j
public class GitLabEventConsumerFactory {

    private static final Map<String, GitLabEventConsumer> CONTEXT = new ConcurrentHashMap<>();

    public static GitLabEventConsumer getByEventName(String eventName) {
        return CONTEXT.get(eventName);
    }

    public static void register(GitLabEventConsumer bean) {
        for (String eventName : bean.getEventNames()) {
            CONTEXT.put(eventName, bean);
            log.debug("GitLabEventConsumeFactory Registered: eventName={}, beanName={}", eventName, bean.getClass().getSimpleName());
        }
    }

}
