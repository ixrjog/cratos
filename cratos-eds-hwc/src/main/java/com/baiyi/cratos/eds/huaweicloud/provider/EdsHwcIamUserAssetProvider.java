package com.baiyi.cratos.eds.huaweicloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.repo.HwcIamRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午2:16
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER)
public class EdsHwcIamUserAssetProvider extends BaseEdsInstanceAssetProvider<EdsHwcConfigModel.Hwc, KeystoneListUsersResult> {

    public EdsHwcIamUserAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade,
                                      UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                      EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<KeystoneListUsersResult> listEntities(
            ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance) throws EdsQueryEntitiesException {
        EdsHwcConfigModel.Hwc huaweicloud = instance.getEdsConfigModel();
        try {
            return HwcIamRepo.listUsers(huaweicloud);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                  KeystoneListUsersResult entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getName())
                .validOf(entity.getEnabled())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance, EdsAsset edsAsset,
            KeystoneListUsersResult entity) {
        return List.of(toEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getName()));
    }

    // TODO role-assignments https://support.huaweicloud.com/intl/zh-cn/api-iam/iam_10_0014.html

}
