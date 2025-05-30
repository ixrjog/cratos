package com.baiyi.cratos.ssh.core.facade.impl;

import com.baiyi.cratos.common.util.IOUtils;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.service.session.SshSessionInstanceService;
import com.baiyi.cratos.service.session.SshSessionService;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午1:55
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SimpleSshSessionFacadeImpl implements SimpleSshSessionFacade {

    private final SshSessionService sshSessionService;
    private final SshSessionInstanceService sshSessionInstanceService;

    @Override
    public void addSshSession(SshSession sshSession) {
        sshSessionService.add(sshSession);
    }

    @Override
    public void updateSshSession(SshSession sshSession) {
        sshSessionService.updateByPrimaryKey(sshSession);
    }

    @Override
    public SshSession getBySessionId(String sessionId) {
        SshSession uniqueKey = SshSession.builder()
                .sessionId(sessionId)
                .build();
        return sshSessionService.getByUniqueKey(uniqueKey);
    }

    @Override
    public void addSshSessionInstance(SshSessionInstance sshSessionInstance) {
        sshSessionInstanceService.add(sshSessionInstance);
    }

    @Override
    public void updateSshSessionInstance(SshSessionInstance sshSessionInstance) {
        sshSessionInstanceService.updateByPrimaryKey(sshSessionInstance);
    }

    @Override
    public void closeSshSessionInstance(String sessionId, String instanceId) {
        SshSessionInstance sshSessionInstance = sshSessionInstanceService.getByInstanceId(instanceId);
        if (sshSessionInstance != null && sshSessionInstance.getSessionId()
                .equals(sessionId)) {
            this.closeSshSessionInstance(sshSessionInstance);
        }
    }

    @Override
    public void closeSshSessionInstance(SshSessionInstance sshSessionInstance) {
        sshSessionInstance.setEndTime(new Date());
        sshSessionInstance.setInstanceClosed(true);
        sshSessionInstance.setOutputSize(IOUtils.fileSize(sshSessionInstance.getAuditPath()));
        sshSessionInstanceService.updateByPrimaryKey(sshSessionInstance);
        setSessionToBeValid(sshSessionInstance.getSessionId());
    }

    private void setSessionToBeValid(String sessionId) {
        SshSession sshSession = sshSessionService.getBySessionId(sessionId);
        if (!sshSession.getValid()) {
            sshSession.setValid(true);
            sshSessionService.updateByPrimaryKey(sshSession);
        }
    }

    @Override
    public void closeSshSession(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            return;
        }
        SshSession sshSession = sshSessionService.getBySessionId(sessionId);
        if (Objects.isNull(sshSession)) {
            return;
        }
        sshSession.setEndTime(new Date());
        sshSession.setValid(sshSessionInstanceService.countBySessionId(sessionId) > 0);
        sshSessionService.updateByPrimaryKey(sshSession);
    }

}
