package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.finops.FinOpsParam;
import com.baiyi.cratos.domain.view.finops.FinOpsVO;
import com.baiyi.cratos.facade.fin.FinOpsFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 16:18
 * &#064;Version 1.0
 */
public class FinOpsFacadeTest extends BaseUnit {

    @Resource
    public FinOpsFacade finOpsFacade;

    @Test
    void test() {
        // finOpsFacade.calculateCost();
        FinOpsParam.QueryAppCost queryAppCost = FinOpsParam.QueryAppCost.builder()
                .allocationCategories(java.util.List.of(FinOpsParam.AllocationCategory.builder()
                        .name("杭州杭州啊啊啊啊啊aaaa")
                        .currencyCode("USD")
                        .amount(100000L)
                        .build(), FinOpsParam.AllocationCategory.builder()
                        .name("Test Category2")
                        .currencyCode("CNY")
                        .amount(200000L)
                        .build()))
                .build();
        FinOpsVO.AppCost appCost = finOpsFacade.queryAppCost(queryAppCost);
        System.out.println("\n\n" + appCost.getCostTable());
        System.out.println("\n\n" + appCost.getCostDetailsTable());
    }

}
