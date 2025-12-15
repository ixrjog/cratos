package com.baiyi.cratos.eds.aliyun.provider.rds;

import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRdsDatabaseRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/6 上午11:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RDS_DATABASE)
public class EdsAliyunRdsDatabaseAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Aliyun, DescribeDatabasesResponse.Database> {

    private final AliyunRdsDatabaseRepo aliyunRdsDatabaseRepo;

    public EdsAliyunRdsDatabaseAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder,
                                             AliyunRdsDatabaseRepo aliyunRdsDatabaseRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunRdsDatabaseRepo = aliyunRdsDatabaseRepo;
    }

    @Override
    protected List<DescribeDatabasesResponse.Database> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<EdsAsset> assets = queryAssetsByInstanceAndType(instance, EdsAssetTypeEnum.ALIYUN_RDS_INSTANCE);
            if (CollectionUtils.isEmpty(assets)) {
                return List.of();
            }
            List<DescribeDatabasesResponse.Database> entities = Lists.newArrayList();
            for (EdsAsset asset : assets) {
                List<DescribeDatabasesResponse.Database> dbs = aliyunRdsDatabaseRepo.listDatabase(asset.getRegion(),
                                                                                                  instance.getConfig(), asset.getAssetId());
                if (!CollectionUtils.isEmpty(dbs)) {
                    entities.addAll(dbs);
                }
            }
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                  DescribeDatabasesResponse.Database entity) {
        final String key = Joiner.on(":")
                .join(entity.getDBInstanceId(), entity.getDBName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDBInstanceId())
                .nameOf(entity.getDBName())
                .assetKeyOf(key)
                .build();
    }

}