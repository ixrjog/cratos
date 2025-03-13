package com.baiyi.cratos.facade.identity.extension.mail;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:42
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseMailIdentityProvider<Config extends IEdsConfigModel, Account> implements MailIdentityProvider, InitializingBean {

    private final EdsInstanceService edsInstanceService;
    protected final EdsAssetService edsAssetService;
    protected final EdsAssetWrapper edsAssetWrapper;
    protected final EdsAssetIndexService edsAssetIndexService;
    private final UserService userService;
    protected final UserWrapper userWrapper;
    protected final EdsInstanceWrapper instanceWrapper;
    protected final EdsInstanceProviderHolderBuilder holderBuilder;

    protected EdsAsset queryAccountAsset(int instanceId, User user) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryInstanceIndexByNameAndValue(instanceId, USER_MAIL,
                user.getEmail());
        if (CollectionUtils.isEmpty(indices)) {
            return null;
        }
        return edsAssetService.getById(indices.getFirst()
                .getAssetId());
    }

    @Override
    public EdsIdentityVO.MailAccount getAccount(EdsInstance instance, User user) {
        try {
            EdsAsset account = queryAccountAsset(instance.getId(), user);
            if (Objects.isNull(account)) {
                return EdsIdentityVO.MailAccount.NO_ACCOUNT;
            }
            EdsAssetIndex uk = EdsAssetIndex.builder()
                    .instanceId(instance.getId())
                    .assetId(account.getId())
                    .name(USER_MAIL)
                    .build();
            EdsAssetIndex mailIndex = edsAssetIndexService.getByUniqueKey(uk);
            if (Objects.isNull(mailIndex)) {
                return EdsIdentityVO.MailAccount.NO_ACCOUNT;
            }
            return EdsIdentityVO.MailAccount.builder()
                    .instance(instanceWrapper.wrapToTarget(instance))
                    .user(userWrapper.wrapToTarget(user))
                    .account(edsAssetWrapper.wrapToTarget(account))
                    .username(user.getUsername())
                    .password("******")
                    .accountLogin(toAccountLoginDetails(account, user.getUsername(), mailIndex.getValue()))
                    .build();
        } catch (Exception ex) {
            throw new CloudIdentityException(ex.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        MailIdentityFactory.register(this);
    }

}
