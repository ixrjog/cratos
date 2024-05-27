package com.baiyi.cratos.ssh.core.facade;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.generator.SshSessionInstance;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午1:54
 * &#064;Version 1.0
 */
public interface SshSessionFacade {

    // session
    void addSshSession(SshSession sshSession);

    void updateSshSession(SshSession sshSession);

    SshSession getBySessionId(String sessionId);

    // instance
    void addSshSessionInstance(SshSessionInstance sshSessionInstance);

    void updateSshSessionInstance(SshSessionInstance sshSessionInstance);

    void closeSshSessionInstance(SshSessionInstance sshSessionInstance);

}
