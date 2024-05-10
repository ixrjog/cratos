package com.baiyi.cratos.eds.huaweicloud.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.model.HuaweicloudEcs;
import com.baiyi.cratos.eds.huaweicloud.repo.HuaweicloudEcsRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:52
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.HUAWEICLOUD, assetType = EdsAssetTypeEnum.HUAWEICLOUD_ECS)
public class EdsHuaweicloudEcsAssetProvider extends BaseEdsInstanceAssetProvider<EdsHuaweicloudConfigModel.Huaweicloud, HuaweicloudEcs.Ecs> {

    public EdsHuaweicloudEcsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
    }

    @Override
    protected List<HuaweicloudEcs.Ecs> listEntities(
            ExternalDataSourceInstance<EdsHuaweicloudConfigModel.Huaweicloud> instance) throws EdsQueryEntitiesException {

        EdsHuaweicloudConfigModel.Huaweicloud huaweicloud = instance.getEdsConfigModel();
        Set<String> reggionIdSet = Sets.newHashSet(huaweicloud.getRegionId());
        reggionIdSet.addAll(Optional.of(huaweicloud)
                .map(EdsHuaweicloudConfigModel.Huaweicloud::getRegionIds)
                .orElse(null));
        List<HuaweicloudEcs.Ecs> entities = Lists.newArrayList();
        try {
            reggionIdSet.forEach(regionId -> {
                List<ServerDetail> serverDetails = HuaweicloudEcsRepo.listServers(regionId, huaweicloud);
                if (!CollectionUtils.isEmpty(serverDetails)) {
                    entities.addAll(serverDetails.stream()
                            .map(e -> HuaweicloudEcs.Ecs.builder()
                                    .regionId(regionId)
                                    .serverDetail(e)
                                    .build())
                            .toList());
                }
            });
            return entities;
        } catch (ServiceResponseException e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
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
