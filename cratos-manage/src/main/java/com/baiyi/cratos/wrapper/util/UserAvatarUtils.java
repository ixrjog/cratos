package com.baiyi.cratos.wrapper.util;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.identity.extension.impl.EdsDingtalkIdentityExtensionImpl;
import com.baiyi.cratos.service.UserService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_USER_MOBILE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/27 15:40
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserAvatarUtils {

    private final UserService userService;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'USER:AVATAR:USERNAME:' + #username", unless = "#result == null")
    public UserVO.UserAvatar queryUserAvatar(String username) {
        EdsIdentityParam.QueryDingtalkIdentityDetails query = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username(username)
                .build();
           EdsIdentityVO.DingtalkIdentityDetails details = SpringContextUtil.getBean(
                        EdsDingtalkIdentityExtensionImpl.class)
                .queryDingtalkIdentityDetails(query);

        if (CollectionUtils.isEmpty(details.getDingtalkIdentities())) {
            return UserVO.UserAvatar.NO_DATA;
        }
        return UserVO.UserAvatar.builder()
                .url(details.getDingtalkIdentities()
                        .getFirst()
                        .getAvatar())
                .source("Dingtalk")
                .build();
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
                .map(asset -> EdsIdentityVO.DingtalkIdentity.builder()
                        .username(username)
                        .user(userWrapper.wrapToTarget(user))
                        .instance(edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                        .account(edsAssetWrapper.wrapToTarget(asset))
                        .avatar(getAvatar(asset))
                        .build())
                .toList();
        return EdsIdentityVO.DingtalkIdentityDetails.builder()
                .username(username)
                .dingtalkIdentities(dingtalkIdentities)
                .build();
    }

    private List<EdsAsset> queryDingtalkAssets(User user,
                                               EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        List<EdsAsset> assets = Lists.newArrayList();
        assets.addAll(
                onlyInTheInstance(queryDingtalkAssetByMobile(user.getMobilePhone()), queryDingtalkIdentityDetails));
        assets.addAll(onlyInTheInstance(queryByUsernameTag(user.getUsername(), EdsAssetTypeEnum.DINGTALK_USER.name()),
                queryDingtalkIdentityDetails));
        return assets.stream()
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    private List<EdsAsset> queryDingtalkAssetByMobile(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return List.of();
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

}
