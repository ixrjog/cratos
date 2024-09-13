package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.generator.Robot;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/13 14:16
 * &#064;Version 1.0
 */
public interface RobotFacade {

    Robot verifyToken(String token);

    boolean verifyResourceAuthorizedToToken(String token, String resource);

}
