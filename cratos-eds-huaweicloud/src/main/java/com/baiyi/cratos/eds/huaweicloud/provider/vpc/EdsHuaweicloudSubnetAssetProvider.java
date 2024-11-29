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
import com.baiyi.cratos.eds.huaweicloud.model.HwcSubnet;
import com.baiyi.cratos.eds.huaweicloud.repo.HwcVpcRepo;
import com.baiyi.cratos.eds.huaweicloud.util.HwcProjectUtil;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.vpc.v2.model.Subnet;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.SUBNET_CIDR_BLOCK;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.VPC_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 15:53
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_SUBNET)
public class EdsHuaweicloudSubnetAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHuaweicloudConfigModel.Huaweicloud, HwcSubnet.Subnet> {

    public EdsHuaweicloudSubnetAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<HwcSubnet.Subnet> listEntities(String regionId,
                                                  EdsHuaweicloudConfigModel.Huaweicloud configModel) throws EdsQueryEntitiesException {
        List<Subnet> subnets = HwcVpcRepo.listSubnets(regionId, configModel);
        if (CollectionUtils.isEmpty(subnets)) {
            return Collections.emptyList();
        }
        return toEntities(regionId, configModel, subnets);
    }

    private List<HwcSubnet.Subnet> toEntities(String regionId,
                                              EdsHuaweicloudConfigModel.Huaweicloud configModel,
                                              List<Subnet> subnets) {
        return subnets.stream()
                .map(e -> HwcSubnet.toSubnet(regionId,
                        HwcProjectUtil.findProjectId(regionId, configModel), e))
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance,
                                  HwcSubnet.Subnet entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getSubnet()
                        .getId())
                .nameOf(entity.getSubnet()
                        .getName())
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getSubnet().getAvailabilityZone())
                .createdTimeOf(entity.getSubnet()
                        .getCreatedAt())
                .validOf("ACTIVE".equalsIgnoreCase(entity.getSubnet()
                        .getStatus()))
                .descriptionOf(entity.getSubnet()
                        .getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance,
                                                      EdsAsset edsAsset, HwcSubnet.Subnet entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, SUBNET_CIDR_BLOCK , entity.getSubnet().getCidr()));
        indices.add(toEdsAssetIndex(edsAsset, VPC_ID , entity.getSubnet().getVpcId()));
        // indices.add(toEdsAssetIndex(edsAsset, SUBNET_AVAILABLE_IP_ADDRESS_COUNT, entity.getVirtualSwitch().getAvailableIpAddressCount()));
        return indices;
    }

}
