package com.baiyi.cratos.ssh.core.message;

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
        // 0 普通账户 1 管理员账户
        private Integer loginType;
        private boolean isAdmin;
        private Terminal terminal;

        @Override
        public int getWidth() {
            return this.terminal.getWidth();
        }

        @Override
        public int getHeight() {
            return this.terminal.getHeight();
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
