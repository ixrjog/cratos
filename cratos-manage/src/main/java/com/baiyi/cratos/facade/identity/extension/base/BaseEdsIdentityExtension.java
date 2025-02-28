package com.baiyi.cratos.facade.identity.extension.base;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;

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

    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId,
                                                  EdsInstanceTypeEnum instanceTypeEnum) {
        if (!IdentityUtil.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            EdsIdentityException.runtime("{} instanceId is incorrect.", instanceTypeEnum.name());
        }
        EdsInstance instance = edsInstanceService.getById(hasEdsInstanceId.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("{} instance does not exist.", instanceTypeEnum.name());
        }
        if (!EdsInstanceTypeEnum.LDAP.name()
                .equals(instance.getEdsType())) {
            EdsIdentityException.runtime("The instance type is not {}.", instanceTypeEnum);
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
