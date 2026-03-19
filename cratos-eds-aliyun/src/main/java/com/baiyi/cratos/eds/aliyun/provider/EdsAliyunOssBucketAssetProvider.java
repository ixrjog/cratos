package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.oss.model.Bucket;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOssRepo;
import com.baiyi.cratos.eds.core.BaseHasEndpointsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/19 下午3:29
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_OSS_BUCKET)
public class EdsAliyunOssBucketAssetProvider extends BaseHasEndpointsEdsAssetProvider<EdsConfigs.Aliyun, Bucket> {

    public EdsAliyunOssBucketAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected Set<String> listEndpoints(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        return Sets.newHashSet(Optional.of(instance)
                .map(ExternalDataSourceInstance::getConfig)
                .map(EdsConfigs.Aliyun::getOss)
                .map(EdsAliyunConfigModel.OSS::getEndpoints)
                .orElse(List.of()));
    }

    @Override
    protected List<Bucket> listEntities(String endpoint,
                                        ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        return AliyunOssRepo.listBuckets(endpoint, instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance, Bucket entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getName())
                .nameOf(entity.getName())
                .assetKeyOf(Joiner.on(".")
                        .join(entity.getName(), entity.getExtranetEndpoint()))
                .regionOf(entity.getRegion())
                .createdTimeOf(entity.getCreationDate())
                .build();
    }

}