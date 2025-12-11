package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午5:54
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RISK_EVENT_IMPACT)
public class RiskEventImpactWrapper extends BaseDataTableConverter<RiskEventVO.Impact, RiskEventImpact> implements BaseBusinessWrapper<RiskEventVO.IRiskEventImpacts, RiskEventVO.Impact> {

    private final RiskEventImpactService impactService;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(RiskEventVO.Impact vo) {
        if (vo.getCost() != 0) {
            RiskEventVO.CostDetail costDetail = RiskEventVO.CostDetail.builder()
                    .cost(vo.getCost())
                    .costDesc(TimeUtils.convertSecondsToHMS(vo.getCost()))
                    .build();
            vo.setCostDetail(costDetail);
        }
    }

    @Override
    public void decorateBusiness(RiskEventVO.IRiskEventImpacts biz) {
        if (IdentityUtils.hasIdentity(biz.getEventId())) {
            List<RiskEventImpact> impactList = impactService.queryByEventId(biz.getEventId());
            if (CollectionUtils.isEmpty(impactList)) {
                return;
            }
            AtomicInteger sumCost = new AtomicInteger();
            List<RiskEventVO.Impact> impacts = impactList.stream()
                    .map(e -> {
                        RiskEventVO.Impact impact = this.convert(e);
                        delegateWrap(impact);
                        sumCost.set(sumCost.get() + impact.getCost());
                        return impact;
                    })
                    .sorted(Comparator.comparingLong(RiskEventVO.Impact::getSeq))
                    .collect(Collectors.toList());
            biz.setImpacts(impacts);
            RiskEventVO.CostDetail totalCost = RiskEventVO.CostDetail.builder()
                    .cost(sumCost.get())
                    .costDesc(TimeUtils.convertSecondsToHMS(sumCost.get()))
                    .build();
            biz.setTotalCost(totalCost);
        }
    }

}
