package com.baiyi.cratos.eds.huaweicloud.cloud.provider.vpc;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.cloud.model.HwcVpc;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcVpcRepo;
import com.baiyi.cratos.eds.huaweicloud.cloud.util.HwcProjectUtils;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.vpc.v2.model.Vpc;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.VPC_CIDR_BLOCK;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 10:22
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_VPC)
public class EdsHwcVpcAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHwcConfigModel.Hwc, HwcVpc.Vpc> {

    public EdsHwcVpcAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<HwcVpc.Vpc> listEntities(String regionId,
                                            EdsHwcConfigModel.Hwc configModel) throws EdsQueryEntitiesException {
        List<Vpc> vpcs = HwcVpcRepo.listVpcs(regionId, configModel);
        if (CollectionUtils.isEmpty(vpcs)) {
            return Collections.emptyList();
        }
        return toEntities(regionId, configModel, vpcs);
    }

    private List<HwcVpc.Vpc> toEntities(String regionId, EdsHwcConfigModel.Hwc configModel,
                                        List<Vpc> vpcs) {
        return vpcs.stream()
                .map(e -> HwcVpc.toVpc(regionId, HwcProjectUtils.findProjectId(regionId, configModel),
                        e))
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                  HwcVpc.Vpc entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVpc()
                        .getId())
                .nameOf(entity.getVpc()
                        .getName())
                .regionOf(entity.getRegionId())
                .createdTimeOf(entity.getVpc()
                        .getCreatedAt())
                .validOf("ACTIVE".equalsIgnoreCase(entity.getVpc()
                        .getStatus()))
                .descriptionOf(entity.getVpc()
                        .getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance, EdsAsset edsAsset,
            HwcVpc.Vpc entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, VPC_CIDR_BLOCK, entity.getVpc()
                .getCidr()));
        return indices;
    }

}
