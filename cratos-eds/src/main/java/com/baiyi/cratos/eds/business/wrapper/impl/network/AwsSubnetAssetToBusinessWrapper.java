package com.baiyi.cratos.eds.business.wrapper.impl.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
import com.baiyi.cratos.eds.business.exception.AssetToBusinessException;
import com.baiyi.cratos.eds.business.wrapper.impl.network.base.BaseGlobalNetworkAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_CIDR_BLOCK;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 14:35
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SUBNET)
public class AwsSubnetAssetToBusinessWrapper extends BaseGlobalNetworkAssetToBusinessWrapper<AwsEc2.Subnet> {

    public AwsSubnetAssetToBusinessWrapper(BusinessAssetBindService businessAssetBindService,
                                           EdsInstanceService edsInstanceService,
                                           EdsAssetIndexService edsAssetIndexService) {
        super(businessAssetBindService, edsInstanceService, edsAssetIndexService);
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
        AwsEc2.Subnet model = getAssetModel(asset);
        return model.getSubnet()
                .getAvailableIpAddressCount();
    }

}