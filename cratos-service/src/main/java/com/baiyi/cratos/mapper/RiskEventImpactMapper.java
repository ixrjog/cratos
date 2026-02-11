package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.http.risk.RiskEventParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface RiskEventImpactMapper extends Mapper<RiskEventImpact> {

    Integer queryTotalCostByParam(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery);

}