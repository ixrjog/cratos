package com.baiyi.cratos.eds.huaweicloud.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
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
import com.baiyi.cratos.eds.huaweicloud.model.HwcEcs;
import com.baiyi.cratos.eds.huaweicloud.repo.HwcEcsRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.EIP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:52
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ECS)
public class EdsHwcEcsAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHwcConfigModel.Hwc, HwcEcs.Ecs> {

    public EdsHwcEcsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<HwcEcs.Ecs> listEntities(String regionId,
                                            EdsHwcConfigModel.Hwc configModel) throws EdsQueryEntitiesException {
        List<ServerDetail> serverDetails = HwcEcsRepo.listServers(regionId, configModel);
        if (CollectionUtils.isEmpty(serverDetails)) {
            return Collections.emptyList();
        }
        return toEntities(regionId, configModel, serverDetails);
    }

    private List<HwcEcs.Ecs> toEntities(String regionId, EdsHwcConfigModel.Hwc configModel,
                                        List<ServerDetail> serverDetails) {
        return serverDetails.stream()
                .map(e -> HwcEcs.toEcs(regionId, e))
                .toList();
    }

    private Map<String, List<HwcEcs.ServerAddress>> toAddressTypeMap(
            Map<String, List<HwcEcs.ServerAddress>> addresses) {
        Map<String, List<HwcEcs.ServerAddress>> catAddress = Maps.newHashMap();
        for (Map.Entry<String, List<HwcEcs.ServerAddress>> entry : addresses.entrySet()) {
            String k = entry.getKey();
            List<HwcEcs.ServerAddress> v = entry.getValue();
            v.forEach(e -> {
                if (catAddress.containsKey(e.getOsEXTIPSType())) {
                    catAddress.get(e.getOsEXTIPSType())
                            .add(e);
                } else {
                    catAddress.put(e.getOsEXTIPSType(), Lists.newArrayList(e));
                }
            });
        }
        return catAddress;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                  HwcEcs.Ecs entity) {
        Map<String, List<HwcEcs.ServerAddress>> addressTypeMap = toAddressTypeMap(entity.getServerDetail()
                .getAddresses());
        String privateIp = addressTypeMap.get(ServerAddress.OsEXTIPSTypeEnum.FIXED.getValue())
                .getFirst()
                .getAddr();
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
                .createdTimeOf(TimeUtils.toDate(entity.getServerDetail()
                        .getCreated(), TimeZoneEnum.UTC))
                .validOf("ACTIVE".equalsIgnoreCase(entity.getServerDetail()
                        .getStatus()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(
            ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance, EdsAsset edsAsset,
            HwcEcs.Ecs entity) {
        Map<String, List<HwcEcs.ServerAddress>> addressTypeMap = toAddressTypeMap(entity.getServerDetail()
                .getAddresses());
        List<EdsAssetIndex> indices = Lists.newArrayList();
        if (addressTypeMap.containsKey(ServerAddress.OsEXTIPSTypeEnum.FLOATING.getValue())) {
            indices.add(createEdsAssetIndex(edsAsset, EIP,
                    addressTypeMap.get(ServerAddress.OsEXTIPSTypeEnum.FLOATING.getValue())
                            .getFirst()
                            .getAddr()));
        }
        return indices;
    }

}
