package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.model.AliyunEcs;
import com.baiyi.cratos.eds.aliyun.repo.AliyunEcsRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/4/11 上午10:59
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ECS)
public class EdsAliyunEcsAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunEcs.Ecs> {

    private final AliyunEcsRepo ecsRepo;

    private static final String VPC = "vpc";

    private static final String PRE_PAID = "PrePaid";

    public static Date toUtcDate(String time) {
        return TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

    @Override
    protected List<AliyunEcs.Ecs> listEntities(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        EdsAliyunConfigModel.Aliyun aliyun = instance.getEdsConfigModel();
        Set<String> regionSet = Optional.of(aliyun)
                .map(EdsAliyunConfigModel.Aliyun::getRegionIds)
                .orElse(Sets.newHashSet());

        regionSet.add(aliyun.getRegionId());
        List<AliyunEcs.Ecs> entities = Lists.newArrayList();
        try {
            regionSet.forEach(regionId -> {
                List<DescribeInstancesResponse.Instance> ecsInstances = ecsRepo.listInstances(regionId, aliyun);
                for (DescribeInstancesResponse.Instance ecsInstance : ecsInstances) {
                    List<DescribeDisksResponse.Disk> disks = ecsRepo.describeDisks(regionId, aliyun, ecsInstance.getInstanceId());
                    AliyunEcs.Ecs ecs = AliyunEcs.Ecs.builder()
                            .regionId(regionId)
                            .instance(ecsInstance)
                            .disks(disks)
                            .build();
                    entities.add(ecs);
                }
            });
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance, AliyunEcs.Ecs entity) {
        final String privateIp = entity.getInstance()
                .getInstanceNetworkType()
                .equals(VPC) ? entity.getInstance()
                .getVpcAttributes()
                .getPrivateIpAddress()
                .get(0) : entity.getInstance()
                .getInnerIpAddress()
                .get(0);

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
                .descriptionOf(entity.getInstance().getDescription())
                .build();
    }

}