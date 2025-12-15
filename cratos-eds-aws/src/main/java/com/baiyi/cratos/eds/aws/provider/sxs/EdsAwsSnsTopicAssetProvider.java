package com.baiyi.cratos.eds.aws.provider.sxs;

import com.amazonaws.services.sns.model.Topic;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsSns;
import com.baiyi.cratos.eds.aws.repo.AwsSnsRepo;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午2:03
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SNS_TOPIC)
public class EdsAwsSnsTopicAssetProvider extends BaseEdsRegionAssetProvider<EdsConfigs.Aws, AwsSns.Topic> {

    private final AwsSnsRepo awsSnsRepo;

    public EdsAwsSnsTopicAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder, AwsSnsRepo awsSnsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.awsSnsRepo = awsSnsRepo;
    }

    @Override
    protected List<AwsSns.Topic> listEntities(String regionId, EdsConfigs.Aws aws) {
        List<Topic> topics = awsSnsRepo.listTopics(regionId, aws);
        if (!CollectionUtils.isEmpty(topics)) {
            return toTopics(regionId, aws, topics);
        }
        return Collections.emptyList();
    }

    private List<AwsSns.Topic> toTopics(String regionId, EdsConfigs.Aws aws, List<Topic> topics) {
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
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance, AwsSns.Topic entity) {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getTopic()
                        .getTopicArn())
                .nameOf(StringUtils.substringAfterLast(entity.getTopic()
                        .getTopicArn(), "/"))
                .regionOf(entity.getRegionId())
                .build();
    }

}