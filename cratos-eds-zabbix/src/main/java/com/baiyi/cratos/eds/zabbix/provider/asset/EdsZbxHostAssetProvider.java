package com.baiyi.cratos.eds.zabbix.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxInterfaceResult;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 14:07
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_HOST)
public class EdsZbxHostAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Zabbix, ZbxHostResult.Host> {

    public EdsZbxHostAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                   CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                   EdsAssetIndexFacade edsAssetIndexFacade,
                                   AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                   EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected List<ZbxHostResult.Host> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            List<ZbxHostResult.Host> hosts = ZbxHostRepo.listHost(instance.getConfig());
            if (CollectionUtils.isEmpty(hosts)) {
                return List.of();
            } else {
                enrichHosts(instance.getConfig(), hosts);
                return hosts;
            }
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private void enrichHosts(EdsConfigs.Zabbix zbx, List<ZbxHostResult.Host> hosts) {
        for (ZbxHostResult.Host host : hosts) {
            try {
                ZbxHostResult.HostExtend hostExtend = ZbxHostRepo.getHostExtend(zbx, host.getHostid());
                host.setHostExtend(hostExtend);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Zabbix> instance,
                                         ZbxHostResult.Host entity) {
        String assetKey = Optional.ofNullable(entity)
                .map(ZbxHostResult.Host::getHostExtend)
                .map(ZbxHostResult.HostExtend::getInterfaces)
                .flatMap(list -> list.stream()
                        .filter(e -> e != null && e.getUseip() == 1)
                        .findFirst()
                        .map(ZbxInterfaceResult.Interface::getIp))
                .filter(StringUtils::hasText)
                .orElse(entity.getName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getHostid())
                .assetKeyOf(assetKey)
                .nameOf(entity.getName())
                .descriptionOf(entity.getDescription())
                .build();
    }

}