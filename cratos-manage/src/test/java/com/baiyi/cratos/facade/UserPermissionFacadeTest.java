package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
import com.baiyi.cratos.service.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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
        UserPermissionParam.QueryBusinessUserPermissionDetails query = UserPermissionParam.QueryBusinessUserPermissionDetails.builder()
                .businessId(2)
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .username("baiyi")
                .build();
        UserPermissionVO.UserPermissionDetails details = userPermissionFacade.queryUserPermissionDetails(query);
        System.out.println(details);
    }

    @Test
    void test2() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                "baiyi", null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
        UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails = UserPermissionParam.QueryBusinessUserPermissionDetails.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(2)
                .build();
        UserPermissionVO.UserPermissionDetails details = userPermissionFacade.queryUserPermissionDetails(
                queryBusinessUserPermissionDetails);
        System.out.println(details);
    }

    @Test
    void test3() {
        UserPermissionParam.QueryAllBusinessUserPermissionDetails query = UserPermissionParam.QueryAllBusinessUserPermissionDetails.builder()
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .username("baiyi")
                .build();
        UserPermissionVO.UserPermissionDetails details = userPermissionFacade.queryUserPermissionDetails(query);
        System.out.println(details);
    }

    @Test
    void test4() {
        UserPermissionVO.BusinessUserPermissionDetails details = userPermissionFacade.getUserBusinessUserPermissionDetails(
                "baiyi");
        System.out.println(details);
    }

//    @Test
//    void test3() {
//        UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails = UserPermissionParam.QueryBusinessUserPermissionDetails.builder()
//                .businessType(BusinessTypeEnum.APPLICATION.name())
//                .businessId(2)
//                .build();
//        UserPermissionVO.BusinessUserPermissionDetails details = userPermissionFacade.queryBusinessUserPermissionDetails(
//                queryBusinessUserPermissionDetails);
//        System.out.println(details);
//    }


}
