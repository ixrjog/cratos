package com.baiyi.cratos.wrapper.command;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.RegexSensitiveDataMasker;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.model.CommandExecModel;
import com.baiyi.cratos.service.CommandExecApprovalService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.command.util.CommandExecUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 15:15
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecWrapper extends BaseDataTableConverter<CommandExecVO.CommandExec, CommandExec> implements IBaseWrapper<CommandExecVO.CommandExec> {

    private final CommandExecApprovalService approvalService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.ENV})
    public void wrap(CommandExecVO.CommandExec vo) {
        String sessionUsername = SessionUtils.getUsername();
        boolean isMask = CommandExecUtils.isMask(sessionUsername, vo);
        vo.setMask(isMask);
        // 命令脱敏
        // vo.setCommandMask(CommandExecUtils.getCommandMask(vo, isMask));
        vo.setCommandMask(isMask ? RegexSensitiveDataMasker.maskSensitiveData(vo.getCommand()) : vo.getCommand());
        vo.setCommand("");
        CommandExecUtils.maskOutputMessages(vo, isMask);
        vo.setApplicantInfo(getApplicantInfo(vo.getId(), vo.getUsername(), vo.getCompleted()));
        vo.setApprovalInfo(getApprovalInfo(sessionUsername, vo));
        CommandExecModel.ExecTarget execTarget = CommandExecModel.loadAs(vo);
        vo.setExecTarget(execTarget.toVO());
        vo.setApprovals(getApprovals(vo.getId()));
    }

    private Map<String, CommandExecVO.Approval> getApprovals(int commandExecId) {
        return approvalService.queryApprovals(commandExecId)
                .stream()
                .map(e -> BeanCopierUtils.copyProperties(e, CommandExecVO.Approval.class))
                .collect(Collectors.toMap(CommandExecVO.Approval::getApprovalType, approval -> approval));
    }

    private CommandExecVO.ApprovalInfo getApprovalInfo(String sessionUsername, CommandExecVO.CommandExec vo) {
        if (!vo.getApprovedBy()
                .equals(sessionUsername)) {
            return CommandExecVO.ApprovalInfo.NOT_THE_CURRENT_APPROVER;
        } else {
            return CommandExecVO.ApprovalInfo.builder()
                    .approvalRequired(getApprovalInfoApprovalRequired(sessionUsername, vo))
                    .build();
        }
    }

    public CommandExecVO.ApplicantInfo getApplicantInfo(int commandExecId, String username, boolean completed) {
        String sessionUsername = SessionUtils.getUsername();
        if (!username.equals(sessionUsername)) {
            return CommandExecVO.ApplicantInfo.NOT_THE_APPLICANT;
        }
        if (completed) {
            return CommandExecVO.ApplicantInfo.builder()
                    .execCommand(false)
                    .build();
        }
        // 审批完成 并且 全部同意
        boolean exec = approvalService.approvalCompletedAndApproved(commandExecId);
        return CommandExecVO.ApplicantInfo.builder()
                .execCommand(exec)
                .build();
    }

    private boolean getApprovalInfoApprovalRequired(String username, CommandExecVO.CommandExec vo) {
        if (Boolean.TRUE.equals(vo.getCompleted())) {
            return false;
        }
        return Objects.nonNull(approvalService.queryUnapprovedRecord(vo.getId(), username));
    }

}
