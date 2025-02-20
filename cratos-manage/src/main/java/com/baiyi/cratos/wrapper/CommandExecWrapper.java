package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.CommandParser;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.model.CommandExecModel;
import com.baiyi.cratos.service.CommandExecApprovalService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/18 15:15
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecWrapper extends BaseDataTableConverter<CommandExecVO.CommandExec, CommandExec> implements IBaseWrapper<CommandExecVO.CommandExec> {

    private static final String MASK = "*********";
    private final CommandExecApprovalService approvalService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.ENV})
    public void wrap(CommandExecVO.CommandExec vo) {
        String sessionUsername = SessionUtils.getUsername();
        boolean isMask = isMask(sessionUsername, vo);
        vo.setMask(isMask);
        vo.setCommandMask(getCommandMask(vo, isMask));
        vo.setCommand("");
        maskOutputMessages(vo, isMask);
        vo.setApplicantInfo(getApplicantInfo(vo.getId(), vo.getUsername(), vo.getCompleted()));
        vo.setApprovalInfo(getApprovalInfo(sessionUsername, vo));
        CommandExecModel.ExecTarget execTarget = CommandExecModel.loadAs(vo);
        vo.setExecTarget(execTarget.toVO());
        vo.setApprovals(getApprovals(vo.getId()));
    }

    private Map<String, CommandExecVO.Approval> getApprovals(int commandExecId) {
        return approvalService.queryApprovals(commandExecId)
                .stream()
                .map(e -> BeanCopierUtil.copyProperties(e, CommandExecVO.Approval.class))
                .collect(Collectors.toMap(CommandExecVO.Approval::getApprovalType, approval -> approval));
    }

    private void maskOutputMessages(CommandExecVO.CommandExec vo, boolean isMask) {
        if (isMask) {
            if (StringUtils.hasText(vo.getOutMsg())) {
                vo.setOutMsg(MASK);
            }
            if (StringUtils.hasText(vo.getErrorMsg())) {
                vo.setErrorMsg(MASK);
            }
        }
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
        return CommandExecVO.ApplicantInfo.builder()
                .execCommand(!approvalService.hasUnfinishedApprovals(commandExecId))
                .build();
    }

    private boolean getApprovalInfoApprovalRequired(String username, CommandExecVO.CommandExec vo) {
        if (Boolean.TRUE.equals(vo.getCompleted())) {
            return false;
        }
        return Objects.nonNull(approvalService.queryUnapprovedRecord(vo.getId(), username));
    }

    private String getCommandMask(CommandExecVO.CommandExec vo, boolean isMask) {
        if (isMask) {
            StringBuilder maskedCommand = new StringBuilder();
            List<String> commands = CommandParser.parseCommand(vo.getCommand());
            IntStream.range(0, commands.size())
                    .forEachOrdered(i -> {
                        if (i > 0) {
                            maskedCommand.append("\n " + MASK);
                        } else {
                            maskedCommand.append(commands.get(i));
                        }
                    });
            return maskedCommand.toString();
        } else {
            return vo.getCommand();
        }
    }

    private boolean isMask(String username, CommandExecVO.CommandExec vo) {
        if (vo.getUsername()
                .equals(username)) {
            return false;
        }
        if (vo.getApprovedBy()
                .equals(username)) {
            return false;
        }
        return !StringUtils.hasText(vo.getCcTo()) || !vo.getCcTo()
                .equals(username);
    }
}
