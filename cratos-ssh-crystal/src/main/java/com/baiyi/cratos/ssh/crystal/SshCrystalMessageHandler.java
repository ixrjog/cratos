package com.baiyi.cratos.ssh.crystal;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import jakarta.websocket.Session;
import org.springframework.aop.support.AopUtils;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/24 17:29
 * &#064;Version 1.0
 */
public interface SshCrystalMessageHandler {

    String NO_MESSAGE = "";

    void handle(String username, String message, Session session, SshSession sshSession);

    /**
     * WorkOrderKey
     *
     * @return
     */
    default String getState() {
        return Objects.requireNonNull(AopUtils.getTargetClass(this)
                        .getAnnotation(MessageStates.class))
                .state()
                .name();
    }

}
