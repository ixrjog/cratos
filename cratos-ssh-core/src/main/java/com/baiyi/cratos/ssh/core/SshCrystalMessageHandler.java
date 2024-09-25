package com.baiyi.cratos.ssh.core;

import com.baiyi.cratos.domain.generator.SshSession;
import jakarta.websocket.Session;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/24 17:29
 * &#064;Version 1.0
 */
public interface SshCrystalMessageHandler {

    String NO_MESSAGE = "";

    void handle(String message, Session session, SshSession sshSession);

    String getState();

}
