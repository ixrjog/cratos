package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.ListTagResourcesResponse;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.aliyun.model.AliyunEcs;
import com.baiyi.cratos.eds.aliyun.repo.AliyunEcsRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunTagRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
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
import com.baiyi.cratos.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    private final AliyunTagRepo aliyunTagRepo;
    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    private static final String VPC = "vpc";
    private static final String PRE_PAID = "PrePaid";

    //private static final String[] TAGS = {"Group", "Env", "Name", "ServerAccount"};

    private static final SysTagKeys[] COMPUTER_TAGS = {SysTagKeys.GROUP, SysTagKeys.NAME, SysTagKeys.SERVER_ACCOUNT};

    public EdsAliyunEcsAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder, AliyunEcsRepo aliyunEcsRepo,
                                     AliyunTagRepo aliyunTagRepo, TagService tagService,
                                     BusinessTagFacade businessTagFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunEcsRepo = aliyunEcsRepo;
        this.aliyunTagRepo = aliyunTagRepo;
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
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
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
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

    @Override
    protected EdsAsset saveEntityAsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                         AliyunEcs.Ecs entity) {
        EdsAsset asset = super.saveEntityAsAsset(instance, entity);
        // 获取符合条件的标签资源
        List<ListTagResourcesResponse.TagResource> tagResources = aliyunTagRepo.listTagResources(entity.getRegionId(),
                        instance.getEdsConfigModel(), AliyunTagRepo.ResourceTypes.INSTANCE, entity.getInstance()
                                .getInstanceId())
                .stream()
                .filter(tagResource -> Arrays.stream(COMPUTER_TAGS)
                        .anyMatch(tagKey -> tagKey.name().equals(tagResource.getTagKey())))
                .toList();
        if (CollectionUtils.isEmpty(tagResources)) {
            return asset;
        }
        // 保存业务标签
        tagResources.stream()
                .map(tagResource -> {
                    Tag tag = tagService.getByTagKey(tagResource.getTagKey());
                    if (Objects.isNull(tag)) {
                        return null;
                    }
                    return BusinessTagParam.SaveBusinessTag.builder()
                            .businessId(asset.getId())
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .tagId(tag.getId())
                            .tagValue(tagResource.getTagValue())
                            .build();
                })
                .filter(Objects::nonNull)
                .forEach(businessTagFacade::saveBusinessTag);
        return asset;
    }

}