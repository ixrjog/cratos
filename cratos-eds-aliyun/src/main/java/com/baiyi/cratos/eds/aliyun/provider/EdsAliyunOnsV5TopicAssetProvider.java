package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.rocketmq20220801.models.ListTopicsResponseBody;
import com.baiyi.cratos.common.util.TimeUtil;
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

import static com.baiyi.cratos.eds.aliyun.provider.EdsAliyunOnsV5InstanceAssetProvider.ONS_INSTANCE_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 上午10:57
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ONS_V5_TOPIC)
public class EdsAliyunOnsV5TopicAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, ListTopicsResponseBody.ListTopicsResponseBodyDataList> {

    public EdsAliyunOnsV5TopicAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
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
    protected List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> listEntities(String endpoint,
                                                                                       ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> results = Lists.newArrayList();
        try {
            List<EdsAsset> edsAssetsOnsInstances = queryByInstanceAssets(instance,
                    EdsAssetTypeEnum.ALIYUN_ONS_V5_INSTANCE);
            if (CollectionUtils.isEmpty(edsAssetsOnsInstances)) {
                return Collections.emptyList();
            } else {
                for (EdsAsset edsAssetsOnsInstance : edsAssetsOnsInstances) {
                    List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> topics = AliyunOnsV5Repo.listTopics(
                            endpoint, instance.getEdsConfigModel(), edsAssetsOnsInstance.getAssetId());
                    if (!CollectionUtils.isEmpty(topics)) {
                        results.addAll(topics);
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
                                  ListTopicsResponseBody.ListTopicsResponseBodyDataList entity) {
        try {
            final String key = Joiner.on(":")
                    .join(entity.getInstanceId(), entity.getTopicName());
            return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getTopicName())
                    .nameOf(entity.getTopicName())
                    .assetKeyOf(key)
                    .kindOf(entity.getMessageType())
                    .regionOf(entity.getRegionId())
                    .createdTimeOf(TimeUtil.strToDate(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                    .descriptionOf(entity.getRemark())
                    .statusOf(entity.getStatus())
                    .build();
        } catch (Exception ex) {
            throw new EdsAssetConversionException(ex.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset,
                                                      ListTopicsResponseBody.ListTopicsResponseBodyDataList entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            indices.add(toEdsAssetIndex(edsAsset, ONS_INSTANCE_ID, entity.getInstanceId()));
        } catch (Exception ignored) {
        }
        return indices;
    }

}