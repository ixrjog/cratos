package com.baiyi.cratos.facade.identity.extension.cloud.impl;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.googlecloud.model.GoogleMemberModel;
import com.baiyi.cratos.facade.identity.extension.cloud.BaseCloudIdentityProvider;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.GCP_MEMBER_ROLES;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 10:08
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_MEMBER)
public class GcpIdentityProvider extends BaseCloudIdentityProvider<EdsGcpConfigModel.Gcp, GoogleMemberModel.Member> {

    public GcpIdentityProvider(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                               EdsAssetWrapper edsAssetWrapper, EdsAssetIndexService edsAssetIndexService,
                               UserService userService, UserWrapper userWrapper, EdsInstanceWrapper instanceWrapper,
                               EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsInstanceService, edsAssetService, edsAssetWrapper, edsAssetIndexService, userService, userWrapper,
                instanceWrapper, holderBuilder);
    }

    private static final String GCP_LOGIN_URL = "https://console.cloud.google.com/";

    @Override
    protected EdsIdentityVO.CloudAccount createAccount(EdsGcpConfigModel.Gcp aws, EdsInstance instance,
                                                       com.baiyi.cratos.domain.generator.User user, String password) {
        throw new CloudIdentityException("Operation not supported");
    }

    @Override
    protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        throw new CloudIdentityException("Operation not supported");
    }

    @Override
    protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission) {
        throw new CloudIdentityException("Operation not supported");
    }

    @Override
    public String getPolicyIndexName(EdsAsset asset) {
        return GCP_MEMBER_ROLES;
    }

    /**
     * 通过邮箱查找用户
     *
     * @param instanceId
     * @param user
     * @param username
     * @return
     */
    @Override
    protected EdsAsset queryAccountAsset(int instanceId, User user, String username) {
        if (ValidationUtils.isEmail(user.getEmail())) {
            List<EdsAsset> assets = edsAssetService.queryInstanceAssetByTypeAndKey(instanceId, getAccountAssetType(),
                    user.getEmail());
            if (!CollectionUtils.isEmpty(assets)) {
                return assets.getFirst();
            }
        }
        return super.queryAccountAsset(instanceId, user, username);
    }

    @Override
    public EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset, String username) {
        EdsGcpConfigModel.Gcp gcp = (EdsGcpConfigModel.Gcp) holderBuilder.newHolder(asset.getInstanceId(),
                        getAccountAssetType())
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .username(asset.getName())
                .name(asset.getName())
                .loginUsername(asset.getName())
                .loginUrl(GCP_LOGIN_URL)
                .build();
    }

    @Override
    public void blockCloudAccount(EdsInstance instance, EdsIdentityParam.BlockCloudAccount blockCloudAccount) {

    }

}
