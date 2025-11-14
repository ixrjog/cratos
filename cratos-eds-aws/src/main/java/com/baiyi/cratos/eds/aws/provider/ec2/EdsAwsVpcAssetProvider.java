package com.baiyi.cratos.eds.aws.provider.ec2;

import com.amazonaws.services.ec2.model.Vpc;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
import com.baiyi.cratos.eds.aws.repo.AwsVpcRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.VPC_CIDR_BLOCK;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午4:39
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_VPC)
public class EdsAwsVpcAssetProvider extends BaseEdsRegionAssetProvider<EdsAwsConfigModel.Aws, AwsEc2.Vpc> {

    public EdsAwsVpcAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<AwsEc2.Vpc> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        try {
            return AwsVpcRepo.describeVpcs(regionId, aws)
                    .stream()
                    .map(e -> toVpc(regionId, e))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AwsEc2.Vpc toVpc(String regionId, Vpc vpc) {
        return AwsEc2.Vpc.builder()
                .regionId(regionId)
                .vpc(vpc)
                .build();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsEc2.Vpc entity) {
        final String tagName = AmazonEc2Util.getName(entity.getVpc()
                .getTags());
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getVpc()
                        .getVpcId())
                .nameOf(StringUtils.hasText(tagName)? tagName : entity.getVpc().getVpcId())
                .regionOf(entity.getRegionId())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                            EdsAsset edsAsset, AwsEc2.Vpc entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, VPC_CIDR_BLOCK, entity.getVpc()
                .getCidrBlock()));
        return indices;
    }

}
