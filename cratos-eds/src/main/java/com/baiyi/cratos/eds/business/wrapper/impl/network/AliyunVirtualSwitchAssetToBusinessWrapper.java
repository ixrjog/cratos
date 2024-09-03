package com.baiyi.cratos.eds.business.wrapper.impl.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunVirtualSwitch;
import com.baiyi.cratos.eds.business.exception.AssetToBusinessException;
import com.baiyi.cratos.eds.business.wrapper.impl.network.base.BaseGlobalNetworkAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.VIRTUAL_SWITCH_CIDR_BLOCK;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 11:24
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_VIRTUAL_SWITCH)
public class AliyunVirtualSwitchAssetToBusinessWrapper extends BaseGlobalNetworkAssetToBusinessWrapper<AliyunVirtualSwitch.Switch> {

    public AliyunVirtualSwitchAssetToBusinessWrapper(EdsInstanceService edsInstanceService,
                                                     EdsAssetIndexService edsAssetIndexService) {
        super(edsInstanceService, edsAssetIndexService);
    }

    @Override
    protected String getCidrBlock(EdsAssetVO.Asset asset) {
        EdsAssetIndex edsAssetIndex = getAssetIndex(asset, VIRTUAL_SWITCH_CIDR_BLOCK);
        if (edsAssetIndex == null) {
            throw new AssetToBusinessException("Unable to query the CidrBlock of the current asset. assetId={}",
                    asset.getId());
        }
        return edsAssetIndex.getValue();
    }

    @Override
    protected int getResourceTotal(EdsAssetVO.Asset asset) {
        AliyunVirtualSwitch.Switch model = getAssetModel(asset);
        return model.getVirtualSwitch()
                .getAvailableIpAddressCount()
                .intValue();
    }

}