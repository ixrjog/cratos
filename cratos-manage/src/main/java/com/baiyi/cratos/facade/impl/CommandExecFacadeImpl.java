package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.enums.CommandExecApprovalStatusEnum;
import com.baiyi.cratos.common.enums.CommandExecApprovalTypeEnum;
import com.baiyi.cratos.common.exception.CommandExecException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.generator.CommandExecApproval;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.facade.CommandExecFacade;
import com.baiyi.cratos.model.CommandExecModel;
import com.baiyi.cratos.service.CommandExecApprovalService;
import com.baiyi.cratos.service.CommandExecService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.CommandExecWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:48
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecFacadeImpl implements CommandExecFacade {

    private final CommandExecService commandExecService;
    private final CommandExecApprovalService commandExecApprovalService;
    private final UserService userService;
    private final BusinessTagFacade businessTagFacade;
    private final EdsInstanceService edsInstanceService;
    private final CommandExecWrapper commandExecWrapper;
    private static final String TAG_COMMAND_EXEC_APPROVER = "CommandExecApprover";

    @Override
    public DataTable<CommandExecVO.CommandExec> queryCommandExecPage(CommandExecParam.CommandExecPageQuery pageQuery) {
        DataTable<CommandExec> table = commandExecService.queryCommandExecPage(pageQuery);
        return commandExecWrapper.wrapToTarget(table);
    }

    @Override
    @SetSessionUserToParam(desc = "Set ApplyUser to CommandExecParam")
    @Transactional(rollbackFor = CommandExecException.class)
    public void addCommandExec(CommandExecParam.AddCommandExec addCommandExec) {
        CommandExec commandExec = addCommandExec.toTarget();
        // 校验审批人
        User approverUser = userService.getByUsername(commandExec.getApprovedBy());
        if (Objects.isNull(approverUser)) {
            CommandExecException.runtime("The approver {} does not exist.", commandExec.getApprovedBy());
        }
        if (!businessTagFacade.containsTag(BusinessTypeEnum.USER.name(), approverUser.getId(),
                TAG_COMMAND_EXEC_APPROVER)) {
            CommandExecException.runtime("The designated approver does not have approval qualifications.");
        }
        // 校验Eds Kubernetes
        int instanceId = Optional.of(addCommandExec)
                .map(CommandExecParam.AddCommandExec::getExecTarget)
                .map(CommandExecParam.ExecTarget::getInstanceId)
                .orElse(0);
        if (!IdentityUtil.hasIdentity(instanceId)) {
            CommandExecException.runtime("Execution target error.");
        }
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        if (Objects.isNull(edsInstance) || !EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(edsInstance.getEdsType())) {
            CommandExecException.runtime("Eds kubernetes instance does not exist or the type is incorrect.");
        }
        commandExec.setNamespace(addCommandExec.getExecTarget()
                .getNamespace());
        CommandExecModel.EdsInstance instance = CommandExecModel.EdsInstance.builder()
                .id(edsInstance.getId())
                .name(edsInstance.getInstanceName())
                .namespace(addCommandExec.getExecTarget()
                        .getNamespace())
                .build();
        CommandExecModel.ExecTarget execTarget = CommandExecModel.ExecTarget.builder()
                .instance(instance)
                .useDefaultExecContainer(addCommandExec.getExecTarget()
                        .isUseDefaultExecContainer())
                .build();
        commandExec.setExecTargetContent(execTarget.dump());
        commandExecService.add(commandExec);
        createApproval(addCommandExec, commandExec);
    }

    /**
     * 创建审批人节点
     *
     * @param addCommandExec
     * @param commandExec
     */
    private void createApproval(CommandExecParam.AddCommandExec addCommandExec, CommandExec commandExec) {
        CommandExecApproval approver = CommandExecApproval.builder()
                .commandExecId(commandExec.getId())
                .approvalType(CommandExecApprovalTypeEnum.APPROVER.name())
                .approvalCompleted(false)
                .username(commandExec.getApprovedBy())
                .build();
        commandExecApprovalService.add(approver);
        if (StringUtils.hasText(addCommandExec.getCcTo())) {
            User ccUser = userService.getByUsername(commandExec.getCcTo());
            if (Objects.isNull(ccUser)) {
                CommandExecException.runtime("The cc to {} does not exist.", commandExec.getCcTo());
            }
            CommandExecApproval cc = CommandExecApproval.builder()
                    .commandExecId(commandExec.getId())
                    .approvalType(CommandExecApprovalTypeEnum.CC_TO.name())
                    .approvalCompleted(true)
                    .username(commandExec.getCcTo())
                    .build();
            commandExecApprovalService.add(cc);
        }
    }

    @Override
    @SetSessionUserToParam
    public void approveCommandExec(CommandExecParam.ApproveCommandExec approveCommandExec) {
        CommandExec commandExec = commandExecService.getById(approveCommandExec.getCommandExecId());
        if (Objects.isNull(commandExec)) {
            CommandExecException.runtime("The commandExec id={} does not exist.",
                    approveCommandExec.getCommandExecId());
        }
        if (Boolean.TRUE.equals(commandExec.getCompleted())) {
            CommandExecException.runtime("The commandExec is completed.", approveCommandExec.getCommandExecId());
        }
        CommandExecApproval commandExecApproval = commandExecApprovalService.queryUnapprovedRecode(
                approveCommandExec.getCommandExecId(), approveCommandExec.getUsername());
        if (Objects.isNull(commandExecApproval)) {
            CommandExecException.runtime("Without your approval item.");
        }
        if (Boolean.TRUE.equals(commandExecApproval.getApprovalCompleted())) {
            CommandExecException.runtime("Approval completed.");
        }
        try {
            CommandExecApprovalStatusEnum approvalStatus = CommandExecApprovalStatusEnum.valueOf(
                    approveCommandExec.getApprovalAction());
            commandExecApproval.setApprovalStatus(approvalStatus.name());
            commandExecApproval.setApproveRemark(approveCommandExec.getApproveRemark());
            commandExecApproval.setApprovalCompleted(true);
            commandExecApprovalService.updateByPrimaryKey(commandExecApproval);
        } catch (IllegalArgumentException ex) {
            CommandExecException.runtime("Incorrect approval action.");
        }
    }

}
