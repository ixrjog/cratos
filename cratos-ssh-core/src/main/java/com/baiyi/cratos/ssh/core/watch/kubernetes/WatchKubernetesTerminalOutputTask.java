package com.baiyi.cratos.ssh.core.watch.kubernetes;

import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.SessionOutputUtils;
import com.baiyi.cratos.ssh.core.watch.base.AbstractSshChannelOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/17 16:13
 * &#064;Version 1.0
 */
@Slf4j
public class WatchKubernetesTerminalOutputTask extends AbstractSshChannelOutputTask {

    public static WatchKubernetesTerminalOutputTask newTask(SessionOutput sessionOutput, ByteArrayOutputStream baos,String auditPath) {
        return new WatchKubernetesTerminalOutputTask(sessionOutput, baos , auditPath);
    }

    public WatchKubernetesTerminalOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream baos,String auditPath) {
        setSessionOutput(sessionOutput);
        setOutputStream(baos);
        setAuditPath(auditPath);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        SessionOutputUtils.addToOutput(getSessionOutput().getSessionId(), getSessionOutput().getInstanceId(), buf, off,
                len);
    }

}