package com.baiyi.cratos.domain.param.http.command;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:52
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CommandExecParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CommandExecPageQuery extends PageParam {
        private String namespace;
        private Boolean completed;
        private String applyUsername;
        private String approvedBy;
        private Boolean success;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddCommandExec implements IToTarget<CommandExec>, HasSessionUser {
        @Null
        private Integer id;
        private String username;
        @NotNull
        private Boolean autoExec;
        private String ccTo;
        @NotBlank(message = "Apply remark cannot be empty")
        private String applyRemark;
        @NotBlank
        private String approvedBy;
        @NotBlank(message = "Command cannot be empty")
        private String command;
        private final Boolean completed = false;
        private final Boolean success = false;

        @Valid
        @NotNull
        private ExecTarget execTarget;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ExecTarget {
        @Schema(description = "Eds Kubernetes Instance ID")
        @NotNull
        private Integer instanceId;
        @NotBlank
        private String namespace;
        private final boolean useDefaultExecContainer = true;
        @Max(60L)
        private Long maxWaitingTime;
    }

    @Data
    @Schema
    public static class ApproveCommandExec implements HasSessionUser {
        @NotNull
        private Integer commandExecId;
        @Null
        private String username;
        @NotBlank(message = "Approval remark cannot be empty")
        private String approveRemark;
        @NotBlank
        @Schema(description = "with approvalStatus")
        private String approvalAction;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

    @Data
    @Schema
    public static class DoCommandExec implements HasSessionUser {
        @NotNull
        private Integer commandExecId;
        @Null
        private String username;
        @Max(60)
        @NotNull
        private Long maxWaitingTime;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

}
