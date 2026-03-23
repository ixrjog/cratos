package com.baiyi.cratos.facade.identity.extension.mail;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.facade.identity.extension.context.MailIdentityProviderContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.eds.core.BaseEdsAssetProvider.INDEX_VALUE_DIVISION_SYMBOL;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL_ALIAS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:42
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseMailIdentityProvider<Config extends HasEdsConfig, Account> implements MailIdentityProvider, InitializingBean {

    protected final MailIdentityProviderContext context;

    protected EdsAsset queryAccountAsset(int instanceId, User user) {
        List<EdsAssetIndex> indices = context.getEdsAssetIndexService().queryInstanceIndexByNameAndValue(
                instanceId, USER_MAIL,
                user.getEmail()
        );
        if (CollectionUtils.isEmpty(indices)) {
            return null;
        }
        return context.getEdsAssetService().getById(indices.getFirst()
                                               .getAssetId());
    }

    @Override
    public EdsIdentityVO.MailAccount getAccount(User user, EdsInstance instance, EdsAsset account) {
        try {
            //EdsAsset account = queryAccountAsset(instance.getId(), user);
            if (Objects.isNull(account)) {
                return EdsIdentityVO.MailAccount.NO_ACCOUNT;
            }
            EdsAssetIndex uk = EdsAssetIndex.builder()
                    .instanceId(instance.getId())
                    .assetId(account.getId())
                    .name(USER_MAIL)
                    .build();
            EdsAssetIndex mailIndex = context.getEdsAssetIndexService().getByUniqueKey(uk);
            if (Objects.isNull(mailIndex)) {
                return EdsIdentityVO.MailAccount.NO_ACCOUNT;
            }
            return EdsIdentityVO.MailAccount.builder()
                    .instance(context.getInstanceWrapper().wrapToTarget(instance))
                    .user(context.getUserWrapper().wrapToTarget(user))
                    .account(context.getEdsAssetWrapper().wrapToTarget(account))
                    .username(user.getUsername())
                    .password("******")
                    .accountLogin(toAccountLoginDetails(account, user.getUsername(), mailIndex.getValue()))
                    .mailAlias(queryMailAlias(instance, account))
                    .build();
        } catch (Exception ex) {
            throw new CloudIdentityException(ex.getMessage());
        }
    }

    private List<String> queryMailAlias(EdsInstance instance, EdsAsset account) {
        EdsAssetIndex uk = EdsAssetIndex.builder()
                .instanceId(instance.getId())
                .assetId(account.getId())
                .name(USER_MAIL_ALIAS)
                .build();
        EdsAssetIndex mailAliasIndex = context.getEdsAssetIndexService().getByUniqueKey(uk);
        return Objects.isNull(mailAliasIndex) ? List.of() : List.of(mailAliasIndex.getValue()
                                                                            .split(INDEX_VALUE_DIVISION_SYMBOL));
    }

    @Override
    public void afterPropertiesSet() {
        MailIdentityFactory.register(this);
    }

}
