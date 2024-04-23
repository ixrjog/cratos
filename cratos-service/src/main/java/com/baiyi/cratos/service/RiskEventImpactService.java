package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.mapper.RiskEventImpactMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午4:59
 * @Version 1.0
 */
public interface RiskEventImpactService extends BaseValidService<RiskEventImpact, RiskEventImpactMapper>, SupportBusinessService {

    List<RiskEventImpact> queryByEventId(int eventId);

    Integer queryTotalCostByParam(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery);

}
