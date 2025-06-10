package com.baiyi.cratos.eds.aliyun.provider.dms;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.model.AliyunDms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDmsRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 11:20
 * &#064;Version 1.0
 */
@Slf4j
//@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DMS_USER)
public class EdsAliyunDmsUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunDms.User> {

    public EdsAliyunDmsUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                         EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<AliyunDms.User> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunDmsRepo.listUser(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunDms.User entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getUserId())
                .nameOf(entity.getNickName())
                .assetKeyOf(entity.getUid())
                .build();
    }

}