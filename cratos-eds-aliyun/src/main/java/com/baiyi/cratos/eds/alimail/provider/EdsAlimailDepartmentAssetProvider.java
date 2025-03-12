package com.baiyi.cratos.eds.alimail.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.alimail.model.AlimailDepartment;
import com.baiyi.cratos.eds.alimail.repo.AlimailDepartmentRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
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
public class EdsAlimailDepartmentAssetProvider extends BaseEdsInstanceAssetProvider<EdsAlimailConfigModel.Alimail, AlimailDepartment.Department> {

    private final AlimailDepartmentRepo alimailDepartmentRepo;

    public EdsAlimailDepartmentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder,
                                             AlimailDepartmentRepo alimailDepartmentRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.alimailDepartmentRepo = alimailDepartmentRepo;
    }


    @Override
    protected List<AlimailDepartment.Department> listEntities(
            ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance) throws EdsQueryEntitiesException {
        try {
            List<AlimailDepartment.Department> departments = alimailDepartmentRepo.listSubDepartments(
                    instance.getEdsConfigModel(), AlimailDepartmentRepo.ROOT);
            if (CollectionUtils.isEmpty(departments)) {
                return List.of();
            }
            // 递归查询
            return alimailDepartmentRepo.listSubDepartments(instance.getEdsConfigModel(), departments);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance,
                                  AlimailDepartment.Department entity) {
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsAlimailConfigModel.Alimail> instance, EdsAsset edsAsset,
            AlimailDepartment.Department entity) {
        return List.of(toEdsAssetIndex(edsAsset, ALIMAIL_DEPARTMENT_PARENT_ID, entity.getParentId()));
    }

}
