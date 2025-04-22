package com.baiyi.cratos.facade.identity.extension.mail;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:35
 * &#064;Version 1.0
 */
public interface MailIdentityProvider extends EdsAssetTypeOfAnnotate {

    EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username, String mail);

    EdsIdentityVO.MailAccount getAccount(EdsInstance instance, User user);

    default String getAccountAssetType() {
        return getAssetType();
    }

    void blockMailAccount(EdsIdentityParam.BlockMailAccount blockMailAccount);

}