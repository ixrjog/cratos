package com.baiyi.cratos.eds.aliyun.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRedisRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 上午10:56
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_REDIS)
public class EdsAliyunRedisAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aliyun, com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> {

    private final AliyunRedisRepo aliyunRedisRepo;

    public EdsAliyunRedisAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder,
                                       AliyunRedisRepo aliyunRedisRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunRedisRepo = aliyunRedisRepo;
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected List<com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance> listEntities(
            String regionId, EdsConfigs.Aliyun configModel) {
        try {
            return aliyunRedisRepo.listInstance(regionId, configModel);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                  com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getInstanceId())
                .nameOf(entity.getInstanceName())
                .kindOf(entity.getInstanceType())
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getZoneId())
                .createdTimeOf(toUtcDate(entity.getCreateTime()))
                .expiredTimeOf(toUtcDate(entity.getEndTime()))
                .descriptionOf(StringFormatter.format("Redis {}", entity.getEngineVersion()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                            EdsAsset edsAsset,
                                            com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse.KVStoreInstance entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_REDIS_PRIVATE_IP, entity.getPrivateIp()));
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_REDIS_INSTANCE_CLASS, entity.getInstanceClass()));
            indices.add(createEdsAssetIndex(edsAsset, ALIYUN_REDIS_CONNECTION_DOMAIN, entity.getConnectionDomain()));
        } catch (Exception ignored) {
        }
        return indices;
    }

}