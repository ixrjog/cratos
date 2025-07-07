package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.annotation.PreVerifyPermissionsFromParam;
import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.exception.RobotException;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.robot.RobotParam;
import com.baiyi.cratos.domain.view.robot.RobotVO;
import com.baiyi.cratos.facade.RobotFacade;
import com.baiyi.cratos.service.RobotService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.RobotTokenWrapper;
import com.baiyi.cratos.wrapper.RobotWrapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final RobotWrapper robotWrapper;
    private final RobotTokenWrapper robotTokenWrapper;
    private final UserService userService;

    @Override
    public DataTable<RobotVO.Robot> queryRobotPage(RobotParam.RobotPageQuery pageQuery) {
        DataTable<Robot> table = robotService.queryRobotPage(pageQuery);
        return robotWrapper.wrapToTarget(table);
    }

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
        return robotService.countResourcesAuthorizedByToken(token, resource) > 0;
    }

    @Override
    public List<RobotVO.Robot> queryRobotByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return List.of();
        }
        return robotService.queryByUsername(username)
                .stream()
                .map(robotWrapper::wrapToTarget)
                .toList();
    }

    /**
     * 管理员新增
     *
     * @param addRobot
     */
    @Override
    @SetSessionUserToParam(desc = "set CreatedBy")
    @PreVerifyPermissionsFromParam(ofUsername = "#addRobot.createdBy", accessLevel = AccessLevel.OPS)
    public RobotVO.RobotToken addRobot(RobotParam.AddRobot addRobot) {
        Robot robot = addRobot.toTarget();
        User user = userService.getByUsername(robot.getUsername());
        if (user == null) {
            throw new RobotException("The user does not exist, please check the username {}.", robot.getUsername());
        }
        if (!user.getValid()) {
            throw new RobotException("Robots cannot be attached to invalid user {}.", robot.getUsername());
        }
        robot.setToken(IdentityUtil.randomUUID());
        robot.setValid(true);
        if (robot.getExpiredTime() == null) {
            robot.setExpiredTime(
                    new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(366L * 3, TimeUnit.DAYS)));
        }
        robotService.add(robot);
        return robotTokenWrapper.wrapToTarget(robot);
    }

    /**
     * 用户申请
     *
     * @param applyRobot
     * @return
     */
    @Override
    @SetSessionUserToParam(desc = "set CreatedBy")
    public RobotVO.RobotToken applyRobot(RobotParam.ApplyRobot applyRobot) {
        Robot robot = applyRobot.toTarget();
        robot.setToken(IdentityUtil.randomUUID());
        robot.setValid(true);
        robot.setTrail(true);
        // 1年后过期
        robot.setExpiredTime(new Date(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(366L, TimeUnit.DAYS)));
        robotService.add(robot);
        return robotTokenWrapper.wrapToTarget(robot);
    }

    @Override
    @SetSessionUserToParam(desc = "set OperatingBy")
    @PreVerifyPermissionsFromParam(ofUsername = "#revokeRobot.operatingBy", accessLevel = AccessLevel.OPS)
    public void revokeRobot(RobotParam.RevokeRobot revokeRobot) {
        Robot robot = robotService.getById(revokeRobot.getId());
        if (!robot.getValid()) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String sessionUsername = authentication.getName();
        if (!StringUtils.hasText(sessionUsername)) {
            throw new RobotException("The current session user is invalid.");
        }
        robot.setValid(false);
        String comment = StringUtils.hasText(robot.getComment()) ? robot.getComment() : null;
        robot.setComment(Joiner.on("|")
                .skipNulls()
                .join(comment, StringFormatter.format("User {} revokes robot token.", sessionUsername)));
        robotService.updateByPrimaryKey(robot);
    }

}
