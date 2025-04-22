package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkIdentityExtension;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_USER_MOBILE;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/10 14:01
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsDingtalkIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsDingtalkIdentityExtension {

    public EdsDingtalkIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                            EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                            UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                            EdsAssetService edsAssetService, EdsFacade edsFacade,
                                            EdsAssetIndexService edsAssetIndexService, TagService tagService,
                                            BusinessTagService businessTagService) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade, edsAssetIndexService, tagService, businessTagService);
    }

    @Override
    public EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        String username = queryDingtalkIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = queryDingtalkAssets(user, queryDingtalkIdentityDetails);
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.DingtalkIdentity> dingtalkIdentities = assets.stream()
                .map(asset -> {
                    EdsAssetIndex mobileIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(),
                            DINGTALK_USER_MOBILE);
                    EdsAssetIndex mailIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), USER_MAIL);
                    return EdsIdentityVO.DingtalkIdentity.builder()
                            .username(username)
                            .user(userWrapper.wrapToTarget(user))
                            .instance(
                                    edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                            .account(edsAssetWrapper.wrapToTarget(asset))
                            .email(Optional.ofNullable(mailIndex).map(EdsAssetIndex::getValue).orElse(null))
                            .mobile(Optional.ofNullable(mobileIndex).map(EdsAssetIndex::getValue).orElse(null))
                            .avatar(getAvatar(asset))
                            .build();
                })
                .toList();
        return EdsIdentityVO.DingtalkIdentityDetails.builder()
                .username(username)
                .dingtalkIdentities(dingtalkIdentities)
                .build();
    }

    private List<EdsAsset> queryDingtalkAssets(User user,
                                               EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        return Stream.of(queryDingtalkAssetByMobile(user.getMobilePhone()),
                        queryDingtalkAssetByEmail(user.getMobilePhone()),
                        queryByUsernameTag(user.getUsername(), EdsAssetTypeEnum.DINGTALK_USER.name()))
                .flatMap(assets -> onlyInTheInstance(assets, queryDingtalkIdentityDetails).stream())
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    private List<EdsAsset> queryDingtalkAssetByMobile(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return List.of();
        }
        if (!ValidationUtils.containsHyphenBetweenDigits(mobilePhone)) {
            mobilePhone = "86-" + mobilePhone;
        }
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(DINGTALK_USER_MOBILE, mobilePhone);
        if (CollectionUtils.isEmpty(indices)) {
            return List.of();
        }
        return indices.stream()
                .map(e -> edsAssetService.getById(e.getAssetId()))
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    private List<EdsAsset> queryDingtalkAssetByEmail(String email) {
        if (!ValidationUtils.isEmail(email)) {
            return List.of();
        }
        return edsAssetIndexService.queryIndexByNameAndValue(USER_MAIL, email)
                .stream()
                .map(e -> edsAssetService.getById(e.getAssetId()))
                .filter(asset -> EdsAssetTypeEnum.DINGTALK_USER.name()
                        .equals(asset.getAssetType()))
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

}
