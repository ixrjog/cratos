package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.facade.inspection.impl.ResignationUsersInspectionTask;
import com.baiyi.cratos.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 13:56
 * &#064;Version 1.0
 */
public class ResignationUsersProcessorTest extends BaseUnit {

    @Resource
    private ResignationUsersInspectionTask resignationUsersInspection;
    @Autowired
    private UserService userService;
    @Resource
    private EdsIdentityFacade edsIdentityFacade;
//
//    @Resource
//    private UserService userService;
//
//    @Resource
//    private EdsIdentityFacade edsIdentityFacade;
//
//    @Test
//    void resignationUsersTest() {
//        resignationUsersProcessor.doTask();
//    }
//
//    @Test
//    void test2() {
//        EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
//                .username("huoquan")
//                .build();
//        EdsIdentityVO.DingtalkIdentityDetails details = edsIdentityFacade.queryDingtalkIdentityDetails(
//                queryDingtalkIdentityDetails);
//        System.out.println(details);
//    }


    @Test
    void test() {
       resignationUsersInspection.inspectionTask();

        User user = userService.getByUsername("iamdylin");

        System.out.println(user);

        EdsIdentityParam.QueryDingtalkIdentityDetails queryDetails = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username(user.getUsername())
                .build();
        EdsIdentityVO.DingtalkIdentityDetails dingtalkDetails = edsIdentityFacade.queryDingtalkIdentityDetails(
                queryDetails);
        boolean x = CollectionUtils.isEmpty(dingtalkDetails.getDingtalkIdentities());
        System.out.println(x);


    }

}
