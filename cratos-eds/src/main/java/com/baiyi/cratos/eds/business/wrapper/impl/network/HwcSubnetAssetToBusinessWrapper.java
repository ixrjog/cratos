package com.baiyi.cratos.eds.business.wrapper.impl.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.exception.AssetToBusinessException;
import com.baiyi.cratos.eds.business.wrapper.impl.network.base.BaseGlobalNetworkAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.huaweicloud.model.HuaweicloudSubnet;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_CIDR_BLOCK;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 16:25
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_SUBNET)
public class HwcSubnetAssetToBusinessWrapper extends BaseGlobalNetworkAssetToBusinessWrapper<HuaweicloudSubnet.Subnet> {

    public HwcSubnetAssetToBusinessWrapper(EdsInstanceService edsInstanceService,
                                           EdsAssetIndexService edsAssetIndexService) {
        super(edsInstanceService, edsAssetIndexService);
    }

    @Override
    protected String getCidrBlock(EdsAssetVO.Asset asset) {
        EdsAssetIndex edsAssetIndex = getAssetIndex(asset, SUBNET_CIDR_BLOCK);
        if (edsAssetIndex == null) {
            throw new AssetToBusinessException("Unable to query the CidrBlock of the current asset. assetId={}",
                    asset.getId());
        }
        return edsAssetIndex.getValue();
    }

    @Override
    protected int getResourceTotal(EdsAssetVO.Asset asset) {
        return 0;
    }

}