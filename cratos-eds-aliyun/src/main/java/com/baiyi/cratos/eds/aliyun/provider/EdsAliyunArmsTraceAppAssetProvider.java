package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.eds.aliyun.model.AliyunArms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunArmsRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
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
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/1 上午10:56
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ARMS_TRACE_APPS)
public class EdsAliyunArmsTraceAppAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunArms.TraceApps> {

    public EdsAliyunArmsTraceAppAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade,
                                              UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<AliyunArms.TraceApps> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<ListTraceAppsResponseBody.TraceApps> appsList = AliyunArmsRepo.listTraceApps(
                    instance.getEdsConfigModel());
            return BeanCopierUtil.copyListProperties(appsList, AliyunArms.TraceApps.class);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunArms.TraceApps entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getAppId())
                .nameOf(entity.getAppName())
                .assetKeyOf(entity.getPid())
                .regionOf(entity.getRegionId())
                .createdTimeOf(entity.getCreateTime())
                .build();
    }

}