package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.param.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkPlanningFacade;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
import com.baiyi.cratos.wrapper.GlobalNetworkPlanningWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 16:51
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalNetworkPlanningFacadeImpl implements GlobalNetworkPlanningFacade {

    private final GlobalNetworkPlanningService globalNetworkPlanningService;

    private final GlobalNetworkPlanningWrapper globalNetworkPlanningWrapper;

    @Override
    @PageQueryByTag(ofType = BusinessTypeEnum.GLOBAL_NETWORK_PLANNING)
    public DataTable<GlobalNetworkVO.Planning> queryGlobalNetworkPlanningPage(
            GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQuery pageQuery) {
        DataTable<GlobalNetworkPlanning> table = globalNetworkPlanningService.queryGlobalNetworkPlanningPage(
                pageQuery.toParam());
        return globalNetworkPlanningWrapper.wrapToTarget(table);
    }

    @Override
    public void addGlobalNetworkPlanning(GlobalNetworkPlanningParam.AddGlobalNetworkPlanning addGlobalNetworkPlanning) {
        GlobalNetworkPlanning globalNetworkPlanning = addGlobalNetworkPlanning.toTarget();
        globalNetworkPlanningService.add(globalNetworkPlanning);
    }

    @Override
    public void updateGlobalNetworkPlanning(
            GlobalNetworkPlanningParam.UpdateGlobalNetworkPlanning updateGlobalNetworkPlanning) {
        GlobalNetworkPlanning globalNetworkPlanning = updateGlobalNetworkPlanning.toTarget();
        globalNetworkPlanningService.updateByPrimaryKey(globalNetworkPlanning);
    }

    @Override
    public void removeNetwork(int networkId) {
        List<GlobalNetworkPlanning> plannings = globalNetworkPlanningService.queryByNetworkId(networkId);
        if (CollectionUtils.isEmpty(plannings)) {
            return;
        }
        for (GlobalNetworkPlanning planning : plannings) {
            planning.setNetworkId(0);
            globalNetworkPlanningService.updateByPrimaryKey(planning);
        }
    }

    @Override
    public void deleteById(int id) {
        globalNetworkPlanningService.deleteById(id);
    }

}