package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamPolicyRepo;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.extension.cloud.BaseCloudIdentityProvider;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:17
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_IAM_USER)
public class AwsIdentityProvider extends BaseCloudIdentityProvider<EdsAwsConfigModel.Aws> {

    private final AwsIamUserRepo iamUserRepo;
    private final AwsIamPolicyRepo iamPolicyRepo;

    public AwsIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                               EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                               UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                               EdsInstanceProviderHolderBuilder holderBuilder, AwsIamUserRepo iamUserRepo,
                               AwsIamPolicyRepo iamPolicyRepo) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
        this.iamUserRepo = iamUserRepo;
        this.iamPolicyRepo = iamPolicyRepo;
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsAwsConfigModel.Aws aws, EdsInstance instance, User user) {
        iamUserRepo.createUser(aws, user, CREATE_LOGIN_PROFILE);
        return this.getAccount(aws, instance, user);
    }

    @Override
    protected EdsIdentityVO.CloudAccount getAccount(EdsAwsConfigModel.Aws aws, EdsInstance instance, User user) {
        try {
            com.amazonaws.services.identitymanagement.model.User iamUser = iamUserRepo.getUser(aws, user.getUsername());
            if (Objects.isNull(iamUser)) {
                return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
            }
            EdsAsset account = getAccountAsset(instance.getId(), user.getUsername());
            return EdsIdentityVO.CloudAccount.builder()
                    .instance(instanceWrapper.wrapToTarget(instance))
                    .user(userWrapper.wrapToTarget(user))
                    .account(Objects.isNull(account) ? null : edsAssetWrapper.wrapToTarget(account))
                    .username(user.getUsername())
                    .password("******")
                    .build();
        } catch (Exception ex) {
            throw new CloudIdentityException(ex.getMessage());
        }
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        final String iamUserName = account.getName();
        final String iamPolicyArn = permission.getAssetKey();
        try {
            iamPolicyRepo.attachUserPolicy(aws, iamUserName, iamPolicyArn);
            com.amazonaws.services.identitymanagement.model.User iamUser = iamUserRepo.getUser(aws, iamUserName);
            postImportIamUser(holder, iamUser);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    private void postImportIamUser(
            EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder,
            com.amazonaws.services.identitymanagement.model.User iamUser) {
        holder.getProvider()
                .importAsset(holder.getInstance(), iamUser);
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        final String iamUserName = account.getName();
        final String iamPolicyArn = permission.getAssetKey();
        try {
            iamPolicyRepo.detachUserPolicy(aws, iamUserName, iamPolicyArn);
            com.amazonaws.services.identitymanagement.model.User iamUser = iamUserRepo.getUser(aws, iamUserName);
            postImportIamUser(holder, iamUser);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

}
