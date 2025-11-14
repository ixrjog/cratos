package com.baiyi.cratos.facade.identity.extension.cloud.provider;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.azure.graph.model.GraphUserModel;
import com.baiyi.cratos.eds.azure.repo.GraphUserRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAzureConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.extension.cloud.provider.base.BaseCloudIdentityProvider;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.AZURE_DIRECTORY_ROLES;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/13 10:16
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AZURE, assetTypeOf = EdsAssetTypeEnum.AZURE_USER)
public class AzureIdentityProvider extends BaseCloudIdentityProvider<EdsAzureConfigModel.Azure, GraphUserModel.User> {

    public AzureIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                 EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                                 UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                                 EdsInstanceProviderHolderBuilder holderBuilder) {
        super(
                edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder
        );
    }

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsAzureConfigModel.Azure config, EdsInstance instance,
                                                       User user, String password) {
        throw new CloudIdentityException("Operation not supported.");
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        CloudIdentityException.runtime("Operation not supported.");
    }

    @Override
    public String getPolicyIndexName(EdsAsset asset) {
        return AZURE_DIRECTORY_ROLES;
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username) {
        EdsAzureConfigModel.Azure config = (EdsAzureConfigModel.Azure) holderBuilder.newHolder(
                        asset.getInstanceId(),
                        getAccountAssetType()
                )
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .accountId(asset.getAssetId())
                .username(username)
                .name(asset.getName())
                .loginUsername(asset.getAssetKey())
                .loginUrl(config.getLogin()
                                  .getLoginUrl())
                .build();
    }

    @Override
    public void blockCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        EdsAzureConfigModel.Azure config = (EdsAzureConfigModel.Azure) holderBuilder.newHolder(
                        blockCloudAccount.getInstanceId(), getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        try {
            GraphUserRepo.blockUserById(config, blockCloudAccount.getAccountId());
        } catch (Exception e) {
            throw new CloudIdentityException(e.getMessage());
        }
    }

    @Override
    public void importCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {
        // TODO
    }

}