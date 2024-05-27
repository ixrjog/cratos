package com.baiyi.cratos.ssh.core.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/25 1:45 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ssh", ignoreInvalidFields = true)
public class SshAuditProperties {

    private Audit audit;

    @Data
    public static class Audit {

        private String path;
        private Boolean open;

    }

    public interface Suffix {
        String AUDIT_LOG = ".log";
        String COMMAND_LOG = "_commander.log";
        String FMT_COMMAND_LOG = "_commander_fmt.log";   // formatted
    }

    public String generateAuditLogFilePath(String sessionId, String instanceId) {
        return Joiner.on("/")
                .join(audit.getPath(), sessionId, instanceId + Suffix.AUDIT_LOG);
    }

    public String buildCommanderLogPath(String sessionId, String instanceId) {
        return Joiner.on("/")
                .join(audit.getPath(), sessionId, instanceId + Suffix.COMMAND_LOG);
    }

    public String buildFmtCommanderLogPath(String sessionId, String instanceId) {
        return Joiner.on("/")
                .join(audit.getPath(), sessionId, instanceId + Suffix.FMT_COMMAND_LOG);
    }

}