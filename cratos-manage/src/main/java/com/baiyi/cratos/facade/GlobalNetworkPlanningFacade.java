package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 16:51
 * &#064;Version 1.0
 */
public interface GlobalNetworkPlanningFacade {

    DataTable<GlobalNetworkVO.Planning> queryGlobalNetworkPlanningPage(
            GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQuery pageQuery);

    void addGlobalNetworkPlanning(GlobalNetworkPlanningParam.AddGlobalNetworkPlanning addGlobalNetworkPlanning);

    void updateGlobalNetworkPlanning(
            GlobalNetworkPlanningParam.UpdateGlobalNetworkPlanning updateGlobalNetworkPlanning);

    void removeNetwork(int networkId);

    void deleteById(int id);

}