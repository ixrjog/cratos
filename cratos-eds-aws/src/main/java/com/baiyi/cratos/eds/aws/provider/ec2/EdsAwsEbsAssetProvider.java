package com.baiyi.cratos.eds.aws.provider.ec2;

import com.amazonaws.services.ec2.model.Volume;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.aws.repo.AwsEbsRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/12 上午10:16
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_EBS)
public class EdsAwsEbsAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAwsConfigModel.Aws, Volume> {

    private final AwsEbsRepo ebsRepo;

    public EdsAwsEbsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade, AwsEbsRepo ebsRepo,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.ebsRepo = ebsRepo;
    }

    @Override
    protected List<Volume> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        return ebsRepo.listVolumes(regionId, aws);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, Volume entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVolumeId())
                .nameOf(AmazonEc2Util.getName(entity.getTags()))
                .kindOf(entity.getVolumeType())
                .zoneOf(entity.getAvailabilityZone())
                .createdTimeOf(entity.getCreateTime())
                .descriptionOf(String.valueOf(entity.getSize()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                                      EdsAsset edsAsset, Volume entity) {
        return entity.getAttachments()
                .stream()
                .filter(attachment -> StringUtils.hasText(attachment.getInstanceId()))
                .map(attachment -> createEdsAssetIndex(edsAsset, attachment.getVolumeId(), attachment.getInstanceId()))
                .collect(Collectors.toList());
    }

}
