package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.mapper.GlobalNetworkPlanningMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 16:16
 * &#064;Version 1.0
 */
public interface GlobalNetworkPlanningService extends BaseValidService<GlobalNetworkPlanning, GlobalNetworkPlanningMapper>, BaseUniqueKeyService<GlobalNetworkPlanning, GlobalNetworkPlanningMapper> {

    DataTable<GlobalNetworkPlanning> queryGlobalNetworkPlanningPage(GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQueryParam param);

    List<GlobalNetworkPlanning> queryByNetworkId(int networkId);

}
