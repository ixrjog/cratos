package com.baiyi.cratos.ssh.core.watch;

import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.SessionOutputUtils;
import com.baiyi.cratos.ssh.core.watch.base.AbstractOutputTask;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/13 14:53
 * &#064;Version 1.0
 */
@Slf4j
public class WatchServerTerminalOutputTask extends AbstractOutputTask {

    public WatchServerTerminalOutputTask(SessionOutput sessionOutput, InputStream outFromChannel, String auditPath) {
        super(sessionOutput, outFromChannel, auditPath);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        SessionOutputUtils.addToOutput(getSessionOutput().getSessionId(), getSessionOutput().getInstanceId(), buf, off,
                len);
    }

}