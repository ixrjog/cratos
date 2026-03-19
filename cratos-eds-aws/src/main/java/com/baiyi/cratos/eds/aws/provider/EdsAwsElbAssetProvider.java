package com.baiyi.cratos.eds.aws.provider;


import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsElbRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/29 18:25
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_ELB)
public class EdsAwsElbAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aws, LoadBalancer> {

    public EdsAwsElbAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<LoadBalancer> listEntities(String regionId, EdsConfigs.Aws aws) {
        return AwsElbRepo.listLoadBalancer(regionId, aws);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance, LoadBalancer entity) {
        // https://docs.aws.amazon.com/elasticloadbalancing/latest/APIReference/API_DescribeLoadBalancers.html
        return createAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getLoadBalancerArn())
                .nameOf(entity.getLoadBalancerName())
                .assetKeyOf(entity.getDNSName())
                .build();
    }

}
