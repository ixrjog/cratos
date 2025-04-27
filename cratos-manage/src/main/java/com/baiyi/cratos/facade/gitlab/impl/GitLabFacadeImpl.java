package com.baiyi.cratos.facade.gitlab.impl;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.gitlab.GitLabEventParam;
import com.baiyi.cratos.eds.gitlab.event.GitLabEventConsumer;
import com.baiyi.cratos.eds.gitlab.event.GitLabEventConsumerFactory;
import com.baiyi.cratos.facade.gitlab.EdsGitLabInstanceManager;
import com.baiyi.cratos.facade.gitlab.GitLabFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 10:09
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class GitLabFacadeImpl implements GitLabFacade {

    private final EdsGitLabInstanceManager edsGitLabInstanceManager;

    public void consumeEvent(GitLabEventParam.SystemHook systemHook, String hookToken) {
        if (StringUtils.isAnyBlank(systemHook.getEvent_name(), hookToken)) {
            log.debug("Invalid event: missing event name or hook token.");
            return;
        }
        EdsInstance edsInstance = edsGitLabInstanceManager.findInstanceByHookToken(hookToken);
        if (Objects.isNull(edsInstance)) {
            log.debug("No matching instance found for the provided hook token.");
            return;
        }
        GitLabEventConsumer gitLabEventConsumer = GitLabEventConsumerFactory.getByEventName(systemHook.getEvent_name());
        if (Objects.nonNull(gitLabEventConsumer)) {
            gitLabEventConsumer.consumeEventV4(edsInstance, systemHook);
        } else {
            log.debug("No consumer found for event: {}", systemHook.getEvent_name());
        }
    }

}
