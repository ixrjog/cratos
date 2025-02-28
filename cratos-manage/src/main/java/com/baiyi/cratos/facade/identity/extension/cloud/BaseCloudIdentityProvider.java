package com.baiyi.cratos.facade.identity.extension.cloud;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:24
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseCloudIdentityProvider<Config extends IEdsConfigModel> implements CloudIdentityProvider, InitializingBean {

    private final EdsInstanceService edsInstanceService;
    protected final EdsInstanceProviderHolderBuilder holderBuilder;

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        EdsInstance instance = getAndVerifyEdsInstance(createCloudAccount,
                EdsInstanceTypeEnum.valueOf(getInstanceType()));

        // TODO
        return null;
    }

    abstract protected EdsIdentityVO.CloudAccount createCloudAccount(EdsInstance instance);

    abstract protected EdsIdentityVO.CloudAccount getAccount(Config config, String accountName);

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

    @Override
    public void afterPropertiesSet() {
        CloudIdentityFactory.register(this);
    }

}
