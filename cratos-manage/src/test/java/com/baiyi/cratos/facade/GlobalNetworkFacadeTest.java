package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/6 16:39
 * &#064;Version 1.0
 */
public class GlobalNetworkFacadeTest extends BaseUnit {

    @Resource
    public GlobalNetworkFacade globalNetworkFacade;

    @Test
    void test() {
        GlobalNetworkParam.QueryGlobalNetworkDetails queryGlobalNetworkDetails = GlobalNetworkParam.QueryGlobalNetworkDetails.builder()
                .id(5)
                .build();
        GlobalNetworkVO.NetworkDetails details = globalNetworkFacade.queryGlobalNetworkDetails(
                queryGlobalNetworkDetails);
        System.out.println(details);
    }

}
