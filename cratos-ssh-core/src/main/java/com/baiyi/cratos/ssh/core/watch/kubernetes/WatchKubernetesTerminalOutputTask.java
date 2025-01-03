package com.baiyi.cratos.ssh.core.watch.kubernetes;

import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.SessionOutputUtil;
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

    public WatchKubernetesTerminalOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream baos) {
        setSessionOutput(sessionOutput);
        setOutputStream(baos);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        SessionOutputUtil.addToOutput(getSessionOutput().getSessionId(), getSessionOutput().getInstanceId(), buf, off, len);
    }

}