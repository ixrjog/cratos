package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.risk.RiskEventImpactParam;
import com.baiyi.cratos.domain.param.http.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:04
 * @Version 1.0
 */
public interface RiskEventFacade {

    DataTable<RiskEventVO.Event> queryRiskEventPage(RiskEventParam.RiskEventPageQuery pageQuery);

    RiskEventVO.Event getRiskEventById(int id);

    void addRiskEvent(RiskEventParam.AddRiskEvent addRiskEvent);

    void updateRiskEvent(RiskEventParam.UpdateRiskEvent updateRiskEvent);

    void addRiskEventImpact(RiskEventImpactParam.AddRiskEventImpact addRiskEventImpact);

    void updateRiskEventImpact(RiskEventImpactParam.UpdateRiskEventImpact updateRiskEventImpact);

    void deleteRiskEventImpactById(int id);


}
