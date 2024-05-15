package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
import com.baiyi.cratos.eds.aws.model.InstanceModel;
import com.baiyi.cratos.eds.aws.repo.AwsEc2Repo;
import com.baiyi.cratos.eds.aws.repo.Ec2InstancesRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午3:32
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_EC2)
public class EdsAwsEc2AssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, AwsEc2.Ec2> {

    private final Ec2InstancesRepo ec2InstancesRepo;

    private final AwsEc2Repo awsEc2Repo;

    public EdsAwsEc2AssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade, Ec2InstancesRepo ec2InstancesRepo,
                                  AwsEc2Repo awsEc2Repo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.ec2InstancesRepo = ec2InstancesRepo;
        this.awsEc2Repo = awsEc2Repo;
    }

    @Override
    protected List<AwsEc2.Ec2> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        Map<String, InstanceModel.EC2InstanceType> instanceTypeMap;
        try {
            instanceTypeMap = ec2InstancesRepo.getInstances();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException("Error in querying Ec2 instance specifications.");
        }

        try {
            EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
            Set<String> regionIdSet = Sets.newHashSet(aws.getRegionId());
            regionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<AwsEc2.Ec2> entities = Lists.newArrayList();
            regionIdSet.forEach(regionId -> {
                List<Instance> ec2Instances = awsEc2Repo.listInstances(regionId, aws);
                if (!CollectionUtils.isEmpty(ec2Instances)) {
                    ec2Instances.forEach(ec2 -> {
                        // 过滤掉终止态的EC2
                        if (StringUtils.hasText(ec2.getPrivateIpAddress())) {
                            entities.add(AwsEc2.Ec2.builder()
                                    .regionId(regionId)
                                    .instance(ec2)
                                    .instanceType(instanceTypeMap.getOrDefault(ec2.getInstanceType(), null))
                                    .build());
                        }
                    });
                }
            });
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsEc2.Ec2 entity) {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getInstance()
                        .getInstanceId())
                .nameOf(AmazonEc2Util.getInstanceName(entity.getInstance()
                        .getTags()))
                .assetKeyOf(entity.getInstance()
                        .getPrivateIpAddress())
                .kindOf(entity.getInstance()
                        .getInstanceType())
                .regionOf(entity.getRegionId())
                .createdTimeOf(entity.getInstance()
                        .getLaunchTime())
                .build();
    }

}
