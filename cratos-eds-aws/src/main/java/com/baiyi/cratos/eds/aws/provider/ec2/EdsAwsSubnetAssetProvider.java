package com.baiyi.cratos.eds.aws.provider.ec2;

import com.amazonaws.services.ec2.model.Subnet;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
import com.baiyi.cratos.eds.aws.repo.AwsVpcRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_AVAILABLE_IP_ADDRESS_COUNT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_CIDR_BLOCK;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午5:06
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SUBNET)
public class EdsAwsSubnetAssetProvider extends BaseEdsRegionAssetProvider<EdsAwsConfigModel.Aws, AwsEc2.Subnet> {

    public EdsAwsSubnetAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<AwsEc2.Subnet> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        try {
            return AwsVpcRepo.describeSubnets(regionId, aws)
                    .stream()
                    .map(e -> toSubnet(regionId, e))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AwsEc2.Subnet toSubnet(String regionId, Subnet subnet) {
        return AwsEc2.Subnet.builder()
                .regionId(regionId)
                .subnet(subnet)
                .build();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsEc2.Subnet entity) {
        final String tagName = AmazonEc2Util.getName(entity.getSubnet()
                .getTags());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getSubnet()
                        .getSubnetId())
                .nameOf(StringUtils.hasText(tagName) ? tagName : entity.getSubnet()
                        .getSubnetId())
                .assetKeyOf(entity.getSubnet()
                        .getSubnetArn())
                .zoneOf(entity.getSubnet()
                        .getAvailabilityZone())
                .regionOf(entity.getRegionId())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                            EdsAsset edsAsset, AwsEc2.Subnet entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, SUBNET_CIDR_BLOCK, entity.getSubnet()
                .getCidrBlock()));
        indices.add(createEdsAssetIndex(edsAsset, SUBNET_AVAILABLE_IP_ADDRESS_COUNT, entity.getSubnet()
                .getAvailableIpAddressCount()));
        return indices;
    }

}

