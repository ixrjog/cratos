package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.util.NetworkUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkFacade;
import com.baiyi.cratos.facade.GlobalNetworkPlanningFacade;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.baiyi.cratos.wrapper.GlobalNetworkWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 11:32
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalNetworkFacadeImpl implements GlobalNetworkFacade {

    private final GlobalNetworkService globalNetworkService;

    private final GlobalNetworkWrapper globalNetworkWrapper;

    private final GlobalNetworkPlanningFacade globalNetworkPlanningFacade;

    @Override
    @PageQueryByTag(ofType = BusinessTypeEnum.GLOBAL_NETWORK)
    public DataTable<GlobalNetworkVO.Network> queryGlobalNetworkPage(
            GlobalNetworkParam.GlobalNetworkPageQuery pageQuery) {
        DataTable<GlobalNetwork> table = globalNetworkService.queryGlobalNetworkPage(pageQuery.toParam());
        return globalNetworkWrapper.wrapToTarget(table);
    }

    @Override
    public void setGlobalNetworkValidById(int id) {
        globalNetworkService.updateValidById(id);
    }

    @Override
    public void addGlobalNetwork(GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork) {
        GlobalNetwork globalNetwork = addGlobalNetwork.toTarget();
        int resourceTotal = NetworkUtil.getIpCount(StringUtils.substringAfter(globalNetwork.getCidrBlock(), "/"));
        globalNetwork.setResourceTotal(resourceTotal);
        globalNetworkService.add(globalNetwork);
    }

    @Override
    public void updateGlobalNetwork(GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork) {
        GlobalNetwork globalNetwork = globalNetworkService.getById(updateGlobalNetwork.getId());
        globalNetwork.setName(updateGlobalNetwork.getName());
        globalNetwork.setMainName(updateGlobalNetwork.getMainName());
        globalNetwork.setComment(updateGlobalNetwork.getComment());
        globalNetworkService.updateByPrimaryKey(globalNetwork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        globalNetworkPlanningFacade.removeNetwork(id);
        globalNetworkService.deleteById(id);
    }

}