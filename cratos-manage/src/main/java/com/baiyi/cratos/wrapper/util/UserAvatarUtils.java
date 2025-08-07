package com.baiyi.cratos.wrapper.util;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/27 15:40
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserAvatarUtils {

    private final UserService userService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'USER:AVATAR:USERNAME:' + #username", unless = "#result == null")
    public UserVO.UserAvatar queryUserAvatar(String username) {
        User user = userService.getByUsername(username);
        List<EdsAsset> assets = queryDingtalkAssets(user);
        if (CollectionUtils.isEmpty(assets)) {
            return UserVO.UserAvatar.NO_DATA;
        }
        String avatar = getAvatar(assets.getFirst());
        return UserVO.UserAvatar.builder()
                .url(avatar)
                .source("Dingtalk")
                .build();
    }

    public List<EdsAsset> queryDingtalkAssets(User user) {
        return Stream.of(queryDingtalkAssetByMobile(user.getMobilePhone()), queryDingtalkAssetByEmail(user.getEmail()),
                        queryByUsernameTag(user.getUsername(), EdsAssetTypeEnum.DINGTALK_USER.name()))
                .flatMap(List::stream)
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    protected List<EdsAsset> queryByUsernameTag(String username, String assetType) {
        Tag tag = tagService.getByTagKey(SysTagKeys.USERNAME.getKey());
        if (Objects.isNull(tag)) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .tagValue(username)
                .build();
        return businessTagService.queryBusinessIdByTag(queryByTag)
                .stream()
                .map(edsAssetService::getById)
                .filter(byId -> assetType.equals(byId.getAssetType()))
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
                // 过滤掉 null 值
                .filter(Objects::nonNull)
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
                // 过滤掉 null 值
                .filter(Objects::nonNull)
                .filter(asset -> EdsAssetTypeEnum.DINGTALK_USER.name()
                        .equals(asset.getAssetType()))
                .collect(Collectors.toMap(EdsAsset::getId, asset -> asset, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
    }

    protected String getAvatar(EdsAsset asset) {
        EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), USER_AVATAR);
        if (Objects.nonNull(index)) {
            return index.getValue();
        }
        return null;
    }

}
