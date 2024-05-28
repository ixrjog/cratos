package com.baiyi.cratos.ssh.core.watch.ssh;

import com.baiyi.cratos.common.util.ArrayUtil;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.watch.base.AbstractSshChannelOutputTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 上午11:00
 * &#064;Version 1.0
 */
@Slf4j
public class WatchKubernetesExecShellOutputTask extends AbstractSshChannelOutputTask {

    private final OutputStream channelOutput;

    public WatchKubernetesExecShellOutputTask(SessionOutput sessionOutput, ByteArrayOutputStream byteArrayOutputStream,String auditPath, OutputStream channelOutput) {
        setSessionOutput(sessionOutput);
        setOutputStream(byteArrayOutputStream);
        setAuditPath(auditPath);
        this.channelOutput = channelOutput;
    }

    @Override
    public void write(char[] buff, int off, int len) throws IOException {
        char[] outBuff = ArrayUtil.sub(buff, 0, len);
        this.channelOutput.write(toBytes(outBuff));
    }

}
