package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 10:39
 * &#064;Version 1.0
 */
public class EdsIdentityFacadeTest extends BaseUnit {

    @Resource
    private EdsIdentityFacade edsIdentityFacade;

    @Test
    void test1() {
        EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails = EdsIdentityParam.QueryMailIdentityDetails.builder()
                .username("shangguanqin")
                .build();
        EdsIdentityVO.MailIdentityDetails details = edsIdentityFacade.queryMailIdentityDetails(
                queryMailIdentityDetails);
        System.out.println(details);
    }

}
