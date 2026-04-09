package com.baiyi.cratos.eds.network.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.aliyun.model.AliyunVirtualSwitch;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.network.BaseNetworkAllocator;
import com.baiyi.cratos.eds.network.model.NetworkModel;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 16:13
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_VIRTUAL_SWITCH)
public class AliyunNetworkAllocator extends BaseNetworkAllocator<EdsConfigs.Aliyun, AliyunVirtualSwitch.Switch> {

    public AliyunNetworkAllocator(EdsProviderHolderFactory edsProviderHolderFactory, EdsInstanceService instanceService,
                                  EdsAssetService assetService) {
        super(edsProviderHolderFactory, instanceService, assetService);
    }

    @Override
    protected NetworkModel.Allocation toAllocation(EdsInstance instance, EdsAsset asset,
                                                   AliyunVirtualSwitch.Switch subnet) {
        DescribeVSwitchesResponse.VSwitch virtualSwitch = subnet.getVirtualSwitch();
        return NetworkModel.Allocation.builder()
                .assetId(asset.getId())
                .name(virtualSwitch.getVSwitchName())
                .cidr(virtualSwitch.getCidrBlock())
                .vpcId(virtualSwitch.getVpcId())
                .subnetId(virtualSwitch.getVSwitchId())
                .region(subnet.getRegionId())
                .build();
    }

}
