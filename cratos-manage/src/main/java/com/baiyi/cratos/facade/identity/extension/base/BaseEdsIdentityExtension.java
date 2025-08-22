package com.baiyi.cratos.facade.identity.extension.base;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.HasEdsInstanceType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_AVATAR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 10:04
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseEdsIdentityExtension {

    protected final EdsAssetWrapper edsAssetWrapper;
    protected final EdsInstanceService edsInstanceService;
    protected final EdsInstanceWrapper edsInstanceWrapper;
    protected final UserService userService;
    protected final UserWrapper userWrapper;
    protected final EdsInstanceProviderHolderBuilder holderBuilder;
    protected final EdsAssetService edsAssetService;
    protected final EdsFacade edsFacade;
    protected final EdsAssetIndexService edsAssetIndexService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

    private static final List<String> EDS_INSTANCE_TYPES = List.of(EdsInstanceTypeEnum.AWS.name(),
            EdsInstanceTypeEnum.ALIYUN.name(), EdsInstanceTypeEnum.HUAWEICLOUD.name(),EdsInstanceTypeEnum.ALIMAIL.name());

    protected Map<Integer, EdsInstance> getEdsInstanceMap(List<EdsAsset> identityAssets,
                                                        HasEdsInstanceType hasEdsInstanceType) {
        Map<Integer, EdsInstance> map = Maps.newHashMap();
        identityAssets.forEach(edsAsset -> {
            EdsInstance instance = Optional.ofNullable(edsInstanceService.getById(edsAsset.getInstanceId()))
                    .orElseThrow(() -> new EdsIdentityException("The edsInstance does not exist: instanceId={}.",
                            edsAsset.getInstanceId()));
            if (!StringUtils.hasText(hasEdsInstanceType.getInstanceType()) || hasEdsInstanceType.getInstanceType()
                    .equals(instance.getEdsType())) {
                map.put(edsAsset.getInstanceId(), instance);
            }
        });
        return map;
    }

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        EdsInstance instance = getEdsInstance(hasEdsInstanceId);
        if (EDS_INSTANCE_TYPES.stream()
                .noneMatch(e -> e.equals(instance.getEdsType()))) {
            EdsIdentityException.runtime("Incorrect instance type.");
        }
        return instance;
    }

    protected List<EdsAsset> onlyInTheInstance(List<EdsAsset> assets, HasEdsInstanceId hasEdsInstanceId) {
        if (IdentityUtils.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            return assets.stream()
                    .filter(e -> e.getInstanceId()
                            .equals(hasEdsInstanceId.getInstanceId()))
                    .toList();
        }
        return assets;
    }

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId,
                                                  EdsInstanceTypeEnum edsInstanceTypeEnum) {
        EdsInstance instance = getEdsInstance(hasEdsInstanceId);
        if (!edsInstanceTypeEnum.name()
                .equals(instance.getEdsType())) {
            EdsIdentityException.runtime("Incorrect instance type.");
        }
        return instance;
    }

    private EdsInstance getEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        if (!IdentityUtils.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            EdsIdentityException.runtime("InstanceId is incorrect.");
        }
        EdsInstance instance = edsInstanceService.getById(hasEdsInstanceId.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("Instance does not exist.");
        }
        return instance;
    }

    protected String verifyAndGeneratePassword(String password) {
        if (PasswordGenerator.isPasswordStrong(password)) {
            return password;
        }
        return PasswordGenerator.generatePassword();
    }

    protected List<EdsAsset> queryByUsernameTag(String username, List<String> assetTypes) {
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
                .filter(byId -> assetTypes.stream()
                        .anyMatch(e -> byId.getAssetType()
                                .equals(e)))
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

    protected String getAvatar(EdsAsset asset) {
        EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), USER_AVATAR);
        if (Objects.nonNull(index)) {
            return index.getValue();
        }
        return null;
    }

}
