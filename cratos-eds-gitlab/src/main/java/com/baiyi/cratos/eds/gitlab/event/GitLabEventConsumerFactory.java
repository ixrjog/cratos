package com.baiyi.cratos.eds.gitlab.event;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.gitlab.GitLabEventParam;
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
            log.debug("GitLabEventConsumeFactory Registered: eventName={}, beanName={}", eventName, bean.getClass()
                    .getSimpleName());
        }
    }

    public static void consumeEventV4(String eventName, EdsInstance instance, GitLabEventParam.SystemHook systemHook) {
        if (CONTEXT.containsKey(eventName)) {
            CONTEXT.get(eventName)
                    .consumeEventV4(instance, systemHook);
        }
    }

}
