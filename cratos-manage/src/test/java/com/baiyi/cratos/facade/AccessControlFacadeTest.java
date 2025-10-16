package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.service.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/16 15:45
 * &#064;Version 1.0
 */
public class AccessControlFacadeTest extends BaseUnit {

    @Resource
    private AccessControlFacade accessControlFacade;

    @Resource
    private ApplicationService applicationService;

    @Test
    void test() {
        Application application = applicationService.getByName("finance-switch-channel");
        SimpleBusiness hasBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(application.getId())
                .build();
        AccessControlVO.AccessControl accessControl = accessControlFacade.generateAccessControl("panyihong",
                hasBusiness, "prod");
        System.out.println(accessControl);
    }

}
