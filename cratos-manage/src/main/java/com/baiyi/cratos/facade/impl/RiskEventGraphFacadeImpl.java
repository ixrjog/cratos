package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.risk.RiskEventGraphVO;
import com.baiyi.cratos.facade.RiskEventGraphFacade;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.service.RiskEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/16 下午2:24
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RiskEventGraphFacadeImpl implements RiskEventGraphFacade {

    private final RiskEventService eventService;

    private final RiskEventImpactService impactService;

    public RiskEventGraphVO.Graph queryGraph(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        Integer cost = impactService.queryTotalCostByParam(riskEventGraphQuery);
        if (cost == null) {
            cost = 0;
        }
        int yearDays = TimeUtil.getDaysByYear(Integer.parseInt(riskEventGraphQuery.getYear()));
        RiskEventGraphVO.SlaGraph slaGraph = RiskEventGraphVO.SlaGraph.builder()
                .total(yearDays * 86400)
                .cost(cost)
                .build();
        return RiskEventGraphVO.Graph.builder()
                .slaGraph(slaGraph)
                .build();
    }

    public OptionsVO.Options getYearOptions() {
        List<OptionsVO.Option> options = eventService.queryYears()
                .stream()
                .map(e -> OptionsVO.Option.builder()
                        .value(e)
                        .label(e)
                        .build())
                .collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(options)
                .build();
    }

}
