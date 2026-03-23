package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.googlecloud.model.GcpIamModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpIamRepo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 15:24
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_IAM_ROLE)
public class EdsGcpRoleAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Gcp, GcpIamModel.Role> {

    private final GcpIamRepo gcpIamRepo;

    public EdsGcpRoleAssetProvider(EdsAssetProviderContext context, GcpIamRepo gcpIamRepo) {
        super(context);
        this.gcpIamRepo = gcpIamRepo;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                         GcpIamModel.Role entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getName())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getTitle())
                .kindOf(entity.getStage())
                .validOf(!entity.getDeleted())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected List<GcpIamModel.Role> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return gcpIamRepo.listRoles(instance.getConfig())
                    .stream()
                    .map(role -> GcpIamModel.Role.builder()
                            .name(role.getName())
                            .description(role.getDescription())
                            .title(role.getTitle())
                            .stage(role.getStage()
                                           .name())
                            .deleted(role.getDeleted())
                            .build())
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
