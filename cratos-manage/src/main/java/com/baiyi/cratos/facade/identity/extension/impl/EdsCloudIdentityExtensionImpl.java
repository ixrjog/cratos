package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.EdsCloudIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 09:53
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsCloudIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsCloudIdentityExtension {

    private static final String[] EDS_INSTANCE_TYPES = {EdsInstanceTypeEnum.AWS.name(), EdsInstanceTypeEnum.ALIYUN.name()};

    public EdsCloudIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                         EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                         UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                         EdsAssetService edsAssetService, EdsFacade edsFacade) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade);
    }

    private EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        return getAndVerifyEdsInstance(hasEdsInstanceId, EdsInstanceTypeEnum.LDAP);
    }

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        return null;
    }

}
