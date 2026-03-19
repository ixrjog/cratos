package com.baiyi.cratos.eds.harbor.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.harbor.model.HarborProject;
import com.baiyi.cratos.eds.harbor.repo.HarborProjectRepo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 上午11:23
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HARBOR, assetTypeOf = EdsAssetTypeEnum.HARBOR_PROJECT)
public class EdsHarborProjectAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Harbor, HarborProject.Project> {

    public EdsHarborProjectAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<HarborProject.Project> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Harbor> instance) throws EdsQueryEntitiesException {
        try {
            return HarborProjectRepo.listProjects(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Harbor> instance,
                                  HarborProject.Project entity) {
        return createAssetBuilder(instance, entity)
                .assetIdOf(entity.getProjectId())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getName())
                .createdTimeOf(entity.getCreationTime())
                .build();
    }

}