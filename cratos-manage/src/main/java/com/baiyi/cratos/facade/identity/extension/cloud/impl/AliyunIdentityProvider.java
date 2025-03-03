package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN , assetTypeOf = EdsAssetTypeEnum.ALIYUN_RAM_USER)
public class AliyunIdentityProvider extends BaseCloudIdentityProvider<EdsAliyunConfigModel.Aliyun> {

    private final AliyunRamUserRepo ramUserRepo;
    public final static boolean ENABLE_MFA = true;

    public AliyunIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                  EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                                  UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                                  EdsInstanceProviderHolderBuilder holderBuilder,AliyunRamUserRepo ramUserRepo) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
        this.ramUserRepo = ramUserRepo;
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsAliyunConfigModel.Aliyun config, EdsInstance instance,
                                                       User user) {
        try {
            CreateUserResponse.User createUser = ramUserRepo.createUser(config.getRegionId(), config, user,
                    CREATE_LOGIN_PROFILE, ENABLE_MFA);
            return this.getAccount(config, instance, user);
        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    protected EdsIdentityVO.CloudAccount getAccount(EdsAliyunConfigModel.Aliyun config, EdsInstance instance,
                                                    User user) {
        try {
            GetUserResponse.User ramUser = ramUserRepo.getUser(config, user.getUsername());
            if (Objects.isNull(ramUser)) {
                return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
            }
            EdsAsset account = getCloudAccountAsset(instance.getId(), user.getUsername());
            return EdsIdentityVO.CloudAccount.builder()
                    .instance(instanceWrapper.wrapToTarget(instance))
                    .user(userWrapper.wrapToTarget(user))
                    .account(Objects.isNull(account) ? null : edsAssetWrapper.wrapToTarget(account))
                    .username(user.getUsername())
                    .password("******")
                    .build();
        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return EdsInstanceTypeEnum.ALIYUN.name();
    }

    @Override
    public void grantPermission(EdsIdentityParam.GrantPermission grantPermission) {

    }

    @Override
    public void revokePermission(EdsIdentityParam.RevokePermission revokePermission) {

    }

}
