package com.baiyi.cratos.wrapper.command.util;

import com.baiyi.cratos.common.util.CommandParser;
import com.baiyi.cratos.domain.view.command.CommandExecVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.IntStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/21 13:40
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandExecUtils {

    private static final String MASK = "*********";

    public static void maskOutputMessages(CommandExecVO.CommandExec vo, boolean isMask) {
        if (isMask) {
            if (StringUtils.hasText(vo.getOutMsg())) {
                vo.setOutMsg(MASK);
            }
            if (StringUtils.hasText(vo.getErrorMsg())) {
                vo.setErrorMsg(MASK);
            }
        }
    }

    public static String getCommandMask(CommandExecVO.CommandExec vo, boolean isMask) {
        if (isMask) {
            StringBuilder maskedCommand = new StringBuilder();
            List<String> commands = CommandParser.parseCommand(vo.getCommand());
            IntStream.range(0, commands.size())
                    .forEachOrdered(i -> {
                        if (i > 0) {
                            maskedCommand.append(" ")
                                    .append(CommandParser.maskString(commands.get(i)));
                        } else {
                            maskedCommand.append(commands.get(i));
                        }
                    });
            return maskedCommand.toString();
        } else {
            return vo.getCommand();
        }
    }

    public static boolean isMask(String username, CommandExecVO.CommandExec vo) {
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
