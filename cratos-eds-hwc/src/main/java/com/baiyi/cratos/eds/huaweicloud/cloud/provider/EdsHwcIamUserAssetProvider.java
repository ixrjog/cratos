package com.baiyi.cratos.eds.huaweicloud.cloud.provider;

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
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcIamRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.iam.v3.model.Credentials;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCESS_KEY_IDS;
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
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                  KeystoneListUsersResult entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getName())
                .validOf(entity.getEnabled())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                                      EdsAsset edsAsset, KeystoneListUsersResult entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getName()));
        // accessKeys
        try {
            List<Credentials> accessKeys = HwcIamRepo.listAccessKeys(instance.getEdsConfigModel()
                    .getRegionId(), instance.getEdsConfigModel(), entity.getId());
            if (!CollectionUtils.isEmpty(accessKeys)) {
                final String accessKeyIds = accessKeys.stream()
                        .map(Credentials::getAccess)
                        .collect(Collectors.joining(INDEX_VALUE_DIVISION_SYMBOL));
                indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCESS_KEY_IDS, accessKeyIds));
            }
        } catch (Exception ignored) {
        }
        return indices;
    }

    // TODO role-assignments https://support.huaweicloud.com/intl/zh-cn/api-iam/iam_10_0014.html

}
