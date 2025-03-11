package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamPolicyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_RAM_POLICIES;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:17
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_RAM_USER)
public class AliyunIdentityProvider extends BaseCloudIdentityProvider<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> {

    private final AliyunRamUserRepo ramUserRepo;
    private final AliyunRamPolicyRepo ramPolicyRepo;
    public final static boolean ENABLE_MFA = true;

    public AliyunIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                  EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                                  UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                                  EdsInstanceProviderHolderBuilder holderBuilder, AliyunRamUserRepo ramUserRepo,
                                  AliyunRamPolicyRepo ramPolicyRepo) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
        this.ramUserRepo = ramUserRepo;
        this.ramPolicyRepo = ramPolicyRepo;
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsAliyunConfigModel.Aliyun config, EdsInstance instance,
                                                       User user, String password) {
        try {
            CreateUserResponse.User createUser = ramUserRepo.createUser(config.getRegionId(), config, user, password,
                    CREATE_LOGIN_PROFILE, ENABLE_MFA);
            GetUserResponse.User ramUser = ramUserRepo.getUser(config, user.getUsername());
            EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) holderBuilder.newHolder(
                    instance.getId(), getAccountAssetType());
            postImportAccountAsset(holder, ramUser);
            return this.getAccount(instance, user, user.getUsername());
        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        final String ramUsername = account.getAssetKey();
        final String policyName = permission.getAssetKey();
        final String policyType = permission.getKind();
        try {
            ramPolicyRepo.attachPolicyToUser(aliyun.getRegionId(), aliyun, ramUsername, policyName, policyType);
            GetUserResponse.User ramUser = ramUserRepo.getUser(aliyun, ramUsername);
            postImportAccountAsset(holder, ramUser);
        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        final String ramUsername = account.getAssetKey();
        final String policyName = permission.getAssetKey();
        final String policyType = permission.getKind();
        try {
            ramPolicyRepo.detachPolicyFromUser(aliyun.getRegionId(), aliyun, ramUsername, policyName, policyType);
            GetUserResponse.User ramUser = ramUserRepo.getUser(aliyun, ramUsername);
            postImportAccountAsset(holder, ramUser);
        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    public String getPolicyIndexName(EdsAsset asset) {
        return ALIYUN_RAM_POLICIES;
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username) {
        EdsAliyunConfigModel.Aliyun aliyun = (EdsAliyunConfigModel.Aliyun) holderBuilder.newHolder(
                        asset.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(username)
                .name(asset.getName())
                .loginUsername(aliyun.getRam()
                        .toUsername(username))
                .loginUrl(aliyun.getRam()
                        .toLoginUrl())
                .build();
    }

}
