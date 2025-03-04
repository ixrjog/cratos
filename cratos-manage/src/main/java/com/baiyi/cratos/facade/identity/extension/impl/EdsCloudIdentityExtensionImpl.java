package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.EdsCloudIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.cloud.CloudIdentityFactory;
import com.baiyi.cratos.facade.identity.extension.cloud.CloudIdentityProvider;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 09:53
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsCloudIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsCloudIdentityExtension {

    public EdsCloudIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                         EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                         UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                         EdsAssetService edsAssetService, EdsFacade edsFacade) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade);
    }


    protected EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId,
                                                  EdsInstanceTypeEnum instanceTypeEnum) {
        if (!IdentityUtil.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            EdsIdentityException.runtime("{} instanceId is incorrect.", instanceTypeEnum.name());
        }
        EdsInstance instance = edsInstanceService.getById(hasEdsInstanceId.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("{} instance does not exist.", instanceTypeEnum.name());
        }
//        if (!instanceTypeEnum.name()
//                .equals(instance.getEdsType())) {
//            EdsIdentityException.runtime("The instance type is not {}.", instanceTypeEnum);
//        }
        return instance;
    }

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        EdsInstance instance = getAndVerifyEdsInstance(createCloudAccount);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        return cloudIdentityProvider.createCloudAccount(instance, createCloudAccount);
    }

    @Override
    public void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission) {
        EdsInstance instance = getAndVerifyEdsInstance(grantPermission);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        cloudIdentityProvider.grantPermission(instance, grantPermission);
    }

    @Override
    public void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission) {
        EdsInstance instance = getAndVerifyEdsInstance(revokePermission);
        CloudIdentityProvider cloudIdentityProvider = CloudIdentityFactory.getProvider(instance.getEdsType());
        cloudIdentityProvider.revokePermission(instance, revokePermission);
    }

}
