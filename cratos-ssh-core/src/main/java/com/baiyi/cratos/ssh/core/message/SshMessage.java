package com.baiyi.cratos.ssh.core.message;

import com.baiyi.cratos.domain.param.HasTerminalSize;
import com.baiyi.cratos.domain.ssh.HasState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 10:22
 * &#064;Version 1.0
 */
public class SshMessage {

    @Data
    @NoArgsConstructor
    public static class Terminal {
        private Integer width;
        private Integer height;
    }

    public static final BaseMessage UNKNOWN_MESSAGE = BaseMessage.builder().build();

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties
    public static class BaseMessage implements HasState, HasTerminalSize {

        public static final BaseMessage CLOSE = BaseMessage.builder()
                .build();

        private String id;
        private String state;
        private Terminal terminal;

        @Override
        public Integer getWidth() {
            return this.terminal.getWidth();
        }

        @Override
        public Integer getHeight() {
            return this.terminal.getHeight();
        }

        @Override
        public Integer getCols() {
            return null;
        }

        @Override
        public Integer getRows() {
            return null;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class BatchCommand extends BaseMessage {
        private Boolean isBatch; // 会话批量指令
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Command extends BaseMessage {
        private String instanceId;
        private String data;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class DuplicateSession extends BaseMessage {
        //        // 源会话
//        private ServerNode duplicateServerNode;
//        // 目标会话
//        private ServerNode serverNode;
        private String token;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Login extends BaseMessage {
        // private Set<ServerNode> serverNodes;
        // private String token;
        private Integer serverAccountId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Logout extends BaseMessage {
        private String instanceId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Resize extends BaseMessage {
        private String instanceId;
    }

}
