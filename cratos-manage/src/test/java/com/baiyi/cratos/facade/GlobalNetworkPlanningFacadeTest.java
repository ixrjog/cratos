package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/6 16:51
 * &#064;Version 1.0
 */
public class GlobalNetworkPlanningFacadeTest extends BaseUnit {

    @Resource
    public GlobalNetworkPlanningFacade globalNetworkPlanningFacade;

    @Test
    void test() {
        GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQuery pageQuery = GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQuery.builder()
                .page(1)
                .length(10)
                .build();
        DataTable<GlobalNetworkVO.Planning> dataTable = globalNetworkPlanningFacade.queryGlobalNetworkPlanningPage(
                pageQuery);
        System.out.println(dataTable);
    }

}
