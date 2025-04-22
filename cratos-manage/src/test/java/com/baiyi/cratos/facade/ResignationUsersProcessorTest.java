package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.facade.identity.ResignationUsersProcessor;
import com.baiyi.cratos.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 13:56
 * &#064;Version 1.0
 */
public class ResignationUsersProcessorTest extends BaseUnit {

    @Resource
    private ResignationUsersProcessor resignationUsersProcessor;

    @Resource
    private UserService userService;

    @Resource
    private EdsIdentityFacade edsIdentityFacade;

    @Test
    void resignationUsersTest() {
        resignationUsersProcessor.doTask();
    }

    @Test
    void test2() {
        EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username("huoquan")
                .build();
        EdsIdentityVO.DingtalkIdentityDetails details = edsIdentityFacade.queryDingtalkIdentityDetails(
                queryDingtalkIdentityDetails);
        System.out.println(details);
    }

}
