package com.baiyi.cratos.eds.aliyun.provider.acr;

import com.aliyuncs.cr.model.v20181201.ListNamespaceResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAcrRepo;
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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 上午10:52
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ACR_NAMESPACE)
public class EdsAliyunAcrNamespaceAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, ListNamespaceResponse.NamespacesItem> {

    private final AliyunAcrRepo aliyunAcrRepo;

    public EdsAliyunAcrNamespaceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade, AliyunAcrRepo aliyunAcrRepo,
                                              UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.aliyunAcrRepo = aliyunAcrRepo;
    }

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListNamespaceResponse.NamespacesItem entity) {
        final String key = Joiner.on(":")
                .join(entity.getInstanceId(), entity.getNamespaceName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getNamespaceName())
                .nameOf(entity.getNamespaceName())
                .assetKeyOf(key)
                .statusOf(entity.getNamespaceStatus())
                .build();
    }

    @Override
    protected List<ListNamespaceResponse.NamespacesItem> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<EdsAsset> assets = queryByInstanceAssets(instance, EdsAssetTypeEnum.ALIYUN_ACR_INSTANCE);
            List<ListNamespaceResponse.NamespacesItem> entities = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(assets)) {
                for (EdsAsset asset : assets) {
                    entities.addAll(aliyunAcrRepo.listNamespace(asset.getRegion(), instance.getEdsConfigModel(),
                            asset.getAssetId()));
                }
            }
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

}
