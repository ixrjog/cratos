package com.baiyi.cratos.eds.aws.provider.ec2;

import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsEc2;
import com.baiyi.cratos.eds.aws.model.InstanceModel;
import com.baiyi.cratos.eds.aws.repo.AwsEc2Repo;
import com.baiyi.cratos.eds.aws.repo.Ec2InstancesRepo;
import com.baiyi.cratos.eds.aws.util.AmazonEc2Util;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午3:32
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_EC2)
public class EdsAwsEc2AssetProvider extends BaseEdsRegionAssetProvider<EdsAwsConfigModel.Aws, AwsEc2.Ec2> {

    private final Ec2InstancesRepo ec2InstancesRepo;

    private final AwsEc2Repo awsEc2Repo;

    public EdsAwsEc2AssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade, Ec2InstancesRepo ec2InstancesRepo,
                                  AwsEc2Repo awsEc2Repo,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.ec2InstancesRepo = ec2InstancesRepo;
        this.awsEc2Repo = awsEc2Repo;
    }

    @Override
    protected List<AwsEc2.Ec2> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        Map<String, InstanceModel.EC2InstanceType> instanceTypeMap;
        try {
            instanceTypeMap = ec2InstancesRepo.getInstances();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException("Error in querying Ec2 instance specifications.");
        }
        List<Instance> ec2Instances = awsEc2Repo.listInstances(regionId, aws);
        if (!CollectionUtils.isEmpty(ec2Instances)) {
            return toEC2(regionId, aws, instanceTypeMap, ec2Instances);
        }
        return Collections.emptyList();
    }

    private List<AwsEc2.Ec2> toEC2(String regionId, EdsAwsConfigModel.Aws aws,
                                   Map<String, InstanceModel.EC2InstanceType> instanceTypeMap,
                                   List<Instance> instances) {
        return instances.stream()
                .map(e -> AwsEc2.Ec2.builder()
                        .regionId(regionId)
                        .instance(e)
                        .instanceType(instanceTypeMap.getOrDefault(e.getInstanceType(), null))
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, AwsEc2.Ec2 entity) {
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getInstance()
                        .getInstanceId())
                .nameOf(AmazonEc2Util.getName(entity.getInstance()
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
