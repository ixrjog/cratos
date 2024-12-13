package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.service.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/11 14:44
 * &#064;Version 1.0
 */
public class UserPermissionFacadeTest extends BaseUnit {

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private ApplicationService applicationService;

    @Test
    void test1() {
        // kili
        Application application = applicationService.getByName("kili");
        UserPermissionParam.GrantUserPermission grantUserPermission1 = UserPermissionParam.GrantUserPermission.builder()
                .username("baiyi")
                .displayName("baiyi")
                .businessId(application.getId())
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .name(application.getName())
                .seq(1)
                .role("dev")
                .build();
        userPermissionFacade.grantUserPermission(grantUserPermission1);

        UserPermissionParam.GrantUserPermission grantUserPermission2 = UserPermissionParam.GrantUserPermission.builder()
                .username("baiyi")
                .displayName("baiyi")
                .businessId(application.getId())
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .name(application.getName())
                .seq(1)
                .role("daily")
                .build();
        userPermissionFacade.grantUserPermission(grantUserPermission2);
    }

    @Test
    void test2() {
        UserPermissionVO.UserPermissionDetails details = userPermissionFacade.getUserPermissionDetailsByUsername(
                "baiyi");
        System.out.println(details);
    }

    @Test
    void test3() {
        UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails = UserPermissionParam.QueryBusinessUserPermissionDetails.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(2)
                .build();
        UserPermissionVO.BusinessUserPermissionDetails details = userPermissionFacade.queryBusinessUserPermissionDetails(
                queryBusinessUserPermissionDetails);
        System.out.println(details);
    }


}
