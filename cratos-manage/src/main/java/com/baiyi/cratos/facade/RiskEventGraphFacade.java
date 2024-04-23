package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.risk.RiskEventGraphVO;

/**
 * @Author baiyi
 * @Date 2024/4/16 下午2:24
 * @Version 1.0
 */
public interface RiskEventGraphFacade {

    RiskEventGraphVO.Graph queryGraph(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery);

    OptionsVO.Options getYearOptions();

}
