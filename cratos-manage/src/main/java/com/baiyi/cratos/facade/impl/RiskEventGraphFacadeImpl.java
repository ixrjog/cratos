package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.GraphVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.risk.RiskEventGraphVO;
import com.baiyi.cratos.facade.RiskEventGraphFacade;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.service.RiskEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.common.util.TimeUtil.THE_NUMBER_OF_SECONDS_IN_A_DAY;

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

    @Override
    public RiskEventGraphVO.Graph queryGraph(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        Integer cost = impactService.queryTotalCostByParam(riskEventGraphQuery);
        if (cost == null) {
            cost = 0;
        }
        int days = getDays(riskEventGraphQuery);
        RiskEventGraphVO.SlaPieGraph slaPieGraph = RiskEventGraphVO.SlaPieGraph.builder()
                .total(days * THE_NUMBER_OF_SECONDS_IN_A_DAY)
                .cost(cost)
                .build();

        List<GraphVO.SimpleData> data = eventService.querySLADataForTheMonth(riskEventGraphQuery);
        RiskEventGraphVO.MonthlySlaCostBarGraph monthlySlaCostBarGraph = RiskEventGraphVO.MonthlySlaCostBarGraph.builder()
                .data(data)
                .build();
        return RiskEventGraphVO.Graph.builder()
                .slaPieGraph(slaPieGraph)
                .monthlySlaCostBarGraph(monthlySlaCostBarGraph)
                .build();
    }

    private int getDays(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        int isoYear = Integer.parseInt(riskEventGraphQuery.getYear());
        if (StringUtils.hasText(riskEventGraphQuery.getQuarter())) {
            int quarter = Integer.parseInt(riskEventGraphQuery.getQuarter());
            return TimeUtil.getDaysByQuarter(isoYear, quarter);
        } else {
            return TimeUtil.getDaysByYear(isoYear);
        }
    }

    @Override
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
