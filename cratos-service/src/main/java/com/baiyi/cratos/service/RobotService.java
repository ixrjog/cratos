package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.param.robot.RobotParam;
import com.baiyi.cratos.mapper.RobotMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:08
 * &#064;Version 1.0
 */
public interface RobotService  extends BaseUniqueKeyService<Robot, RobotMapper>, BaseValidService<Robot, RobotMapper> {

    DataTable<Robot> queryRobotPage(RobotParam.RobotPageQuery pageQuery);

    int countResourcesAuthorizedByToken(String token, String resource);

    List<Robot> queryByUsername(String username);

}
