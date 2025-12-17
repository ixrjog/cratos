package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDdsMongoRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.domain.constant.Global.ISO8601;
import static com.baiyi.cratos.domain.constant.Global.ISO8601_1;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/2 上午11:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DDS_MONGO_INSTANCE)
public class EdsAliyunDdsMongoInstanceAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsConfigs.Aliyun, DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> {

    public EdsAliyunDdsMongoInstanceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                  CredentialService credentialService,
                                                  ConfigCredTemplate configCredTemplate,
                                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                                  AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected Set<String> listEndpoints(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        return Sets.newHashSet(Optional.of(instance)
                .map(ExternalDataSourceInstance::getConfig)
                .map(EdsConfigs.Aliyun::getMongoDB)
                .map(EdsAliyunConfigModel.MongoDB::getEndpoints)
                .orElse(Collections.emptyList()));
    }

    @Override
    protected List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> listEntities(
            String endpoint,
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunDdsMongoRepo.describeDBInstances(endpoint, instance.getConfig());
        } catch (Exception ex) {
            throw new EdsQueryEntitiesException(ex.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                  DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance entity) {
        try {
            return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDBInstanceId())
                    .nameOf(entity.getDBInstanceDescription())
                    .regionOf(entity.getRegionId())
                    .zoneOf(entity.getZoneId())
                    .createdTimeOf(TimeUtils.strToDate(entity.getCreationTime(), ISO8601))
                    .expiredTimeOf(TimeUtils.strToDate(entity.getExpireTime(), ISO8601_1))
                    .build();
        } catch (Exception ex) {
            throw new EdsAssetConversionException(ex.getMessage());
        }
    }

}