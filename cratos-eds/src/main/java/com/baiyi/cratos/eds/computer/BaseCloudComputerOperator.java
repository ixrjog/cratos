package com.baiyi.cratos.eds.computer;

import com.baiyi.cratos.common.exception.CloudComputerOperationException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.computer.context.CloudComputerContext;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 11:33
 * &#064;Version 1.0
 */
@SuppressWarnings("All")
@RequiredArgsConstructor
public abstract class BaseCloudComputerOperator<Config extends HasEdsConfig, Computer> implements HasCloudComputerOperator<Config, Computer> {

    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;
    private final EdsAssetService edsAssetService;
    private final CredentialService credentialService;
    private final ConfigCredTemplate configCredTemplate;

    @Override
    public void reboot(Integer assetId) {
        CloudComputerContext<Config> context = buildContext(assetId);
        this.rebootInstance(context);
    }

    @Override
    public void start(Integer assetId) {
        CloudComputerContext<Config> context = buildContext(assetId);
        this.startInstance(context);
    }

    @Override
    public void stop(Integer assetId) {
        CloudComputerContext<Config> context = buildContext(assetId);
        this.stopInstance(context);
    }

    protected CloudComputerContext<Config> buildContext(Integer assetId) {
        EdsAsset asset = edsAssetService.getById(assetId);
        Optional.of(asset)
                .orElseThrow(() -> new CloudComputerOperationException("Asset not found, assetId: {}", assetId));
        EdsInstance edsInstance = edsInstanceService.getById(asset.getInstanceId());
        Optional.of(edsInstance)
                .orElseThrow(() -> new CloudComputerOperationException(
                        "Instance not found, instanceId: {}",
                        asset.getInstanceId()
                ));
        if (!IdentityUtils.hasIdentity(edsInstance.getConfigId())) {
            CloudComputerOperationException.runtime(
                    "Instance not bound to config, instanceId: {}", edsInstance.getId());
        }
        EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
        Optional.of(edsConfig)
                .orElseThrow(() -> new CloudComputerOperationException(
                        "Instance configuration not found: configId={}",
                        edsInstance.getConfigId()
                ));
        Config config = configLoadAs(edsConfig);
        return CloudComputerContext.<Config>builder()
                .asset(asset)
                .edsInstance(edsInstance)
                .edsConfig(edsConfig)
                .config(config)
                .build();
    }

    protected EdsAsset getAsset(Integer assetId) {
        return edsAssetService.getById(assetId);
    }

    protected abstract String rebootInstance(
            CloudComputerContext<Config> context) throws CloudComputerOperationException;

    protected abstract String startInstance(
            CloudComputerContext<Config> context) throws CloudComputerOperationException;

    protected abstract String stopInstance(
            CloudComputerContext<Config> context) throws CloudComputerOperationException;

    protected Config configLoadAs(EdsConfig edsConfig) {
        String configContent = edsConfig.getConfigContent();
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credentialService.getById(edsConfig.getCredentialId());
            if (cred != null) {
                return configLoadAs(configCredTemplate.renderTemplate(configContent, cred));
            }
        }
        return configLoadAs(configContent);
    }

    protected Config configLoadAs(String configContent) {
        // Get the entity type of generic `Config`
        Class<Config> clazz = Generics.find(this.getClass(), BaseCloudComputerOperator.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

}
