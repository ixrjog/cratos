package com.baiyi.cratos.ssh.core.watch.ssh;

import com.baiyi.cratos.common.util.ArrayUtil;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.watch.base.AbstractSshChannelOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Kubernetes Watch Log
 * &#064;Author  baiyi
 * &#064;Date  2024/6/3 下午3:56
 * &#064;Version 1.0
 */
@Slf4j
public class WatchKubernetesLogOutputTask extends AbstractSshChannelOutputTask {

    private final PrintWriter printWriter;

    public WatchKubernetesLogOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream byteArrayOutputStream,
                                        String auditPath, PrintWriter printWriter) {
        setSessionOutput(sessionOutput);
        setOutputStream(byteArrayOutputStream);
        setAuditPath(auditPath);
        this.printWriter = printWriter;
    }

    @Override
    public void write(char[] buff, int off, int len) throws IOException {
        char[] outBuff = ArrayUtil.sub(buff, 0, len);
        this.printWriter.write(outBuff);
    }

}

