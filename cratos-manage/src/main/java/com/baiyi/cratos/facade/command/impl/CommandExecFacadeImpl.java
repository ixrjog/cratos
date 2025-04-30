package com.baiyi.cratos.facade.command.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.constants.SchedulerLockNameConstants;
import com.baiyi.cratos.common.enums.CommandExecApprovalStatusEnum;
import com.baiyi.cratos.common.enums.CommandExecApprovalTypeEnum;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.CommandExecException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.exec.KubernetesPodExec;
import com.baiyi.cratos.eds.kubernetes.exec.context.PodExecContext;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.facade.command.CommandExecFacade;
import com.baiyi.cratos.facade.command.CommandExecNoticeFacade;
import com.baiyi.cratos.model.CommandExecModel;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.command.CommandExecWrapper;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

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
    private final KubernetesPodExec kubernetesPodExec;
    private final KubernetesPodRepo kubernetesPodRepo;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final EdsAssetService edsAssetService;
    private final CommandExecNoticeFacade commandExecNoticeFacade;

    private static final long DEFAULT_MAX_WAITING_TIME = 10L;

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
                SysTagKeys.COMMAND_EXEC_APPROVER.getKey())) {
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
        String namespace = Optional.of(addCommandExec)
                .map(CommandExecParam.AddCommandExec::getExecTarget)
                .map(CommandExecParam.ExecTarget::getNamespace)
                .orElseThrow(() -> new CommandExecException("The namespace cannot be empty."));
        commandExec.setNamespace(namespace);
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
                .maxWaitingTime(Optional.of(addCommandExec)
                        .map(CommandExecParam.AddCommandExec::getExecTarget)
                        .map(CommandExecParam.ExecTarget::getMaxWaitingTime)
                        .orElse(DEFAULT_MAX_WAITING_TIME))
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
        // 通知
        commandExecNoticeFacade.sendApprovalNotice(approver);
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
        CommandExecApproval commandExecApproval = commandExecApprovalService.queryUnapprovedRecord(
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
            commandExecApproval.setApprovalAt(new Date());
            commandExecApprovalService.updateByPrimaryKey(commandExecApproval);
            if (commandExecApprovalService.approvalCompletedAndApproved(commandExec.getId())) {
                if (Boolean.TRUE.equals(commandExec.getAutoExec())) {
                    autoCommandExec(commandExec);
                }
            } else {
                commandExec.setCompleted(true);
                commandExec.setCompletedAt(new Date());
                commandExecService.updateByPrimaryKey(commandExec);
            }
            // 通知
            commandExecNoticeFacade.sendApprovalResultNotice(commandExecApproval);
        } catch (IllegalArgumentException ex) {
            CommandExecException.runtime("Incorrect approval action.");
        }
    }

    private void autoCommandExec(CommandExec commandExec) {
        List<CommandExecApproval> approvals = commandExecApprovalService.queryApprovals(commandExec.getId(),
                        CommandExecApprovalTypeEnum.APPROVER.name())
                .stream()
                .filter(e -> !(Boolean.TRUE.equals(
                        e.getApprovalCompleted()) && CommandExecApprovalStatusEnum.AGREE.name()
                        .equals(e.getApprovalStatus())))
                .toList();
        if (CollectionUtils.isEmpty(approvals)) {
            // 可以执行
            CommandExecModel.ExecTarget execTarget = CommandExecModel.loadAs(commandExec);
            doCommandExec(commandExec, execTarget.getMaxWaitingTime());
        }
    }

    @Override
    @SetSessionUserToParam
    @SchedulerLock(name = SchedulerLockNameConstants.DO_COMMAND_EXEC, lockAtMostFor = "10s", lockAtLeastFor = "10s")
    public void doCommandExec(CommandExecParam.DoCommandExec doCommandExec) {
        CommandExec commandExec = commandExecService.getById(doCommandExec.getCommandExecId());
        CommandExecVO.ApplicantInfo applicantInfo = commandExecWrapper.getApplicantInfo(commandExec.getId(),
                commandExec.getUsername(), commandExec.getCompleted());
        if (!applicantInfo.isExecCommand()) {
            CommandExecException.runtime("Cannot execute command.");
        }
        doCommandExec(commandExec, doCommandExec.getMaxWaitingTime());
    }

    @Override
    @SetSessionUserToParam
    @SchedulerLock(name = SchedulerLockNameConstants.DO_COMMAND_EXEC, lockAtMostFor = "10s", lockAtLeastFor = "10s")
    public void adminDoCommandExec(CommandExecParam.DoCommandExec doCommandExec) {
        CommandExec commandExec = commandExecService.getById(doCommandExec.getCommandExecId());
        if (Boolean.TRUE.equals(commandExec.getCompleted())) {
            CommandExecException.runtime("Cannot execute command, Task execution completed.");
        }
        doCommandExec(commandExec, doCommandExec.getMaxWaitingTime());
    }

    @SuppressWarnings("unchecked")
    private void doCommandExec(CommandExec commandExec, Long maxWaitingTime) {
        CommandExecModel.ExecTarget execTarget = CommandExecModel.loadAs(commandExec);
        String namespace = execTarget.getInstance()
                .getNamespace();
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, ?> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, ?>) holderBuilder.newHolder(
                execTarget.getInstance()
                        .getId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        PodExecContext execContext = PodExecContext.builder()
                .maxWaitingTime(maxWaitingTime)
                .command(commandExec.getCommand())
                .build();
        EdsKubernetesConfigModel.Kubernetes kubernetes = holder.getInstance()
                .getEdsConfigModel();
        // 查询当前实例下打标签的资产
        List<Integer> assetIds = businessTagFacade.queryByBusinessTypeAndTagKey(BusinessTypeEnum.EDS_ASSET.name(),
                SysTagKeys.COMMAND_EXEC.getKey());
        if (CollectionUtils.isEmpty(assetIds)) {
            CommandExecException.runtime("No available execution target.");
        }
        EdsAsset asset = getEdsAsset(execTarget, assetIds);
        List<Pod> pods = kubernetesPodRepo.list(kubernetes, namespace, asset.getName());
        if (CollectionUtils.isEmpty(pods)) {
            CommandExecException.runtime("No available execution pods.");
        }
        kubernetesPodExec.exec(kubernetes, namespace, pods.getFirst()
                .getMetadata()
                .getName(), execContext, new CountDownLatch(1));
        commandExec.setOutMsg(execContext.getOutMsg());
        commandExec.setErrorMsg(execContext.getErrorMsg());
        commandExec.setSuccess(execContext.getSuccess());
        commandExec.setCompleted(true);
        commandExec.setCompletedAt(new Date());
        commandExecService.updateByPrimaryKey(commandExec);
    }

    private EdsAsset getEdsAsset(CommandExecModel.ExecTarget execTarget, List<Integer> assetIds) {
        return assetIds.stream()
                .map(edsAssetService::getById)
                .filter(asset -> asset.getInstanceId()
                        .equals(execTarget.getInstance()
                                .getId()) && EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name()
                        .equals(asset.getAssetType()))
                .findFirst()
                .orElseThrow(() -> new CommandExecException("No available execution target."));
    }

}
