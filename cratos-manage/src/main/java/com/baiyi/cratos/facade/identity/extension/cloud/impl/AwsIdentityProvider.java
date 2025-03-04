package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
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
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_IAM_USER)
public class AwsIdentityProvider extends BaseCloudIdentityProvider<EdsAwsConfigModel.Aws> {

    private final AwsIamUserRepo iamUserRepo;

    public AwsIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                               EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                               UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                               EdsInstanceProviderHolderBuilder holderBuilder, AwsIamUserRepo iamUserRepo) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
        this.iamUserRepo = iamUserRepo;
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


}
