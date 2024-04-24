package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import tk.mybatis.mapper.common.Mapper;

public interface RiskEventImpactMapper extends Mapper<RiskEventImpact> {

    Integer queryTotalCostByParam(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery);

}