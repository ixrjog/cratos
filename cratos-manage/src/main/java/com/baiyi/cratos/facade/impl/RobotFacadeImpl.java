package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.facade.RobotFacade;
import com.baiyi.cratos.service.RobotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RobotFacadeImpl implements RobotFacade {

    private final RobotService robotService;

    @Override
    public Robot verifyToken(String token) {
        Robot uniqueKey = Robot.builder()
                .token(token)
                .build();
        Robot robot = robotService.getByUniqueKey(uniqueKey);
        if (robot == null || !robot.getValid()) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }

        if (ExpiredUtil.isExpired(robot.getExpiredTime())) {
            revokeToken(robot);
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_EXPIRED);
        }
        return robot;
    }

    private void revokeToken(Robot robot) {
        robot.setValid(false);
        robotService.updateByPrimaryKey(robot);
    }

    @Override
    public boolean verifyResourceAuthorizedToToken(String token, String resource) {
        return  robotService.countResourcesAuthorizedByToken(token, resource) > 0;
    }

}
