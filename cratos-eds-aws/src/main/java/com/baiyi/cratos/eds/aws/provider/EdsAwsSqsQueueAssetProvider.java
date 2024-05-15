package com.baiyi.cratos.eds.aws.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsSqs;
import com.baiyi.cratos.eds.aws.repo.AwsSqsRepo;
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
 * &#064;Date  2024/5/15 上午10:07
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_SQS_QUEUE)
public class EdsAwsSqsQueueAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, AwsSqs.Queue> {

    private final AwsSqsRepo awsSqsRepo;

    public EdsAwsSqsQueueAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade, AwsSqsRepo awsSqsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.awsSqsRepo = awsSqsRepo;
    }

    @Override
    protected List<AwsSqs.Queue> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            Set<String> regionIdSet = Sets.newHashSet(aws.getRegionId());
            regionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<AwsSqs.Queue> entities = Lists.newArrayList();
            regionIdSet.forEach(regionId -> {
                List<String> queues = awsSqsRepo.listQueues(regionId, aws);
                if (!CollectionUtils.isEmpty(queues)) {
                    entities.addAll(toQueues(regionId, aws, queues));
                }
            });
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private List<AwsSqs.Queue> toQueues(String regionId, EdsAwsConfigModel.Aws aws, List<String> queues) {
        return queues.stream()
                .map(e -> {
                    Map<String, String> attributes = awsSqsRepo.getQueueAttributes(regionId, aws, e);
                    return AwsSqs.Queue.builder()
                            .regionId(regionId)
                            .attributes(attributes)
                            .queue(e)
                            .build();
                })
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsSqs.Queue entity) {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getAttributes()
                        .get("QueueArn"))
                .assetKeyOf(entity.getQueue())
                .nameOf(StringUtils.substringAfterLast(entity.getQueue(), "/"))
                .regionOf(entity.getRegionId())
                .createdTimeOf(new Date(Long.parseLong(entity.getAttributes()
                        .get("CreatedTimestamp")) * 1000))
                .build();
    }

}