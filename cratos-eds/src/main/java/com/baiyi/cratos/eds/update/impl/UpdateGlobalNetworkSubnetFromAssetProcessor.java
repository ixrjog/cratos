package com.baiyi.cratos.eds.core.update.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.eds.core.update.BaseUpdateBusinessFromAssetProcessor;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.GlobalNetworkSubnetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_AVAILABLE_IP_ADDRESS_COUNT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/11 17:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
public class UpdateGlobalNetworkSubnetFromAssetProcessor extends BaseUpdateBusinessFromAssetProcessor<GlobalNetworkSubnet> {

    private final GlobalNetworkSubnetService globalNetworkSubnetService;
    private final EdsAssetIndexService edsAssetIndexService;

    public UpdateGlobalNetworkSubnetFromAssetProcessor(BusinessAssetBoundService businessAssetBoundService,
                                                       GlobalNetworkSubnetService globalNetworkSubnetService,
                                                       EdsAssetIndexService edsAssetIndexService) {
        super(businessAssetBoundService);
        this.globalNetworkSubnetService = globalNetworkSubnetService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected GlobalNetworkSubnet getBusiness(BusinessAssetBound businessAssetBound) {
        return globalNetworkSubnetService.getById(businessAssetBound.getBusinessId());
    }

    @Override
    protected void updateBusiness(EdsAsset asset, GlobalNetworkSubnet business) {
        EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                .instanceId(asset.getInstanceId())
                .assetId(asset.getId())
                .name(SUBNET_AVAILABLE_IP_ADDRESS_COUNT)
                .build();
        EdsAssetIndex index = edsAssetIndexService.getByUniqueKey(uniqueKey);
        if (index == null || Integer.parseInt(index.getValue()) == business.getResourceTotal()) {
            return;
        }
        business.setResourceTotal(Integer.parseInt(index.getValue()));
        globalNetworkSubnetService.updateByPrimaryKey(business);
    }

}
