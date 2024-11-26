package com.baiyi.cratos.eds.aws.provider.sxs;

import com.amazonaws.services.sns.model.Subscription;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.model.AwsSns;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.aws.repo.AwsSnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午2:27
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_SNS_SUBSCRIPTION)
public class EdsAwsSnsSubscriptionAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAwsConfigModel.Aws, AwsSns.Subscription> {

    private final AwsSnsRepo awsSnsRepo;

    public EdsAwsSnsSubscriptionAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade, AwsSnsRepo awsSnsRepo,
                                              UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.awsSnsRepo = awsSnsRepo;
    }

    @Override
    protected List<AwsSns.Subscription> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        List<Subscription> subscriptions = awsSnsRepo.listSubscriptions(regionId, aws);
        if (!CollectionUtils.isEmpty(subscriptions)) {
            return toSubscriptions(regionId, aws, subscriptions);
        }
        return Collections.emptyList();
    }

    private List<AwsSns.Subscription> toSubscriptions(String regionId, EdsAwsConfigModel.Aws aws,
                                                      List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(e -> {
                    Map<String, String> attributes = awsSnsRepo.getSubscriptionAttributes(regionId, aws,
                            e.getSubscriptionArn());
                    return AwsSns.Subscription.builder()
                            .regionId(regionId)
                            .attributes(attributes)
                            .subscription(e)
                            .build();
                })
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                  AwsSns.Subscription entity) {
        return newEdsAssetBuilder(instance, entity)
                // ID
                .assetIdOf(StringUtils.substringAfterLast(entity.getSubscription()
                        .getSubscriptionArn(), ":"))
                .nameOf(entity.getSubscription()
                        .getEndpoint())
                .regionOf(entity.getRegionId())
                .kindOf(entity.getSubscription()
                        .getProtocol())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                                      EdsAsset edsAsset, AwsSns.Subscription entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, AWS_SNS_SUBSCRIPTION_ENDPOINT, entity.getSubscription()
                .getEndpoint()));
        indices.add(toEdsAssetIndex(edsAsset, AWS_SNS_SUBSCRIPTION_TOPIC_ARN, entity.getSubscription()
                .getTopicArn()));
        indices.add(toEdsAssetIndex(edsAsset, AWS_SNS_SUBSCRIPTION_PROTOCOL, entity.getSubscription().getProtocol()));
        return indices;
    }

}