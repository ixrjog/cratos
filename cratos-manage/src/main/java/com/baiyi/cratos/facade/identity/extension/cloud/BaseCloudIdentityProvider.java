package com.baiyi.cratos.facade.identity.extension.cloud;

import com.baiyi.cratos.common.exception.CloudIdentityException;
import com.baiyi.cratos.common.util.PasswordGenerator;
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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:24
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseCloudIdentityProvider<Config extends IEdsConfigModel,Account> implements CloudIdentityProvider, InitializingBean {

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
        EdsIdentityVO.CloudAccount cloudAccount = getAccount(instance, user, user.getUsername());
        // 账户已存在
        if (cloudAccount.isExist()) {
            return cloudAccount;
        }
        final String password = generatePassword(createCloudAccount);
        cloudAccount = createAccount(holder.getInstance()
                .getEdsConfigModel(), instance, user, password);
        cloudAccount.setPassword(password);
        return cloudAccount;
    }

    /**
     * 特殊密码策略可重写
     *
     * @param createCloudAccount
     * @return
     */
    protected String generatePassword(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        return StringUtils.hasText(
                createCloudAccount.getPassword()) ? createCloudAccount.getPassword() : PasswordGenerator.generatePassword();
    }

    @Override
    public void grantPermission(EdsInstance instance, EdsIdentityParam.GrantPermission grantPermission) {
        EdsAsset permission = edsAssetService.getById(grantPermission.getGrantId());
        if (Objects.isNull(permission)) {
            CloudIdentityException.runtime("Permission asset do not exist.");
        }
        EdsAsset account = edsAssetService.getById(grantPermission.getAccountId());
        if (Objects.isNull(account)) {
            CloudIdentityException.runtime("Account asset do not exist.");
        }
        this.grantPermission(instance, account, permission);
    }

    @Override
    public void revokePermission(EdsInstance instance, EdsIdentityParam.RevokePermission revokePermission) {
        EdsAsset permission = edsAssetService.getById(revokePermission.getRevokeId());
        if (Objects.isNull(permission)) {
            CloudIdentityException.runtime("Permission asset do not exist.");
        }
        EdsAsset account = edsAssetService.getById(revokePermission.getAccountId());
        if (Objects.isNull(account)) {
            CloudIdentityException.runtime("Account asset do not exist.");
        }
        this.revokePermission(instance, account, permission);
    }

    protected EdsAsset getAccountAsset(int instanceId, String username) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryInstanceIndexByNameAndValue(instanceId,
                CLOUD_ACCOUNT_USERNAME, username);
        if (CollectionUtils.isEmpty(indices)) {
            return null;
        }
        return edsAssetService.getById(indices.getFirst()
                .getAssetId());
    }

    protected List<String> getPolicies(EdsAsset account) {
        String policyIndexName = getPolicyIndexName(account);
        EdsAssetIndex query = EdsAssetIndex.builder()
                .instanceId(account.getInstanceId())
                .assetId(account.getId())
                .name(policyIndexName)
                .build();
        EdsAssetIndex policyIndex = edsAssetIndexService.getByUniqueKey(query);
        if (Objects.isNull(policyIndex)) {
            return List.of();
        }
        return List.of(policyIndex.getValue()
                .split(","));
    }

    protected String getAccountAssetType() {
        return getAssetType();
    }

    @Override
    public EdsIdentityVO.CloudAccount getAccount(EdsInstance instance, User user, String username) {
        try {
            EdsAsset account = getAccountAsset(instance.getId(), username);
            if (Objects.isNull(account)) {
                return EdsIdentityVO.CloudAccount.NO_ACCOUNT;
            }
            return EdsIdentityVO.CloudAccount.builder()
                    .instance(instanceWrapper.wrapToTarget(instance))
                    .user(userWrapper.wrapToTarget(user))
                    .account(edsAssetWrapper.wrapToTarget(account))
                    .username(username)
                    .password("******")
                    .accountLogin(toAccountLoginDetails(account, username))
                    .policies(getPolicies(account))
                    .build();
        } catch (Exception ex) {
            throw new CloudIdentityException(ex.getMessage());
        }
    }

    protected void postImportAccountAsset(EdsInstanceProviderHolder<Config, Account> holder,
                                          Account account) {
        holder.getProvider()
                .importAsset(holder.getInstance(), account);
    }

    abstract protected EdsIdentityVO.CloudAccount createAccount(Config config, EdsInstance instance, User user,
                                                                String password);

    abstract protected void grantPermission(EdsInstance instance, EdsAsset account, EdsAsset permission);

    abstract protected void revokePermission(EdsInstance instance, EdsAsset account, EdsAsset permission);

    @Override
    public void afterPropertiesSet() {
        CloudIdentityFactory.register(this);
    }

}
