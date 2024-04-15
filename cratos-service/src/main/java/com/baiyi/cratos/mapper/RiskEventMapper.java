package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RiskEventMapper extends Mapper<RiskEvent> {

    List<RiskEvent> queryPageByParam(RiskEventParam.RiskEventPageQuery pageQuery);

}