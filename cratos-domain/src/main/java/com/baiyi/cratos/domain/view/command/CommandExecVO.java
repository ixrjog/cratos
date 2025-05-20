package com.baiyi.cratos.domain.view.command;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 14:45
 * &#064;Version 1.0
 */
public class CommandExecVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class CommandExec extends BaseVO implements EnvVO.HasEnv, Serializable {
        @Serial
        private static final long serialVersionUID = -7247001798442829593L;
        private Integer id;
        private String username;
        private Boolean autoExec;
        private String approvedBy;
        private String ccTo;
        private Boolean completed;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date completedAt;
        private String namespace;
        private Boolean success;
        private String applyRemark;
        private String command;
        @Schema(description = "命令掩盖(私密)")
        private String commandMask;
        private String execTargetContent;
        private String outMsg;
        private String errorMsg;
        private EnvVO.Env env;
        private boolean isMask;
        @Schema(description = "申请人信息")
        private ApplicantInfo applicantInfo;
        @Schema(description = "审批人信息")
        private ApprovalInfo approvalInfo;
        private ExecTarget execTarget;
        private Map<String, Approval> approvals;
        @Override
        public String getEnvName() {
            return this.namespace;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ApplicantInfo implements Serializable {
        public static final ApplicantInfo NOT_THE_APPLICANT = ApplicantInfo.builder()
                .isApplicant(false)
                .execCommand(false)
                .build();
        @Serial
        private static final long serialVersionUID = -7584357457920941459L;
        @Schema(description = "是当前审批人")
        @Builder.Default
        private boolean isApplicant = true;
        @Schema(description = "执行命令")
        private boolean execCommand;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ApprovalInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = -2702560971256065616L;
        public static final ApprovalInfo NOT_THE_CURRENT_APPROVER = ApprovalInfo.builder()
                .isCurrentApprover(false)
                .approvalRequired(false)
                .build();
        @Schema(description = "是当前审批人")
        @Builder.Default
        private boolean isCurrentApprover = true;
        @Schema(description = "需要审批")
        private boolean approvalRequired;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecTarget {
        private EdsInstance instance;
        private Boolean useDefaultExecContainer;
        private Long maxWaitingTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EdsInstance {
        private String name;
        private Integer id;
        private String namespace;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Approval extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -7809805253468967134L;
        private Integer id;
        private Integer commandExecId;
        private String approvalType;
        private String username;
        private String approvalStatus;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date approvalAt;
        private Boolean approvalCompleted;
        private String approveRemark;
    }

}
