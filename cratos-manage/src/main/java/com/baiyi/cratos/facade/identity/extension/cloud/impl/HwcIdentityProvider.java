package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.huaweicloud.repo.HwcIamRepo;
import com.baiyi.cratos.eds.huaweicloud.util.HwcUserConvertor;
import com.baiyi.cratos.facade.identity.extension.cloud.BaseCloudIdentityProvider;
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

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/5 09:48
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER)
public class HwcIdentityProvider extends BaseCloudIdentityProvider<EdsHuaweicloudConfigModel.Huaweicloud> {

    public final static boolean ENABLE_MFA = true;

    public HwcIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                               EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                               UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                               EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsHuaweicloudConfigModel.Huaweicloud config,
                                                       EdsInstance instance, User user) {
        try {
            KeystoneCreateUserResult createUserResult = HwcIamRepo.createUser(config.getRegionId(), config, user);
            EdsInstanceProviderHolder<EdsHuaweicloudConfigModel.Huaweicloud, KeystoneListUsersResult> holder = (EdsInstanceProviderHolder<EdsHuaweicloudConfigModel.Huaweicloud, KeystoneListUsersResult>) holderBuilder.newHolder(
                    instance.getId(), getAccountAssetType());
            KeystoneListUsersResult iamUser = HwcUserConvertor.to(createUserResult);
            postImportIamUser(holder, iamUser);
            return this.getAccount(config, instance, user);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    private void postImportIamUser(
            EdsInstanceProviderHolder<EdsHuaweicloudConfigModel.Huaweicloud, KeystoneListUsersResult> holder,
            KeystoneListUsersResult iamUser) {
        holder.getProvider()
                .importAsset(holder.getInstance(), iamUser);
    }

    @Override
    protected EdsIdentityVO.CloudAccount getAccount(EdsHuaweicloudConfigModel.Huaweicloud config, EdsInstance instance,
                                                    User user) {
        EdsAsset account = getAccountAsset(instance.getId(), user.getUsername());
        if (Objects.isNull(account)) {
            return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
        }
        return EdsIdentityVO.CloudAccount.builder()
                .instance(instanceWrapper.wrapToTarget(instance))
                .user(userWrapper.wrapToTarget(user))
                .account(edsAssetWrapper.wrapToTarget(account))
                .username(user.getUsername())
                .password("******")
                .build();
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

}

