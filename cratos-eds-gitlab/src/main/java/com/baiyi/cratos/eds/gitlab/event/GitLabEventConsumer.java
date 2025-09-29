package com.baiyi.cratos.eds.gitlab.event;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.event.GitLabEventParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/6 10:37 上午
 * @Version 1.0
 */
public interface GitLabEventConsumer {

    /**
     * 事件名称
     * @return
     */
    List<String> getEventNames();

    /**
     * 消费事件
     *
     * @param instance
     * @param systemHook
     */
    void consumeEventV4(EdsInstance instance, GitLabEventParam.SystemHook systemHook);

}
