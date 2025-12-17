package com.baiyi.cratos.eds.alimail.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.repo.AlimailDepartmentRepo;
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
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIMAIL_DEPARTMENT_PARENT_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 17:26
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIMAIL, assetTypeOf = EdsAssetTypeEnum.ALIMAIL_DEPARTMENT)
public class EdsAlimailDepartmentAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Alimail, AlimailDepartment.Department> {

    public EdsAlimailDepartmentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                             EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected List<AlimailDepartment.Department> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Alimail> instance) throws EdsQueryEntitiesException {
        try {
            List<AlimailDepartment.Department> departments = AlimailDepartmentRepo.listSubDepartments(
                    instance.getConfig(), AlimailDepartmentRepo.ROOT);
            if (CollectionUtils.isEmpty(departments)) {
                return List.of();
            }
            // 递归查询
            return AlimailDepartmentRepo.listSubDepartments(instance.getConfig(), departments);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Alimail> instance,
                                  AlimailDepartment.Department entity) {
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Alimail> instance, EdsAsset edsAsset,
            AlimailDepartment.Department entity) {
        return List.of(createEdsAssetIndex(edsAsset, ALIMAIL_DEPARTMENT_PARENT_ID, entity.getParentId()));
    }

}
