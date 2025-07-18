package com.baiyi.cratos.eds.aliyun.provider.ons;

import com.aliyun.rocketmq20220801.models.ListConsumerGroupsResponseBody;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOnsV5Repo;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ONS_INSTANCE_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 上午11:45
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ONS_V5_CONSUMER_GROUP)
public class EdsAliyunOnsV5ConsumerGroupAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> {

    public EdsAliyunOnsV5ConsumerGroupAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                    CredentialService credentialService,
                                                    ConfigCredTemplate configCredTemplate,
                                                    EdsAssetIndexFacade edsAssetIndexFacade,
                                                    UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                    EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
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
    protected List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> listEntities(String endpoint,
                                                                                                       ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> results = Lists.newArrayList();
        try {
            List<EdsAsset> edsAssetsOnsInstances = queryAssetsByInstanceAndType(instance,
                    EdsAssetTypeEnum.ALIYUN_ONS_V5_INSTANCE);
            if (CollectionUtils.isEmpty(edsAssetsOnsInstances)) {
                return Collections.emptyList();
            } else {
                for (EdsAsset edsAssetsOnsInstance : edsAssetsOnsInstances) {
                    List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> consumerGroups = AliyunOnsV5Repo.listConsumerGroups(
                            endpoint, instance.getEdsConfigModel(), edsAssetsOnsInstance.getAssetId());
                    if (!CollectionUtils.isEmpty(consumerGroups)) {
                        results.addAll(consumerGroups);
                    }
                }
            }
            return results;
        } catch (Exception ex) {
            throw new EdsQueryEntitiesException(ex.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList entity) {
        try {
            final String key = Joiner.on(":")
                    .join(entity.getInstanceId(), entity.getConsumerGroupId());
            return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getConsumerGroupId())
                    .nameOf(entity.getConsumerGroupId())
                    .assetKeyOf(key)
                    .regionOf(entity.getRegionId())
                    .createdTimeOf(TimeUtils.strToDate(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                    .descriptionOf(entity.getRemark())
                    .statusOf(entity.getStatus())
                    .build();
        } catch (Exception ex) {
            throw new EdsAssetConversionException(ex.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset,
                                                      ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_ONS_INSTANCE_ID, entity.getInstanceId()));
        } catch (Exception ignored) {
        }
        return indices;
    }

}