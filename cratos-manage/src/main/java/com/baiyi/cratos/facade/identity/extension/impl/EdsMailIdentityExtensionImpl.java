package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsMailIdentityExtension;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.mail.MailIdentityFactory;
import com.baiyi.cratos.facade.identity.extension.mail.MailIdentityProvider;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsMailIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsMailIdentityExtension {

    public EdsMailIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                        EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                        UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                        EdsAssetService edsAssetService, EdsFacade edsFacade,
                                        EdsAssetIndexService edsAssetIndexService, TagService tagService,
                                        BusinessTagService businessTagService) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade, edsAssetIndexService, tagService, businessTagService);
    }

    private static final List<String> MAIL_ACCOUNT_ASSET_TYPES = List.of(EdsAssetTypeEnum.ALIMAIL_USER.name());

    @Override
    public EdsIdentityVO.MailIdentityDetails queryMailIdentityDetails(
            EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails) {
        User user = userService.getByUsername(queryMailIdentityDetails.getUsername());
        List<EdsAsset> cloudIdentityAssets = queryMailAssets(user);
        if (cloudIdentityAssets.isEmpty()) {
            return EdsIdentityVO.MailIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsInstance> instanceMap = getEdsInstanceMap(cloudIdentityAssets, queryMailIdentityDetails);
        Map<String, List<EdsIdentityVO.MailAccount>> accounts = Maps.newHashMap();
        cloudIdentityAssets.stream()
                .filter(asset -> instanceMap.containsKey(asset.getInstanceId()))
                .forEach(asset -> {
                    MailIdentityProvider identityProvider = MailIdentityFactory.getProvider(
                            instanceMap.get(asset.getInstanceId())
                                    .getEdsType());
                    EdsIdentityVO.MailAccount mailAccount = identityProvider.getAccount(user,
                            instanceMap.get(asset.getInstanceId()), asset);
                    putAccounts(accounts, mailAccount);
                });
        return EdsIdentityVO.MailIdentityDetails.builder()
                .username(queryMailIdentityDetails.getUsername())
                .accounts(accounts)
                .build();
    }

    @Override
    public void blockMailAccount(EdsIdentityParam.BlockMailAccount blockMailAccount) {
        EdsInstance instance = getAndVerifyEdsInstance(blockMailAccount);
        MailIdentityProvider identityProvider = MailIdentityFactory.getProvider(instance.getEdsType());
        identityProvider.blockMailAccount(blockMailAccount);
    }

    private void putAccounts(Map<String, List<EdsIdentityVO.MailAccount>> accounts,
                             EdsIdentityVO.MailAccount mailAccount) {
        accounts.computeIfAbsent(mailAccount.getInstance()
                        .getEdsType(), k -> Lists.newArrayList())
                .add(mailAccount);
    }

    private List<EdsAsset> queryMailAssets(User user) {
        List<EdsAsset> mailIdentityAssets = Lists.newArrayList();
        if (ValidationUtils.isEmail(user.getEmail())) {
            List<EdsAsset> byIndexUsername = edsAssetIndexService.queryIndexByNameAndValue(USER_MAIL, user.getEmail())
                    .stream()
                    .map(e -> edsAssetService.getById(e.getAssetId()))
                    .filter(Objects::nonNull)
                    .filter(e -> EdsAssetTypeEnum.ALIMAIL_USER.name()
                            .equals(e.getAssetType()))
                    .toList();
            if (!CollectionUtils.isEmpty(byIndexUsername)) {
                mailIdentityAssets.addAll(byIndexUsername);
            }
        }
        mailIdentityAssets.addAll(queryByUsernameTag(user.getUsername(), MAIL_ACCOUNT_ASSET_TYPES));
        return mailIdentityAssets.stream()
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

}
