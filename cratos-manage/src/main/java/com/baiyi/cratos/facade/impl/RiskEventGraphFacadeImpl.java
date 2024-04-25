package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.base.GraphVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.risk.RiskEventGraphVO;
import com.baiyi.cratos.facade.RiskEventGraphFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.service.RiskEventService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    private final TagService tagService;

    private final RiskEventImpactService impactService;

    private final BusinessTagService businessTagService;

    private static final String FIN_LOSSES_TAG = "FinLosses";

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
        RiskEventGraphVO.MonthlySlaCostBarGraph monthlySlaCostBarGraph = getMonthlySlaCostBarGraph(riskEventGraphQuery);
        RiskEventGraphVO.FinLosses finLosses = getFinLosses(riskEventGraphQuery);
        return RiskEventGraphVO.Graph.builder()
                .slaPieGraph(slaPieGraph)
                .monthlySlaCostBarGraph(monthlySlaCostBarGraph)
                .finLosses(finLosses)
                .build();
    }

    private RiskEventGraphVO.FinLosses getFinLosses(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        Tag tagUniqueKey = Tag.builder()
                .tagKey(FIN_LOSSES_TAG)
                .build();
        Tag tag = tagService.getByUniqueKey(tagUniqueKey);
        if (tag == null) {
            return RiskEventGraphVO.FinLosses.EMPTY;
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.RISK_EVENT.name())
                .build();
        List<Integer> eventIdList = businessTagService.queryBusinessIdByTag(queryByTag);
        Map<String, Integer> finLossesMap = Maps.newHashMap();
        eventIdList.forEach(eventId -> {
            RiskEvent riskEvent = eventService.getById(eventId);
            if (isSkipLoop(riskEventGraphQuery, riskEvent)) {
                return;
            }
            BusinessTag businessTagUniqueKey = BusinessTag.builder()
                    .tagId(tag.getId())
                    .businessId(eventId)
                    .businessType(BusinessTypeEnum.RISK_EVENT.name())
                    .build();
            BusinessTag businessTag = businessTagService.getByUniqueKey(businessTagUniqueKey);
            String[] s = businessTag.getTagValue()
                    .split(":");
            try {
                String type = s[0];
                int currency = Integer.parseInt(s[1]);
                if (finLossesMap.containsKey(type)) {
                    finLossesMap.put(type, currency + finLossesMap.get(type));
                } else {
                    finLossesMap.put(type, currency);
                }
            } catch (NumberFormatException ignored) {
            }
        });
        return RiskEventGraphVO.FinLosses.builder()
                .data(finLossesMap)
                .build();
    }

    private boolean isSkipLoop(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery, RiskEvent riskEvent) {
        if (!riskEvent.getValid()) {
            return true;
        }
        if (StringUtils.hasText(riskEventGraphQuery.getYear())) {
            if (!riskEventGraphQuery.getYear()
                    .equals(riskEvent.getYear())) {
                return true;
            }
        }
        if (StringUtils.hasText(riskEventGraphQuery.getQuarter())) {
            return !riskEventGraphQuery.getQuarter()
                    .equals(riskEvent.getQuarter());
        }
        return false;
    }

    private RiskEventGraphVO.MonthlySlaCostBarGraph getMonthlySlaCostBarGraph(
            RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        List<GraphVO.SimpleData> data;
        if (riskEventGraphQuery.isQueryByTag()) {
            BusinessTagParam.QueryByTag queryByTag = riskEventGraphQuery.getQueryByTag();
            queryByTag.setBusinessType(BusinessTypeEnum.RISK_EVENT_IMPACT.name());
            List<Integer> impactIdList = businessTagService.queryBusinessIdByTag(queryByTag);
            if (CollectionUtils.isEmpty(impactIdList)) {
                data = Collections.emptyList();
            } else {
                data = eventService.querySLADataForTheMonth(riskEventGraphQuery, impactIdList);
            }
        } else {
            data = eventService.querySLADataForTheMonth(riskEventGraphQuery);
        }
        return RiskEventGraphVO.MonthlySlaCostBarGraph.builder()
                .data(data)
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
