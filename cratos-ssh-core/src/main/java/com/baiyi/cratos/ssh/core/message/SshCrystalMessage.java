package com.baiyi.cratos.ssh.core.message;

import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/8 14:24
 * &#064;Version 1.0
 */
public class SshCrystalMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class SuperOpen extends SshMessage.BaseMessage {
        @NotNull
        private Integer assetId;
        private String instanceId;
        private String instanceName;
        @NotBlank
        private String serverAccount;
    }

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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Resize extends SshMessage.BaseMessage {
        private String instanceId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Command extends SshMessage.BaseMessage {
        private String instanceId;
        private String input;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class SetBatchFlag extends SshMessage.BaseMessage {
        // 会话批量指令
        private Boolean isBatch;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Close extends SshMessage.BaseMessage {
        private String instanceId;
    }

    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @Data
    @JsonIgnoreProperties
    public static class CloseAll extends SshMessage.BaseMessage {
        public static final CloseAll CLOSE_ALL = CloseAll.builder()
                .state(MessageState.CLOSE_ALL.name())
                .build();
    }

}
