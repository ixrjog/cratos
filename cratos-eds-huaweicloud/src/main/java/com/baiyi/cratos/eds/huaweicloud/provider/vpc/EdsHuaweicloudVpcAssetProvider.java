package com.baiyi.cratos.eds.huaweicloud.provider.vpc;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.model.HuaweicloudVpc;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudVpcRepo;
import com.baiyi.cratos.eds.huaweicloud.util.HuaweicloudProjectUtil;
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
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.HUAWEICLOUD, assetType = EdsAssetTypeEnum.HUAWEICLOUD_VPC)
public class EdsHuaweicloudVpcAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHuaweicloudConfigModel.Huaweicloud, HuaweicloudVpc.Vpc> {

    public EdsHuaweicloudVpcAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<HuaweicloudVpc.Vpc> listEntities(String regionId,
                                                    EdsHuaweicloudConfigModel.Huaweicloud configModel) throws EdsQueryEntitiesException {
        List<Vpc> vpcs = HuaweicloudVpcRepo.listVpcs(regionId, configModel);
        if (CollectionUtils.isEmpty(vpcs)) {
            return Collections.emptyList();
        }
        return toEntities(regionId, configModel, vpcs);
    }

    private List<HuaweicloudVpc.Vpc> toEntities(String regionId, EdsHuaweicloudConfigModel.Huaweicloud configModel,
                                                List<Vpc> vpcs) {
        return vpcs.stream()
                .map(e -> HuaweicloudVpc.toVpc(regionId, HuaweicloudProjectUtil.findProjectId(regionId, configModel),
                        e))
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance,
                                  HuaweicloudVpc.Vpc entity) {
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
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance, EdsAsset edsAsset,
            HuaweicloudVpc.Vpc entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, VPC_CIDR_BLOCK, entity.getVpc()
                .getCidr()));
        return indices;
    }

}
