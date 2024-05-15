package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.sns.model.Topic;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsSns;
import com.baiyi.cratos.eds.aws.repo.AwsSnsRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午2:03
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_SNS_TOPIC)
public class EdsAwsSnsTopicAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, AwsSns.Topic> {

    private final AwsSnsRepo awsSnsRepo;

    public EdsAwsSnsTopicAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade, AwsSnsRepo awsSnsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.awsSnsRepo = awsSnsRepo;
    }

    @Override
    protected List<AwsSns.Topic> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            Set<String> regionIdSet = Sets.newHashSet(aws.getRegionId());
            regionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<AwsSns.Topic> entities = Lists.newArrayList();
            regionIdSet.forEach(regionId -> {
                List<Topic> topics = awsSnsRepo.listTopics(regionId, aws);
                if (!CollectionUtils.isEmpty(topics)) {
                    entities.addAll(toTopics(regionId, aws, topics));
                }
            });
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private List<AwsSns.Topic> toTopics(String regionId, EdsAwsConfigModel.Aws aws, List<Topic> topics) {
        return topics.stream()
                .map(e -> {
                    Map<String, String> attributes = awsSnsRepo.getTopicAttributes(regionId, aws, e.getTopicArn());
                    return AwsSns.Topic.builder()
                            .regionId(regionId)
                            .attributes(attributes)
                            .topic(e)
                            .build();
                })
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsSns.Topic entity) {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getTopic().getTopicArn())
                .nameOf(StringUtils.substringAfterLast(entity.getTopic().getTopicArn(), "/"))
                .regionOf(entity.getRegionId())
                .build();
    }

}