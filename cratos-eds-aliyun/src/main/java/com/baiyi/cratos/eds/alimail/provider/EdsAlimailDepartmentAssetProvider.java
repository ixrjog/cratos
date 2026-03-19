package com.baiyi.cratos.eds.alimail.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.repo.AlimailDepartmentRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
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
public class EdsAlimailDepartmentAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Alimail, AlimailDepartment.Department> {

    public EdsAlimailDepartmentAssetProvider(EdsAssetProviderContext context) {
        super(context);
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
        return createAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(
            ExternalDataSourceInstance<EdsConfigs.Alimail> instance, EdsAsset edsAsset,
            AlimailDepartment.Department entity) {
        return List.of(createEdsAssetIndex(edsAsset, ALIMAIL_DEPARTMENT_PARENT_ID, entity.getParentId()));
    }

}
