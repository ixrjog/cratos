package com.baiyi.cratos.eds.aws.provider.sxs;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsSqs;
import com.baiyi.cratos.eds.aws.repo.AwsSqsRepo;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 上午10:07
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SQS_QUEUE)
public class EdsAwsSqsQueueAssetProvider extends BaseEdsRegionAssetProvider<EdsAwsConfigModel.Aws, AwsSqs.Queue> {

    private final AwsSqsRepo awsSqsRepo;

    public EdsAwsSqsQueueAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder, AwsSqsRepo awsSqsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.awsSqsRepo = awsSqsRepo;
    }

    @Override
    protected List<AwsSqs.Queue> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        List<String> queues = awsSqsRepo.listQueues(regionId, aws);
        if (!CollectionUtils.isEmpty(queues)) {
            return toQueues(regionId, aws, queues);
        }
        return Collections.emptyList();
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
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsSqs.Queue entity) {
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