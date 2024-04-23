package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;


/**
 * @Author baiyi
 * @Date 2024/4/16 下午2:38
 * @Version 1.0
 */
class RiskEventReportFacadeTest extends BaseUnit {

    @Resource
    private RiskEventGraphFacade riskEventReportFacade;

    @Test
    void test() {
        OptionsVO.Options options = riskEventReportFacade.getYearOptions();
        System.out.println(options);
    }

}