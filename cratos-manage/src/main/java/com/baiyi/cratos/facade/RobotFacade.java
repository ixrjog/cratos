package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.robot.RobotParam;
import com.baiyi.cratos.domain.view.robot.RobotVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:16
 * &#064;Version 1.0
 */
public interface RobotFacade {

    Robot verifyToken(String token);

    boolean verifyResourceAuthorizedToToken(String token, String resource);

    DataTable<RobotVO.Robot> queryRobotPage(RobotParam.RobotPageQuery pageQuery);

    List<RobotVO.Robot> queryRobotByUsername(String username);

    RobotVO.RobotToken addRobot(RobotParam.AddRobot addRobot);

    RobotVO.RobotToken applyRobot(RobotParam.ApplyRobot applyRobot);

    void revokeRobot(RobotParam.RevokeRobot revokeRobot);

}
