package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.GraphVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RiskEventMapper extends Mapper<RiskEvent> {

    List<RiskEvent> queryPageByParam(RiskEventParam.RiskEventPageQuery pageQuery);

    List<String> queryYears();

    List<GraphVO.SimpleData> queryMonths(RiskEventParam.RiskEventPageQuery pageQuery);

    List<GraphVO.SimpleData> querySLADataForTheMonth(
            @Param("year") String year, @Param("quarter") String quarter,
            @Param("impactIdList") List<Integer> impactIdList);

}