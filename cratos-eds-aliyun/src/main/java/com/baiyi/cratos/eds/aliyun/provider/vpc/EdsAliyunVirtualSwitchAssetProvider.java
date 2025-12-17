package com.baiyi.cratos.eds.aliyun.provider.vpc;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.model.AliyunVirtualSwitch;
import com.baiyi.cratos.eds.aliyun.repo.AliyunVpcRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午2:30
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_VIRTUAL_SWITCH)
public class EdsAliyunVirtualSwitchAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aliyun, AliyunVirtualSwitch.Switch> {

    private final AliyunVpcRepo aliyunVpcRepo;

    public EdsAliyunVirtualSwitchAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                               EdsInstanceProviderHolderBuilder holderBuilder,
                                               AliyunVpcRepo aliyunVpcRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.aliyunVpcRepo = aliyunVpcRepo;
    }

    @Override
    protected List<AliyunVirtualSwitch.Switch> listEntities(String regionId, EdsConfigs.Aliyun configModel) {
        try {
            List<DescribeVpcsResponse.Vpc> vpcs = aliyunVpcRepo.listVpc(regionId, configModel);
            if (CollectionUtils.isEmpty(vpcs)) {
                return List.of();
            }
            return vpcs.stream()
                    .flatMap(vpc -> aliyunVpcRepo.listSwitch(regionId, configModel, vpc.getVpcId())
                            .stream())
                    .map(e -> toSwitch(regionId, e))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AliyunVirtualSwitch.Switch toSwitch(String regionId, DescribeVSwitchesResponse.VSwitch vSwitch) {
        return AliyunVirtualSwitch.Switch.builder()
                .regionId(regionId)
                .virtualSwitch(vSwitch)
                .build();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                  AliyunVirtualSwitch.Switch entity) {
        final String key = Joiner.on(":")
                .join(entity.getVirtualSwitch()
                        .getVpcId(), entity.getVirtualSwitch()
                        .getVSwitchId());

        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVirtualSwitch()
                        .getVSwitchId())
                .nameOf(entity.getVirtualSwitch()
                        .getVSwitchName())
                .assetKeyOf(key)
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getVirtualSwitch()
                        .getZoneId())
                .descriptionOf(entity.getVirtualSwitch()
                        .getDescription())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                            EdsAsset edsAsset, AliyunVirtualSwitch.Switch entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, VIRTUAL_SWITCH_CIDR_BLOCK, entity.getVirtualSwitch()
                .getCidrBlock()));
        indices.add(createEdsAssetIndex(edsAsset, VPC_ID, entity.getVirtualSwitch()
                .getVpcId()));
        indices.add(createEdsAssetIndex(edsAsset, SUBNET_AVAILABLE_IP_ADDRESS_COUNT, entity.getVirtualSwitch()
                .getAvailableIpAddressCount()));
        return indices;
    }

}