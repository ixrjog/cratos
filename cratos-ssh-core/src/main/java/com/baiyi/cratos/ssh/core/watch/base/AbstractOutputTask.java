package com.baiyi.cratos.ssh.core.watch.base;

import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.ssh.core.SshRecorder;
import com.baiyi.cratos.ssh.core.model.SessionOutput;
import com.baiyi.cratos.ssh.core.util.SessionOutputUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2021/7/7 12:51 下午
 * @Version 1.0
 */
@Slf4j
@Data
public abstract class AbstractOutputTask implements IRecordOutputTask {

    private InputStream outFromChannel;

    private SessionOutput sessionOutput;

    private String auditPath;

    private static final int BUFF_SIZE = 1024 * 8;

    public AbstractOutputTask(SessionOutput sessionOutput, InputStream outFromChannel,String auditPath) {
        setSessionOutput(sessionOutput);
        setOutFromChannel(outFromChannel);
        setAuditPath(auditPath);
    }

    @Override
    public void run() {
        InputStreamReader isr = new InputStreamReader(outFromChannel, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr, BUFF_SIZE);
        try {
            SessionOutputUtil.addOutput(sessionOutput);
            char[] buff = new char[BUFF_SIZE];
            int read;
            while ((read = br.read(buff)) != -1) {
                char[] outBuff = com.baiyi.cratos.common.util.ArrayUtil.sub(buff, 0, read);
                writeAndRecord(outBuff, 0, outBuff.length);
                TimeUtil.millisecondsSleep(10L);
            }
        } catch (Exception ex) {
            log.debug(ex.getMessage(), ex);
        } finally {
            log.debug("Watch server output task ended: sessionId={}, instanceId={}", sessionOutput.getSessionId(),
                    sessionOutput.getInstanceId());
            SessionOutputUtil.removeOutput(sessionOutput.getSessionId(), sessionOutput.getInstanceId());
        }
    }

    protected byte[] toBytes(char[] chars) {
        return String.valueOf(chars)
                .getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void record(char[] buf, int off, int len) {
        SshRecorder.record(auditPath, buf, off, len);
    }

}