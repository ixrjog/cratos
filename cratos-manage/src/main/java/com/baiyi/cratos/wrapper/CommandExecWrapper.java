package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.CommandParser;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.service.CommandExecApprovalService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
        String username = SessionUtils.getUsername();
        boolean isMask = isMask(username, vo);
        vo.setCommandMask(getCommandMask(vo, isMask));
        vo.setCommand("");
        if (isMask) {
            if (StringUtils.hasText(vo.getOutMsg())) {
                vo.setOutMsg(MASK);
            }
            if (StringUtils.hasText(vo.getErrorMsg())) {
                vo.setErrorMsg(MASK);
            }
        }
        // 申请人信息
        if (!vo.getUsername()
                .equals(username)) {
            vo.setApplicantInfo(CommandExecVO.ApplicantInfo.NOT_THE_APPLICANT);
        } else {
            if (!vo.getCompleted() && !approvalService.hasUnfinishedApprovals(vo.getId())) {
                vo.setApplicantInfo(CommandExecVO.ApplicantInfo.builder()
                        .execCommand(true)
                        .build());
            }
        }
        // 审批人信息
        if (!vo.getApprovedBy()
                .equals(username)) {
            vo.setApprovalInfo(CommandExecVO.ApprovalInfo.NOT_THE_CURRENT_APPROVER);
        } else {
            vo.setApprovalInfo(CommandExecVO.ApprovalInfo.builder()
                    .approvalRequired(getApprovalInfoApprovalRequired(username, vo))
                    .build());
        }
    }

    private boolean getApplicantInfoExecCommand(String username, CommandExecVO.CommandExec vo) {
        if (vo.getCompleted()) {
            return false;
        }
        return !approvalService.hasUnfinishedApprovals(vo.getId());
    }

    private boolean getApprovalInfoApprovalRequired(String username, CommandExecVO.CommandExec vo) {
        if (Boolean.TRUE.equals(vo.getCompleted())) {
            return false;
        }
        return Objects.isNull(approvalService.queryUnapprovedRecord(vo.getId(), username));
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
