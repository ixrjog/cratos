package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.BindAssetsAfterImport;
import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkFacade;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.baiyi.cratos.wrapper.GlobalNetworkWrapper;
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
public class GlobalNetworkFacadeImpl implements GlobalNetworkFacade {

    private final GlobalNetworkService globalNetworkService;

    private final GlobalNetworkWrapper globalNetworkWrapper;

    @Override
    @PageQueryByTag(ofType = BusinessTypeEnum.GLOBAL_NETWORK)
    public DataTable<GlobalNetworkVO.GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQuery pageQuery) {
        DataTable<GlobalNetwork> table = globalNetworkService.queryGlobalNetworkPage(pageQuery.toParam());
        return globalNetworkWrapper.wrapToTarget(table);
    }

    @Override
    public void setGlobalNetworkValidById(int id) {
        globalNetworkService.updateValidById(id);
    }

    @Override
    @BindAssetsAfterImport
    public GlobalNetwork addGlobalNetwork(GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork) {
        GlobalNetwork globalNetwork = addGlobalNetwork.toTarget();
        globalNetworkService.add(globalNetwork);
        return globalNetwork;
    }

    @Override
    public void updateGlobalNetwork(GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork) {
        GlobalNetwork globalNetwork = globalNetworkService.getById(updateGlobalNetwork.getId());
        if (globalNetwork == null) {
            return;
        }
        globalNetwork.setName(updateGlobalNetwork.getName());
        globalNetwork.setComment(updateGlobalNetwork.getComment());
        globalNetworkService.updateByPrimaryKey(globalNetwork);
    }

    @Override
    public void deleteById(int id) {
        globalNetworkService.deleteById(id);
    }

}
