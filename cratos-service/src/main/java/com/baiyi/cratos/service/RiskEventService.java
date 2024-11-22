package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.http.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.GraphVO;
import com.baiyi.cratos.mapper.RiskEventMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:19
 * @Version 1.0
 */
public interface RiskEventService extends BaseUniqueKeyService<RiskEvent, RiskEventMapper>, BaseValidService<RiskEvent, RiskEventMapper>, SupportBusinessService {

    DataTable<RiskEvent> queryRiskEventPage(RiskEventParam.RiskEventPageQueryParam param);

    List<String> queryYears();

    List<GraphVO.SimpleData> querySLADataForTheMonth(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery,
                                                     List<Integer> impactIdList);

    default List<GraphVO.SimpleData> querySLADataForTheMonth(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        return querySLADataForTheMonth(riskEventGraphQuery, null);
    }

}
