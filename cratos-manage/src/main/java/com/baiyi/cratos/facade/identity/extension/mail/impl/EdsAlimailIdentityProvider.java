package com.baiyi.cratos.facade.identity.extension.mail.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.repo.AlimailUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.facade.identity.extension.context.MailIdentityProviderContext;
import com.baiyi.cratos.facade.identity.extension.mail.BaseMailIdentityProvider;
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
public class EdsAlimailIdentityProvider extends BaseMailIdentityProvider<EdsConfigs.Alimail, AlimailUser.User> {

    public EdsAlimailIdentityProvider(MailIdentityProviderContext context) {
        super(context);
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username, String mail) {
        EdsConfigs.Alimail alimail = (EdsConfigs.Alimail) context.getEdsProviderHolderFactory()
                .createHolder(asset.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getConfig();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(username)
                .name(asset.getName())
                .loginUsername(mail)
                .loginUrl(alimail.getLoginUrl())
                .build();
    }

    @Override
    public void blockMailAccount(EdsIdentityParam.BlockMailAccount blockMailAccount) {
        EdsConfigs.Alimail alimail = (EdsConfigs.Alimail) context.getEdsProviderHolderFactory()
                .createHolder(blockMailAccount.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getConfig();
        AlimailUserRepo.freezeUser(alimail, blockMailAccount.getUserId());
    }

}
