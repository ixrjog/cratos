package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.CommandParser;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.ENV})
    public void wrap(CommandExecVO.CommandExec vo) {
        boolean isMask = isMask(vo);
        vo.setCommandMask(getCommandMask(vo, isMask));
        if (isMask) {
            if (StringUtils.hasText(vo.getOutMsg())) {
                vo.setOutMsg(MASK);
            }
            if (StringUtils.hasText(vo.getErrorMsg())) {
                vo.setErrorMsg(MASK);
            }
        }
    }

    private String getCommandMask(CommandExecVO.CommandExec vo, boolean isMask) {
        if (isMask) {
            StringBuilder maskedCommand = new StringBuilder();
            List<String> commands = CommandParser.parseCommand(vo.getCommand());
            for (int i = 0; i < commands.size(); i++) {
                if (i > 0) {
                    maskedCommand.append("\n " + MASK);
                } else {
                    maskedCommand.append(commands.get(i));
                }
            }
            return maskedCommand.toString();
        } else {
            return vo.getCommand();
        }
    }

    private boolean isMask(CommandExecVO.CommandExec vo) {
        String username = SessionUtils.getUsername();
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
