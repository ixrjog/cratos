package com.baiyi.cratos.facade.identity.extension.cloud.provider;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcIamRepo;
import com.baiyi.cratos.eds.huaweicloud.cloud.util.HwcUserConvertor;
import com.baiyi.cratos.facade.identity.extension.cloud.provider.base.BaseCloudIdentityProvider;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.huaweicloud.sdk.iam.v3.model.KeystoneCreateUserResult;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.HUAWEICLOUD_IAM_POLICIES;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/5 09:48
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER)
public class HwcIdentityProvider extends BaseCloudIdentityProvider<EdsConfigs.Hwc, KeystoneListUsersResult> {

    public final static boolean ENABLE_MFA = true;

    public HwcIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                               EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                               UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                               EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsConfigs.Hwc config, EdsInstance instance, User user,
                                                       String password) {
        try {
            KeystoneCreateUserResult createUserResult = HwcIamRepo.createUser(config.getRegionId(), config, user,
                    password);
            EdsInstanceProviderHolder<EdsConfigs.Hwc, KeystoneListUsersResult> holder = (EdsInstanceProviderHolder<EdsConfigs.Hwc, KeystoneListUsersResult>) holderBuilder.newHolder(
                    instance.getId(), getAccountAssetType());
            KeystoneListUsersResult iamUser = HwcUserConvertor.to(createUserResult);
            postImportAccountAsset(holder, iamUser);
            return this.getAccount(instance, user, user.getUsername());
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

    @Override
    public String getPolicyIndexName(EdsAsset asset) {
        return HUAWEICLOUD_IAM_POLICIES;
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username) {
        EdsConfigs.Hwc hwc = (EdsConfigs.Hwc) holderBuilder.newHolder(asset.getInstanceId(),
                        getAccountAssetType())
                .getInstance()
                .getConfig();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(username)
                .name(asset.getName())
                .loginUsername(username)
                .loginUrl(hwc.getIam()
                        .toLoginUrl(hwc.getCred()
                                .getUsername()))
                .build();
    }

    @Override
    public void blockCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        EdsConfigs.Hwc hwc = (EdsConfigs.Hwc) holderBuilder.newHolder(blockCloudAccount.getInstanceId(),
                        getAccountAssetType())
                .getInstance()
                .getConfig();
        HwcIamRepo.blockUser(hwc.getRegionId(), hwc, blockCloudAccount.getAccountId());
    }

    @Override
    public void importCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        // TODO
    }

}

