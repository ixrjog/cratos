package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.ec2.model.Volume;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.repo.AwsEbsRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/12 上午10:16
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_EBS)
public class EcsAwsEbsAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, Volume> {

    private final AwsEbsRepo ebsRepo;

    @Override
    protected List<Volume> listEntities(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        try {
            EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
            Set<String> reggionIdSet = Sets.newHashSet(aws.getRegionId());
            reggionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<Volume> entities = Lists.newArrayList();
            reggionIdSet.forEach(regionId -> entities.addAll(ebsRepo.listVolumes(regionId, aws)));
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, Volume entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVolumeId())
                .nameOf(AmazonEc2Util.getInstanceName(entity.getTags()))
                .kindOf(entity.getVolumeType())
                .zoneOf(entity.getAvailabilityZone())
                .createdTimeOf(entity.getCreateTime())
                .descriptionOf(String.valueOf(entity.getSize()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(EdsAsset edsAsset, Volume entity) {
        return entity.getAttachments()
                .stream()
                .filter(attachment -> StringUtils.hasText(attachment.getInstanceId()))
                .map(attachment -> EdsAssetIndex.builder()
                        .instanceId(edsAsset.getInstanceId())
                        .assetId(edsAsset.getId())
                        .name(attachment.getVolumeId())
                        .value(attachment.getInstanceId())
                        .build())
                .collect(Collectors.toList());
    }

}