package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.exception.RiskEventImpactException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.http.risk.RiskEventImpactParam;
import com.baiyi.cratos.domain.param.http.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;
import com.baiyi.cratos.facade.RiskEventFacade;
import com.baiyi.cratos.service.RiskEventImpactService;
import com.baiyi.cratos.service.RiskEventService;
import com.baiyi.cratos.wrapper.RiskEventWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static com.baiyi.cratos.common.util.TimeUtils.YEAR;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RiskEventFacadeImpl implements RiskEventFacade {

    private final RiskEventService riskEventService;
    private final RiskEventImpactService impactService;
    private final RiskEventWrapper riskEventWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.RISK_EVENT)
    public DataTable<RiskEventVO.Event> queryRiskEventPage(RiskEventParam.RiskEventPageQuery pageQuery) {
        DataTable<RiskEvent> table = riskEventService.queryRiskEventPage(pageQuery.toParam());
        return riskEventWrapper.wrapToTarget(table);
    }

    @Override
    public RiskEventVO.Event getRiskEventById(int id) {
        RiskEvent riskEvent = riskEventService.getById(id);
        return riskEventWrapper.wrapToTarget(riskEvent);
    }

    @Override
    public void addRiskEvent(RiskEventParam.AddRiskEvent addRiskEvent) {
        RiskEvent riskEvent = addRiskEvent.toTarget();
        Date eventTime = riskEvent.getEventTime();
        riskEvent.setYear(TimeUtils.parse(eventTime, YEAR));
        riskEvent.setWeeks(TimeUtils.getWeekOfYear(eventTime));
        riskEvent.setQuarter(String.valueOf(TimeUtils.getQuarter(eventTime)));
        riskEventService.add(riskEvent);
    }

    @Override
    public void updateRiskEvent(RiskEventParam.UpdateRiskEvent updateRiskEvent) {
        RiskEvent riskEvent = updateRiskEvent.toTarget();
        Date eventTime = riskEvent.getEventTime();
        riskEvent.setYear(TimeUtils.parse(eventTime, YEAR));
        riskEvent.setWeeks(TimeUtils.getWeekOfYear(eventTime));
        riskEvent.setQuarter(String.valueOf(TimeUtils.getQuarter(eventTime)));
        riskEventService.updateByPrimaryKey(riskEvent);
        if (!riskEvent.getValid()) {
            List<RiskEventImpact> impacts = impactService.queryByEventId(riskEvent.getId());
            if (!CollectionUtils.isEmpty(impacts)) {
                impacts.forEach(e -> {
                    if (!e.getValid()) {
                        e.setValid(false);
                        impactService.updateByPrimaryKey(e);
                    }
                });
            }
        }
    }

    @Override
    public void addRiskEventImpact(RiskEventImpactParam.AddRiskEventImpact addRiskEventImpact) {
        RiskEventImpact riskEventImpact = addRiskEventImpact.toTarget();
        saveRiskEventImpact(riskEventImpact);
    }

    @Override
    public void updateRiskEventImpact(RiskEventImpactParam.UpdateRiskEventImpact updateRiskEventImpact) {
        RiskEventImpact riskEventImpact = updateRiskEventImpact.toTarget();
        saveRiskEventImpact(riskEventImpact);
    }

    @Override
    public void deleteRiskEventImpactById(int id) {
        if (impactService.getById(id) != null) {
            impactService.deleteById(id);
        }
    }

    private void saveRiskEventImpact(RiskEventImpact riskEventImpact) {
        if (riskEventImpact.getSla()) {
            if (riskEventImpact.getStartTime() == null) {
                throw new RiskEventImpactException("SLA impact must specify a start time.");
            }
            if (riskEventImpact.getEndTime() == null) {
                throw new RiskEventImpactException("SLA impact must specify a end time.");
            }
            if (riskEventImpact.getStartTime()
                    .getTime() >= riskEventImpact.getEndTime()
                    .getTime()) {
                throw new RiskEventImpactException("The start time is greater than the end time.");
            }
            // 设置SLA 成本
            riskEventImpact.setCost((int) (riskEventImpact.getEndTime()
                    .getTime() - riskEventImpact.getStartTime()
                    .getTime()) / 1000);
        } else {
            riskEventImpact.setCost(0);
        }
        if (IdentityUtils.hasIdentity(riskEventImpact.getId())) {
            impactService.updateByPrimaryKey(riskEventImpact);
        } else {
            impactService.add(riskEventImpact);
        }
    }

}
