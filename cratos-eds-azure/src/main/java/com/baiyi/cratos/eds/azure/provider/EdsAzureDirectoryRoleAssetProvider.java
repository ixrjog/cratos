package com.baiyi.cratos.eds.azure.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.azure.graph.model.GraphDirectoryModel;
import com.baiyi.cratos.eds.azure.repo.GraphDirectoryRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/14 11:36
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AZURE, assetTypeOf = EdsAssetTypeEnum.AZURE_DIRECTORY_ROLE)
public class EdsAzureDirectoryRoleAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Azure, GraphDirectoryModel.Role> {

    public EdsAzureDirectoryRoleAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<GraphDirectoryModel.Role> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Azure> instance) throws EdsQueryEntitiesException {
        return GraphDirectoryRepo.listRoles(instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Azure> instance,
                                         GraphDirectoryModel.Role entity) throws EdsAssetConversionException {
        final String key = Joiner.on("|")
                .skipNulls()
                .join(entity.getId(), entity.getRoleTemplateId());
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getDisplayName())
                .assetKeyOf(key)
                .descriptionOf(entity.getDescription())
                .build();
    }

}