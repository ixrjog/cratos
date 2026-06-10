package com.baiyi.cratos.eds.aliyun.provider.slb;

import com.aliyun.slb20140515.models.DescribeLoadBalancerAttributeResponseBody;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.model.AliyunClb;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCLBRepo;
import com.baiyi.cratos.eds.aliyun.util.AliyunRegionUtils;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/2 17:23
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CLB)
public class EdsAliyunClbAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsConfigs.Aliyun, AliyunClb.Clb> {

    private static final String DEFAULT_ENDPOINT = "slb.cn-hangzhou.aliyuncs.com";

    public EdsAliyunClbAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        List<String> endpoints = Optional.of(instance.getConfig())
                .map(EdsConfigs.Aliyun::getClb)
                .map(EdsAliyunConfigModel.CLB::getEndpoints)
                .orElse(Lists.newArrayList(DEFAULT_ENDPOINT));
        return new HashSet<>(endpoints);
    }

    @Override
    protected List<AliyunClb.Clb> listEntities(String namespace,
                                               ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunCLBRepo.describeLoadBalancers(namespace, instance.getConfig())
                    .stream()
                    .map(e -> {
                        try {
                            return toClb(namespace, instance.getConfig(), e);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AliyunClb.Clb toClb(String endpoint, EdsConfigs.Aliyun aliyun,
                                DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer loadBalancer) throws Exception {
        DescribeLoadBalancerAttributeResponseBody attribute = AliyunCLBRepo.describeLoadBalancerAttribute(
                endpoint, aliyun, loadBalancer.getLoadBalancerId());
        return AliyunClb.Clb.builder()
                .endpoint(endpoint)
                .regionId(AliyunRegionUtils.toRegionId(endpoint))
                .loadBalancer(loadBalancer)
                .attribute(attribute)
                .build();
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance, AliyunClb.Clb entity) {
        // https://help.aliyun.com/zh/slb/application-load-balancer/developer-reference/api-alb-2020-06-16-listloadbalancers?spm=a2c4g.11186623.0.i4
        return createAssetBuilder(instance, entity).assetIdOf(entity.getLoadBalancer()
                                                                      .getLoadBalancerId())
                .nameOf(entity.getLoadBalancer()
                                .getLoadBalancerName())
                .assetKeyOf(entity.getLoadBalancer()
                                    .getLoadBalancerId())
                .statusOf(entity.getLoadBalancer()
                                  .getLoadBalancerStatus())
                .regionOf(entity)
                .build();
    }

}