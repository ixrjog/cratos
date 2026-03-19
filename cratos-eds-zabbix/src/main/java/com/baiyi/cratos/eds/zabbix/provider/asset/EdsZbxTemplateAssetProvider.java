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
import com.baiyi.cratos.eds.zabbix.repo.ZbxTemplateRepo;
import com.baiyi.cratos.eds.zabbix.result.ZbxTemplateResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 18:16
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_TEMPLATE)
public class EdsZbxTemplateAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Zabbix, ZbxTemplateResult.Template> {

    public EdsZbxTemplateAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<ZbxTemplateResult.Template> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            return ZbxTemplateRepo.listTemplate(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Zabbix> instance,
                                         ZbxTemplateResult.Template entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getTemplateid())
                .assetKeyOf(entity.getHost())
                // 显示名
                .nameOf(entity.getName())
                .descriptionOf(entity.getDescription())
                .build();
    }

}