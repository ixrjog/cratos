package com.baiyi.cratos.eds.zabbix.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostGroupRepo;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxHostGroupResult;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:12
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_HOSTGROUP)
public class EdsZbxHostGroupAssetProvider extends BaseEdsInstanceAssetProvider<EdsZabbixConfigModel.Zabbix, ZbxHostGroupResult.HostGroup> {

    private final ZbxHostGroupRepo zbxHostGroupRepo;

    public EdsZbxHostGroupAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                        CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                        EdsAssetIndexFacade edsAssetIndexFacade,
                                        UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                        EdsInstanceProviderHolderBuilder holderBuilder,
                                        ZbxHostGroupRepo zbxHostGroupRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.zbxHostGroupRepo = zbxHostGroupRepo;
    }

    @Override
    protected List<ZbxHostGroupResult.HostGroup> listEntities(
            ExternalDataSourceInstance<EdsZabbixConfigModel.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            return zbxHostGroupRepo.listHostGroup(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsZabbixConfigModel.Zabbix> instance,
                                         ZbxHostGroupResult.HostGroup entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getGroupid())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getName())
                .build();
    }

}