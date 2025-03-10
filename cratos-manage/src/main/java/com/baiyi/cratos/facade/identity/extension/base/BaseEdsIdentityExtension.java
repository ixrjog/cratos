package com.baiyi.cratos.facade.identity.extension.base;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    private static final List<String> EDS_INSTANCE_TYPES = List.of(EdsInstanceTypeEnum.AWS.name(),
            EdsInstanceTypeEnum.ALIYUN.name(), EdsInstanceTypeEnum.HUAWEICLOUD.name());

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        EdsInstance instance = getEdsInstance(hasEdsInstanceId);
        if (EDS_INSTANCE_TYPES.stream()
                .noneMatch(e -> e.equals(instance.getEdsType()))) {
            EdsIdentityException.runtime("Incorrect instance type.");
        }
        return instance;
    }

    protected List<EdsAsset> onlyInTheInstance(List<EdsAsset> assets, HasEdsInstanceId hasEdsInstanceId) {
        if (IdentityUtil.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            return assets.stream()
                    .filter(e -> e.getInstanceId()
                            .equals(hasEdsInstanceId.getInstanceId()))
                    .toList();
        }
        return assets;
    }

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId, EdsInstanceTypeEnum edsInstanceTypeEnum) {
        EdsInstance instance = getEdsInstance(hasEdsInstanceId);
        if (!edsInstanceTypeEnum.name().equals(instance.getEdsType())) {
            EdsIdentityException.runtime("Incorrect instance type.");
        }
        return instance;
    }

    private EdsInstance getEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        if (!IdentityUtil.hasIdentity(hasEdsInstanceId.getInstanceId())) {
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

}
