package com.baiyi.cratos.ssh.core.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/8 14:24
 * &#064;Version 1.0
 */
public class SshCrystalMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Open extends SshMessage.BaseMessage {
        private Integer assetId;
        private String instanceId;
        private String instanceName;
        private String serverAccount;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class OpenFromDuplicate extends SshMessage.BaseMessage {
        private String instanceId;
        private String instanceName;
        private String serverAccount;
        private String fromInstanceId;
    }

}
