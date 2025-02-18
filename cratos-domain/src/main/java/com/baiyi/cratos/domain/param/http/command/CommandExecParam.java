package com.baiyi.cratos.domain.param.http.command;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:52
 * &#064;Version 1.0
 */
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
        @NotBlank
        private String applyRemark;
        @NotBlank
        private String approvedBy;
        @NotBlank
        private String command;
        private final Boolean completed = false;
        private final Boolean success = false;

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
        private Integer instanceId;
        private String namespace;
        private final boolean useDefaultExecContainer = true;
    }

    @Data
    @Schema
    public static class ApproveCommandExec implements HasSessionUser {
        @NotNull
        private Integer commandExecId;
        @Null
        private String username;
        @NotBlank
        private String approveRemark;
        @NotBlank
        @Schema(description = "with approvalStatus")
        private String approvalAction;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

}
