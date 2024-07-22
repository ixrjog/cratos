package com.baiyi.cratos.eds.aliyun.provider.acr;

import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAcrRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/19 上午10:31
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ACR_INSTANCE)
public class EdsAliyunAcrInstanceAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, ListInstanceResponse.InstancesItem> {

    private final AliyunAcrRepo aliyunAcrRepo;

    public EdsAliyunAcrInstanceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade, AliyunAcrRepo aliyunAcrRepo,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.aliyunAcrRepo = aliyunAcrRepo;
    }

    @Override
    protected List<ListInstanceResponse.InstancesItem> listEntities(String regionId,
                                                                    EdsAliyunConfigModel.Aliyun configModel) throws EdsQueryEntitiesException {
        try {
            return aliyunAcrRepo.listInstance(regionId, configModel);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListInstanceResponse.InstancesItem entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getInstanceId())
                .nameOf(entity.getInstanceName())
                .regionOf(entity.getRegionId())
                .statusOf(entity.getInstanceStatus())
                .createdTimeOf(toUtcDate(entity.getCreateTime()))
                .build();
    }

    @Override
    protected Set<String> getRegionSet(EdsAliyunConfigModel.Aliyun configModel) {
        return Sets.newHashSet(  Optional.of(configModel)
             .map(EdsAliyunConfigModel.Aliyun::getAcr)
             .map(EdsAliyunConfigModel.ACR::getRegionIds)
             .orElse(Collections.emptyList()));
    }

}
