package com.baiyi.cratos.ssh.core;


import com.baiyi.cratos.common.util.IOUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author baiyi
 * @Date 2020/5/25 3:08 下午
 * @Version 1.0
 */
@Slf4j
public class SshRecorder {

    public static void record(String auditPath, char[] buf, int off, int len) {
        try {
            IOUtil.appendFile(new String(buf).substring(off, len), auditPath);
        } catch (Exception e) {
            log.error("Ssh session audit log write failed. {}", auditPath);
        }
    }

    /**
     * 用户命令操作审计日志，暂不使用
     *
     * @param commander
     * @param sessionId
     * @param instanceId
     */
    private static void recordCommanderLog(StringBuffer commander, String sessionId, String instanceId) {
        try {
            String log = new String(commander);
            log = log.replaceAll("(\n|\r\n)\\s+", "");
            while (log.contains("\b")) {
                // 退格处理
                log = log.replaceFirst(".\b", "");
            }
            // IOUtil.appendFile(log, sshAuditProperties.buildCommanderLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Ssh session audit log write failed. sessionId={}, instanceId={}", sessionId, instanceId);
        }
    }

}