package com.baiyi.cratos.eds.aliyun.provider.ons;

import com.aliyun.rocketmq20220801.models.GetInstanceResponse;
import com.aliyun.rocketmq20220801.models.ListInstancesResponseBody;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ONS_INSTANCE_INTERNET_ENDPOINT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ONS_INSTANCE_VPC_ENDPOINT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 上午10:09
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ONS_V5_INSTANCE)
public class EdsAliyunOnsV5InstanceAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, ListInstancesResponseBody.ListInstancesResponseBodyDataList> {

    public EdsAliyunOnsV5InstanceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
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
    protected List<ListInstancesResponseBody.ListInstancesResponseBodyDataList> listEntities(String endpoint,
                                                                                             ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunOnsV5Repo.listInstances(endpoint, instance.getEdsConfigModel());
        } catch (Exception ex) {
            throw new EdsQueryEntitiesException(ex.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                         ListInstancesResponseBody.ListInstancesResponseBodyDataList entity) {
        try {
            return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getInstanceId())
                    .nameOf(entity.getInstanceName())
                    .regionOf(entity.getRegionId())
                    .createdTimeOf(TimeUtils.strToDate(entity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                    .expiredTimeOf(TimeUtils.strToDate(entity.getExpireTime(), "yyyy-MM-dd HH:mm:ss"))
                    .descriptionOf(entity.getRemark())
                    .build();
        } catch (Exception ex) {
            throw new EdsAssetConversionException(ex.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
            EdsAsset edsAsset,
            ListInstancesResponseBody.ListInstancesResponseBodyDataList entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            GetInstanceResponse instanceResponse = AliyunOnsV5Repo.getInstance(entity.getRegionId(),
                    instance.getEdsConfigModel(), entity.getInstanceId());
            instanceResponse.getBody().getData().getNetworkInfo().endpoints.forEach(endpoint -> {

                if ("TCP_VPC".equals(endpoint.getEndpointType())) {
                    indices.add(
                            createEdsAssetIndex(edsAsset, ALIYUN_ONS_INSTANCE_VPC_ENDPOINT, endpoint.getEndpointUrl()));
                }

                if ("TCP_INTERNET".equals(endpoint.getEndpointType())) {
                    indices.add(createEdsAssetIndex(edsAsset, ALIYUN_ONS_INSTANCE_INTERNET_ENDPOINT,
                            endpoint.getEndpointUrl()));
                }
            });
        } catch (Exception ignored) {
        }
        return indices;
    }

}