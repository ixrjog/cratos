package com.baiyi.cratos.eds.aliyun.provider.rds;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRdsInstanceRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/6 上午9:59
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RDS_INSTANCE)
public class EdsAliyunRdsInstanceAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, DescribeDBInstanceAttributeResponse.DBInstanceAttribute> {

    private final AliyunRdsInstanceRepo aliyunRdsInstanceRepo;

    public EdsAliyunRdsInstanceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder,
                                             AliyunRdsInstanceRepo aliyunRdsInstanceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunRdsInstanceRepo = aliyunRdsInstanceRepo;
    }

    @Override
    protected List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> listEntities(String regionId,
                                                                                         EdsAliyunConfigModel.Aliyun configModel) throws EdsQueryEntitiesException {
        try {
            return aliyunRdsInstanceRepo.listDbInstance(regionId, configModel);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  DescribeDBInstanceAttributeResponse.DBInstanceAttribute entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDBInstanceId())
                .nameOf(entity.getDBInstanceDescription())
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getZoneId())
                .createdTimeOf(toUtcDate(entity.getCreationTime()))
                .expiredTimeOf(toUtcDate(entity.getExpireTime()))
                // MySQL 8.0
                .descriptionOf(StringFormatter.arrayFormat("{} {}", entity.getEngine(), entity.getEngineVersion()))
                .build();
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

}