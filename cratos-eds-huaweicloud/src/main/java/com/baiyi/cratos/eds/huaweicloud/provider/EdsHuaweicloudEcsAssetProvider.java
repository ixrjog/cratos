package com.baiyi.cratos.eds.huaweicloud.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
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
import com.baiyi.cratos.eds.huaweicloud.model.HuaweicloudEcs;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudEcsRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:52
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.HUAWEICLOUD, assetType = EdsAssetTypeEnum.HUAWEICLOUD_ECS)
public class EdsHuaweicloudEcsAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHuaweicloudConfigModel.Huaweicloud, HuaweicloudEcs.Ecs> {

    public EdsHuaweicloudEcsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<HuaweicloudEcs.Ecs> listEntities(String regionId,
                                                    EdsHuaweicloudConfigModel.Huaweicloud configModel) throws EdsQueryEntitiesException {
        List<ServerDetail> serverDetails = HuaweicloudEcsRepo.listServers(regionId, configModel);
        if (CollectionUtils.isEmpty(serverDetails)) {
            return Collections.emptyList();
        }
        return toEcs(regionId, configModel, serverDetails);
    }

    private List<HuaweicloudEcs.Ecs> toEcs(String regionId, EdsHuaweicloudConfigModel.Huaweicloud configModel,
                                           List<ServerDetail> serverDetails) {
        return serverDetails.stream()
                .map(e -> HuaweicloudEcs.Ecs.builder()
                        .regionId(regionId)
                        .serverDetail(e)
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance,
                                  HuaweicloudEcs.Ecs entity) {
        Map<String, List<ServerAddress>> addMap = entity.getServerDetail()
                .getAddresses();
        String privateIp = "";
        for (String key : addMap.keySet()) {
            for (ServerAddress serverAdd : addMap.get(key)) {
                if (ServerAddress.OsEXTIPSTypeEnum.FIXED == serverAdd.getOsEXTIPSType()) {
                    privateIp = serverAdd.getAddr();
                }
            }
        }
        final String assetId = entity.getServerDetail()
                .getId();
        if (!StringUtils.hasText(privateIp)) {
            privateIp = assetId;
        }

        return newEdsAssetBuilder(instance, entity).assetIdOf(assetId)
                .nameOf(entity.getServerDetail()
                        .getName())
                .assetKeyOf(privateIp)
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getServerDetail()
                        .getOsEXTAZAvailabilityZone())
                .createdTimeOf(TimeUtil.toDate(entity.getServerDetail()
                        .getCreated(), TimeZoneEnum.UTC))
                .validOf("ACTIVE".equalsIgnoreCase(entity.getServerDetail()
                        .getStatus()))
                .build();
    }

}
