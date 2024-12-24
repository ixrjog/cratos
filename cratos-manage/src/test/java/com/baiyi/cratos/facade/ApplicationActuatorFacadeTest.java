package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.application.ApplicationActuatorFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 17:27
 * &#064;Version 1.0
 */
public class ApplicationActuatorFacadeTest extends BaseUnit {

    @Resource
    private ApplicationActuatorFacade applicationActuatorFacade;

    @Test
    void test() {
        applicationActuatorFacade.scanAll();
    }

}
