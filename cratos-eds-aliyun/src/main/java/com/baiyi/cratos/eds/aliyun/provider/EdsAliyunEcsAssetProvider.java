package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.model.AliyunEcs;
import com.baiyi.cratos.eds.aliyun.repo.AliyunEcsRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/4/11 上午10:59
 * &#064;Version  1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ECS)
public class EdsAliyunEcsAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunEcs.Ecs> {

    private final AliyunEcsRepo aliyunEcsRepo;

    private static final String VPC = "vpc";
    private static final String PRE_PAID = "PrePaid";

    public EdsAliyunEcsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder, AliyunEcsRepo aliyunEcsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunEcsRepo = aliyunEcsRepo;
    }

    public static Date toUtcDate(String time) {
        return TimeUtils.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected List<AliyunEcs.Ecs> listEntities(String regionId, EdsAliyunConfigModel.Aliyun configModel) {
        List<DescribeInstancesResponse.Instance> instances = aliyunEcsRepo.listInstances(regionId, configModel);
        if (!CollectionUtils.isEmpty(instances)) {
            return toEcs(regionId, configModel, instances);
        }
        return Collections.emptyList();
    }

    private List<AliyunEcs.Ecs> toEcs(String regionId, EdsAliyunConfigModel.Aliyun configModel,
                                      List<DescribeInstancesResponse.Instance> instances) {
        return instances.stream()
                .map(e -> {
                    List<DescribeDisksResponse.Disk> disks = aliyunEcsRepo.describeDisks(regionId, configModel,
                            e.getInstanceId());
                    AliyunEcs.Ecs ecs = AliyunEcs.Ecs.builder()
                            .regionId(regionId)
                            .instance(e)
                            .disks(disks)
                            .build();
                    log.debug(ecs.toString());
                    return ecs;
                })
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunEcs.Ecs entity) {
        final String privateIp = entity.getInstance()
                .getInstanceNetworkType()
                .equals(VPC) ? entity.getInstance()
                .getVpcAttributes()
                .getPrivateIpAddress()
                .getFirst() : entity.getInstance()
                .getInnerIpAddress()
                .getFirst();
        Date expiredTime = null;
        Optional<String> optionalExpiredTime = Optional.of(entity.getInstance())
                .map(DescribeInstancesResponse.Instance::getExpiredTime);
        if (optionalExpiredTime.isPresent()) {
            expiredTime = toUtcDate(optionalExpiredTime.get());
        }
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getInstance()
                        .getInstanceId())
                .nameOf(entity.getInstance()
                        .getInstanceName())
                .assetKeyOf(privateIp)
                .kindOf(entity.getInstance()
                        .getInstanceType())
                .regionOf(entity.getRegionId())
                .zoneOf(entity.getInstance()
                        .getZoneId())
                .createdTimeOf(toUtcDate(entity.getInstance()
                        .getCreationTime()))
                .expiredTimeOf(expiredTime)
                .descriptionOf(entity.getInstance()
                        .getDescription())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.builder()
                .comparisonName(true)
                .comparisonKey(true)
                .comparisonDescription(true)
                .comparisonExpiredTime(true)
                .comparisonOriginalModel(true)
                .comparisonKind(true)
                .build()
                .compare(a1, a2);
    }

}