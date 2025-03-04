package com.baiyi.cratos.facade.identity.extension.cloud;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:24
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseCloudIdentityProvider<Config extends IEdsConfigModel> implements CloudIdentityProvider, InitializingBean {

    private final EdsInstanceService edsInstanceService;
    protected final EdsAssetService edsAssetService;
    protected final EdsAssetWrapper edsAssetWrapper;
    protected final EdsAssetIndexService edsAssetIndexService;
    private final UserService userService;
    protected final UserWrapper userWrapper;
    protected final EdsInstanceWrapper instanceWrapper;
    protected final EdsInstanceProviderHolderBuilder holderBuilder;
    public final static boolean CREATE_LOGIN_PROFILE = true;

    @SuppressWarnings("unchecked")
    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsInstance instance,
                                                         EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) holderBuilder.newHolder(
                instance.getId(), getAccountAssetType());
        User user = userService.getByUsername(createCloudAccount.getUsername());
        EdsIdentityVO.CloudAccount cloudAccount = getAccount(holder.getInstance()
                .getEdsConfigModel(), instance, user);
        // 账户已存在
        if (cloudAccount.isExist()) {
            return cloudAccount;
        }
        return createAccount(holder.getInstance()
                .getEdsConfigModel(), instance, user);
    }

    @Override
    public void grantPermission(EdsInstance instance, EdsIdentityParam.GrantPermission grantPermission) {
             EdsAsset permissionAsset = edsAssetService.getById(grantPermission.getGrantId());
    }

    @Override
    public void revokePermission(EdsInstance instance, EdsIdentityParam.RevokePermission revokePermission) {

    }

    protected EdsAsset getCloudAccountAsset(int instanceId, String username) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByInstanceAndAssetTypeAndNameValue(instanceId,
                getAccountAssetType(), CLOUD_ACCOUNT_USERNAME, username);
        if (CollectionUtils.isEmpty(indices)) {
            return null;
        }
        return edsAssetService.getById(indices.getFirst()
                .getAssetId());
    }

    protected String getAccountAssetType() {
        return getAssetType();
    }

    abstract protected EdsIdentityVO.CloudAccount createAccount(Config config, EdsInstance instance, User user);

    abstract protected EdsIdentityVO.CloudAccount getAccount(Config config, EdsInstance instance, User user);

    @Override
    public void afterPropertiesSet() {
        CloudIdentityFactory.register(this);
    }

}
