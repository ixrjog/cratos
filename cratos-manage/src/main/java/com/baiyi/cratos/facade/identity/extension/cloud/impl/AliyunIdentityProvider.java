package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.extension.cloud.BaseCloudIdentityProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AliyunIdentityProvider extends BaseCloudIdentityProvider<EdsAliyunConfigModel.Aliyun> {

    private final AliyunRamUserRepo ramUserRepo;

    public AliyunIdentityProvider(EdsInstanceService edsInstanceService, EdsInstanceProviderHolderBuilder holderBuilder,
                                  AliyunRamUserRepo ramUserRepo) {
        super(edsInstanceService, holderBuilder);
        this.ramUserRepo = ramUserRepo;
    }

    @Override
    protected EdsIdentityVO.CloudAccount createCloudAccount(EdsInstance instance) {
        return null;
    }

    @Override
    protected EdsIdentityVO.CloudAccount getAccount(EdsAliyunConfigModel.Aliyun config, String accountName) {
        // TODO
        try {
            GetUserResponse.User ramUser = ramUserRepo.getUser(config, accountName);
            if (Objects.isNull(ramUser)) {
                return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
            }

            return null;

        } catch (ClientException ce) {
            throw new CloudIdentityException(ce.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return EdsInstanceTypeEnum.ALIYUN.name();
    }

    @Override
    public void grantPermission(EdsIdentityParam.GrantPermission grantPermission) {

    }

    @Override
    public void revokePermission(EdsIdentityParam.RevokePermission revokePermission) {

    }

}
