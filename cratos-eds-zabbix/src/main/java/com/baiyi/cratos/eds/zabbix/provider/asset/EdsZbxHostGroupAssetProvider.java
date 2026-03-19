package com.baiyi.cratos.eds.zabbix.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.zabbix.repo.ZbxHostGroupRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostGroupResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:12
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_HOSTGROUP)
public class EdsZbxHostGroupAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Zabbix, ZbxHostGroupResult.HostGroup> {

    public EdsZbxHostGroupAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<ZbxHostGroupResult.HostGroup> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            return ZbxHostGroupRepo.listHostGroup(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Zabbix> instance,
                                         ZbxHostGroupResult.HostGroup entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getGroupid())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getName())
                .build();
    }

}