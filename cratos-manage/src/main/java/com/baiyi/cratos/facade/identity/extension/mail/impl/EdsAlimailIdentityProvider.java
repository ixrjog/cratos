package com.baiyi.cratos.facade.identity.extension.mail.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.repo.AlimailUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.extension.mail.BaseMailIdentityProvider;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:37
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIMAIL, assetTypeOf = EdsAssetTypeEnum.ALIMAIL_USER)
public class EdsAlimailIdentityProvider extends BaseMailIdentityProvider<EdsAlimailConfigModel.Alimail, AlimailUser.User> {

    private final AlimailUserRepo alimailUserRepo;

    public EdsAlimailIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                      EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                                      UserService userService, UserWrapper userWrapper,
                                      EdsInstanceWrapper instanceWrapper,
                                      EdsInstanceProviderHolderBuilder holderBuilder, AlimailUserRepo alimailUserRepo) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
        this.alimailUserRepo = alimailUserRepo;
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username, String mail) {
        EdsAlimailConfigModel.Alimail alimail = (EdsAlimailConfigModel.Alimail) holderBuilder.newHolder(
                        asset.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(username)
                .name(asset.getName())
                .loginUsername(mail)
                .loginUrl(alimail.getLoginUrl())
                .build();
    }

    @Override
    public void blockMailAccount(EdsIdentityParam.BlockMailAccount blockMailAccount) {
        EdsAlimailConfigModel.Alimail alimail = (EdsAlimailConfigModel.Alimail) holderBuilder.newHolder(
                        blockMailAccount.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        alimailUserRepo.freezeUser(alimail, blockMailAccount.getUserId());
    }

}
