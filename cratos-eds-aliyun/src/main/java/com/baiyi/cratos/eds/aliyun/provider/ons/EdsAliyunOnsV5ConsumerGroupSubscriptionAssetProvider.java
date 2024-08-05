package com.baiyi.cratos.eds.aliyun.provider.ons;

import com.aliyun.rocketmq20220801.models.ListConsumerGroupSubscriptionsResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.client.AliyunOnsClient;
import com.baiyi.cratos.eds.aliyun.model.AliyunOnsV5;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOnsV5Repo;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 下午2:10
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ONS_V5_CONSUMER_GROUP_SUBSCRIPTION)
public class EdsAliyunOnsV5ConsumerGroupSubscriptionAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunOnsV5.ConsumerGroupSubscription> {

    private final EdsAssetIndexService edsAssetIndexService;

    public EdsAliyunOnsV5ConsumerGroupSubscriptionAssetProvider(EdsAssetService edsAssetService,
                                                                SimpleEdsFacade simpleEdsFacade,
                                                                CredentialService credentialService,
                                                                ConfigCredTemplate configCredTemplate,
                                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                                UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                                EdsAssetIndexService edsAssetIndexService) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected Set<String> listEndpoints(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        return Sets.newHashSet(Optional.of(instance)
                .map(ExternalDataSourceInstance::getEdsConfigModel)
                .map(EdsAliyunConfigModel.Aliyun::getOns)
                .map(EdsAliyunConfigModel.ONS::getV5)
                .map(EdsAliyunConfigModel.RocketMQ::getEndpoints)
                .orElse(Collections.emptyList()));
    }

    @Override
    protected List<AliyunOnsV5.ConsumerGroupSubscription> listEntities(String endpoint,
                                                                       ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<AliyunOnsV5.ConsumerGroupSubscription> results = Lists.newArrayList();
        try {
            List<EdsAsset> edsAssetsOnsConsumerGroups = queryByInstanceAssets(instance,
                    EdsAssetTypeEnum.ALIYUN_ONS_V5_CONSUMER_GROUP);
            if (CollectionUtils.isEmpty(edsAssetsOnsConsumerGroups)) {
                return Collections.emptyList();
            } else {
                List<EdsAsset> endpointConsumerGroups = edsAssetsOnsConsumerGroups.stream()
                        .filter(e -> endpoint.equals(AliyunOnsClient.toEndpoint(e.getRegion())))
                        .toList();
                if (!CollectionUtils.isEmpty(edsAssetsOnsConsumerGroups)) {
                    for (EdsAsset endpointConsumerGroup : endpointConsumerGroups) {
                        EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                                .instanceId(endpointConsumerGroup.getInstanceId())
                                .assetId(endpointConsumerGroup.getId())
                                .name(ALIYUN_ONS_INSTANCE_ID)
                                .build();
                        EdsAssetIndex onsInstanceIndex = edsAssetIndexService.getByUniqueKey(uniqueKey);
                        if (onsInstanceIndex == null) {
                            continue;
                        }
                        final String instanceId = onsInstanceIndex.getValue();
                        final String consumerGroupId = endpointConsumerGroup.getAssetId();
                        List<ListConsumerGroupSubscriptionsResponseBody.ListConsumerGroupSubscriptionsResponseBodyData> consumerGroupSubscriptions = AliyunOnsV5Repo.listConsumerGroupSubscriptions(
                                endpoint, instance.getEdsConfigModel(), instanceId, consumerGroupId);
                        if (!CollectionUtils.isEmpty(consumerGroupSubscriptions)) {
                            results.addAll(consumerGroupSubscriptions.stream()
                                    .map(e -> AliyunOnsV5.ConsumerGroupSubscription.builder()
                                            .instanceId(instanceId)
                                            .endpoint(endpoint)
                                            .consumerGroupSubscription(e)
                                            .build())
                                    .toList());
                        }
                    }
                }
            }
            return results;
        } catch (Exception ex) {
            throw new EdsQueryEntitiesException(ex.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunOnsV5.ConsumerGroupSubscription entity) {
        try {
            // instanceId:consumerGroupId:topicName
            final String key = Joiner.on(":")
                    .join(entity.getInstanceId(), entity.getConsumerGroupSubscription()
                            .getConsumerGroupId(), entity.getConsumerGroupSubscription()
                            .getTopicName());
            return newEdsAssetBuilder(instance, entity).assetIdOf(key)
                    .assetKeyOf(key)
                    .regionOf(entity.getEndpoint())
                    .kindOf(entity.getConsumerGroupSubscription()
                            .getMessageModel())
                    .statusOf(entity.getConsumerGroupSubscription()
                            .getSubscriptionStatus())
                    .build();
        } catch (Exception ex) {
            throw new EdsAssetConversionException(ex.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, AliyunOnsV5.ConsumerGroupSubscription entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            indices.add(toEdsAssetIndex(edsAsset, ALIYUN_ONS_INSTANCE_ID, entity.getInstanceId()));
            indices.add(toEdsAssetIndex(edsAsset, ALIYUN_ONS_CONSUMER_GROUP_ID, entity.getConsumerGroupSubscription()
                    .getConsumerGroupId()));
            indices.add(toEdsAssetIndex(edsAsset, ALIYUN_ONS_TOPIC_NAME, entity.getConsumerGroupSubscription()
                    .getTopicName()));
        } catch (Exception ignored) {
        }
        return indices;
    }

}
