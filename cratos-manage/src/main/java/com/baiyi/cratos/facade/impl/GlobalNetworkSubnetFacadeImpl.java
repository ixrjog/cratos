package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PostImportProcessor;
import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkSubnetParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkSubnetFacade;
import com.baiyi.cratos.service.GlobalNetworkSubnetService;
import com.baiyi.cratos.wrapper.GlobalNetworkSubnetWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:57
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalNetworkSubnetFacadeImpl implements GlobalNetworkSubnetFacade {

    private final GlobalNetworkSubnetService globalNetworkSubnetService;
    private final GlobalNetworkSubnetWrapper globalNetworkSubnetWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public DataTable<GlobalNetworkVO.Subnet> queryGlobalNetworkSubnetPage(
            GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQuery pageQuery) {
        DataTable<GlobalNetworkSubnet> table = globalNetworkSubnetService.queryGlobalNetworkSubnetPage(pageQuery.toParam());
        return globalNetworkSubnetWrapper.wrapToTarget(table);
    }

    @Override
    public void setGlobalNetworkSubnetValidById(int id) {
        globalNetworkSubnetService.updateValidById(id);
    }

    @Override
    @PostImportProcessor(ofType =  BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public GlobalNetworkSubnet addGlobalNetworkSubnet(GlobalNetworkSubnetParam.AddGlobalNetworkSubnet addGlobalNetworkSubnet) {
        GlobalNetworkSubnet globalNetworkSubnet = addGlobalNetworkSubnet.toTarget();
        globalNetworkSubnetService.add(globalNetworkSubnet);
        return globalNetworkSubnet;
    }

    @Override
    public void updateGlobalNetworkSubnet(GlobalNetworkSubnetParam.UpdateGlobalNetworkSubnet updateGlobalNetworkSubnet) {
        GlobalNetworkSubnet globalNetworkSubnet = globalNetworkSubnetService.getById(updateGlobalNetworkSubnet.getId());
        if (globalNetworkSubnet == null) {
            return;
        }
        globalNetworkSubnet.setName(updateGlobalNetworkSubnet.getName());
        globalNetworkSubnet.setComment(updateGlobalNetworkSubnet.getComment());
        globalNetworkSubnetService.updateByPrimaryKey(globalNetworkSubnet);
    }

    @Override
    public void deleteById(int id) {
        globalNetworkSubnetService.deleteById(id);
    }

}
