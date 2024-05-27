package com.baiyi.cratos.ssh.core.facade.impl;

import com.baiyi.cratos.common.util.IOUtil;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.service.SshSessionService;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    public void closeSshSessionInstance(SshSessionInstance sshSessionInstance) {
        //  TODO serverCommandAudit.recordCommand(terminalSessionInstance);
        sshSessionInstance.setEndTime(new Date());
        sshSessionInstance.setInstanceClosed(true);
        sshSessionInstance.setOutputSize(IOUtil.fileSize(sshSessionInstance.getAuditPath()));
        sshSessionInstanceService.updateByPrimaryKey(sshSessionInstance);
    }

}
