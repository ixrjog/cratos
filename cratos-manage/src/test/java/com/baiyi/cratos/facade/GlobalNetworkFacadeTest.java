package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.GlobalNetworkSubnetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/6 16:39
 * &#064;Version 1.0
 */
public class GlobalNetworkFacadeTest extends BaseUnit {

    @Resource
    private GlobalNetworkFacade globalNetworkFacade;

    @Resource
    private GlobalNetworkSubnetService globalNetworkSubnetService;

    @Resource
    private BusinessAssetBindService businessAssetBindService;

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test() {
        GlobalNetworkParam.QueryGlobalNetworkDetails queryGlobalNetworkDetails = GlobalNetworkParam.QueryGlobalNetworkDetails.builder()
                .id(5)
                .build();
        GlobalNetworkVO.NetworkDetails details = globalNetworkFacade.queryGlobalNetworkDetails(
                queryGlobalNetworkDetails);
        System.out.println(details);
    }

    @Test
    void test2() {
        globalNetworkSubnetService.selectAll()
                .forEach(e -> {
                    SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                            .businessType(BusinessTypeEnum.GLOBAL_NETWORK_SUBNET.name())
                            .businessId(e.getId())
                            .build();
                    List<BusinessAssetBind> binds = businessAssetBindService.queryByBusiness(simpleBusiness);
                    if (!CollectionUtils.isEmpty(binds)) {
                        BusinessAssetBind bind = binds.getFirst();
                        EdsAsset edsAsset = edsAssetService.getById(bind.getAssetId());
                        if (edsAsset != null) {
                            e.setSubnetKey(edsAsset.getAssetId());
                            e.setRegion(edsAsset.getRegion());
                            e.setZone(edsAsset.getZone());
                            globalNetworkSubnetService.updateByPrimaryKey(e);
                            System.out.println(e);
                        }
                    }
                });

    }

}
