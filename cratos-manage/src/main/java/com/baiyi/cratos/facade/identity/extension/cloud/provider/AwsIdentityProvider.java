package com.baiyi.cratos.facade.identity.extension.cloud.provider;

import com.amazonaws.services.identitymanagement.model.LoginProfile;
import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamPolicyRepo;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.extension.cloud.provider.base.BaseCloudIdentityProvider;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.AWS_IAM_POLICIES;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:17
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_IAM_USER)
public class AwsIdentityProvider extends BaseCloudIdentityProvider<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> {

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
    protected EdsIdentityVO.CloudAccount createAccount(EdsAwsConfigModel.Aws aws, EdsInstance instance, User user,
                                                       String password) {
        iamUserRepo.createUser(aws, user, password, CREATE_LOGIN_PROFILE);
        return this.getAccount(instance, user, user.getUsername());
    }

    @Override
    public EdsIdentityVO.CloudAccount getAccount(EdsInstance instance, User user, String username) {
        try {
            EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) holderBuilder.newHolder(
                    instance.getId(), getAccountAssetType());
            com.amazonaws.services.identitymanagement.model.User iamUser = iamUserRepo.getUser(holder.getInstance()
                    .getEdsConfigModel(), username);
            if (Objects.isNull(iamUser)) {
                return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
            }
            return super.getAccount(instance, user, username);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    @Override
    public void blockCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        LoginProfile loginProfile = iamUserRepo.getLoginProfile(aws, blockCloudAccount.getAccount());
        if (Objects.nonNull(loginProfile)) {
            iamUserRepo.deleteLoginProfile(aws, blockCloudAccount.getAccount());
        }
    }

    @Override
    public void importCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        com.amazonaws.services.identitymanagement.model.User user = iamUserRepo.getUser(aws,
                blockCloudAccount.getAccount());
        holder.importAsset(user);
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
            postImportAccountAsset(holder, iamUser);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
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
            postImportAccountAsset(holder, iamUser);
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    @Override
    public String getPolicyIndexName(EdsAsset asset) {
        return AWS_IAM_POLICIES;
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username) {
        EdsAwsConfigModel.Aws aws = (EdsAwsConfigModel.Aws) holderBuilder.newHolder(asset.getInstanceId(),
                        getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(username)
                .name(asset.getName())
                .accountId(aws.getCred()
                        .getId())
                .loginUsername(username)
                .loginUrl(aws.getIam()
                        .toLoginUrl())
                .build();
    }

}
