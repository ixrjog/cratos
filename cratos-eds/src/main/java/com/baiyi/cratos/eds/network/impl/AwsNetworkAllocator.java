package com.baiyi.cratos.eds.network.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
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
 * &#064;Date  2026/4/9 16:57
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SUBNET)
public class AwsNetworkAllocator extends BaseNetworkAllocator<EdsConfigs.Aws, AwsEc2.Subnet> {

    public AwsNetworkAllocator(EdsProviderHolderFactory edsProviderHolderFactory, EdsInstanceService instanceService,
                               EdsAssetService assetService) {
        super(edsProviderHolderFactory, instanceService, assetService);
    }

    @Override
    protected NetworkModel.Allocation toAllocation(EdsInstance instance, EdsAsset asset, AwsEc2.Subnet subnet) {
        com.amazonaws.services.ec2.model.Subnet sn = subnet.getSubnet();
        return NetworkModel.Allocation.builder()
                .assetId(asset.getId())
                .name(asset.getName())
                .cidr(sn.getCidrBlock())
                .vpcId(sn.getVpcId())
                .subnetId(sn.getSubnetId())
                .region(subnet.getRegionId())
                .build();
    }

}