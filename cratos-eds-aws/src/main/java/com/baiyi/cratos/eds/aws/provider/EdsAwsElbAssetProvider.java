package com.baiyi.cratos.eds.aws.provider;


import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseHasRegionEdsAssetProvider;
import com.baiyi.cratos.eds.aws.repo.AwsElbRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/29 18:25
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_ELB)
public class EdsAwsElbAssetProvider extends BaseHasRegionEdsAssetProvider<EdsAwsConfigModel.Aws, LoadBalancer> {

    public EdsAwsElbAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<LoadBalancer> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        return AwsElbRepo.listLoadBalancer(regionId, aws);
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, LoadBalancer entity) {
        // https://docs.aws.amazon.com/elasticloadbalancing/latest/APIReference/API_DescribeLoadBalancers.html
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getLoadBalancerArn())
                .nameOf(entity.getLoadBalancerName())
                .assetKeyOf(entity.getDNSName())
                .build();
    }

}
