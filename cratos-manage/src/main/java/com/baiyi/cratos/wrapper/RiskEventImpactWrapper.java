package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
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
public class RiskEventImpactWrapper extends BaseDataTableConverter<RiskEventVO.Impact, RiskEventImpact> implements IBusinessWrapper<RiskEventVO.IRiskEventImpacts, RiskEventVO.Impact> {

    private final RiskEventImpactService impactService;

    @Override
    public void wrap(RiskEventVO.Impact impact) {
    }

    @Override
    public void businessWrap(RiskEventVO.IRiskEventImpacts iRiskEventImpacts) {
        if (IdentityUtil.hasIdentity(iRiskEventImpacts.getEventId())) {
            List<RiskEventImpact> impactList = impactService.queryByEventId(iRiskEventImpacts.getEventId());
            if (CollectionUtils.isEmpty(impactList)) {
                return;
            }
            AtomicInteger sumCost = new AtomicInteger();
            List<RiskEventVO.Impact> impacts = impactList.stream()
                    .map(e -> {
                        RiskEventVO.Impact impact = this.convert(e);
                        wrapFromProxy(impact);
                        sumCost.set(sumCost.get() + impact.getCost());
                        return impact;
                    })
                    .sorted(Comparator.comparingLong(RiskEventVO.Impact::getSort))
                    .collect(Collectors.toList());
            iRiskEventImpacts.setImpacts(impacts);
            RiskEventVO.TotalCost totalCost = RiskEventVO.TotalCost.builder()
                    .cost(sumCost.get())
                    .costDesc(TimeUtil.convertSecondsToHMS(sumCost.get()))
                    .build();
            iRiskEventImpacts.setTotalCost(totalCost);
        }
    }

}
