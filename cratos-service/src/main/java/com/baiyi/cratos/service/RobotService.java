package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.mapper.RobotMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:08
 * &#064;Version 1.0
 */
public interface RobotService  extends BaseUniqueKeyService<Robot, RobotMapper>, BaseValidService<Robot, RobotMapper> {

    int countResourcesAuthorizedByToken(String token, String resource);

}
