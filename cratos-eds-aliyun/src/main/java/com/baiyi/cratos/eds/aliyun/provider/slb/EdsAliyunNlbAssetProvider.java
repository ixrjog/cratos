package com.baiyi.cratos.eds.aliyun.provider.slb;


import com.aliyun.nlb20220430.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.model.AliyunNlb;
import com.baiyi.cratos.eds.aliyun.repo.AliyunNlbRepo;
import com.baiyi.cratos.eds.aliyun.util.AliyunRegionUtils;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/4 15:30
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_NLB)
public class EdsAliyunNlbAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsConfigs.Aliyun, AliyunNlb.Nlb> {
    
    private static final String DEFAULT_ENDPOINT = "nlb.cn-hangzhou.aliyuncs.com";

    public EdsAliyunNlbAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        List<String> endpoints = Optional.of(instance.getConfig())
                .map(EdsConfigs.Aliyun::getNlb)
                .map(EdsAliyunConfigModel.NLB::getEndpoints)
                .orElse(Lists.newArrayList(DEFAULT_ENDPOINT));
        return new HashSet<>(endpoints);
    }

    @Override
    protected List<AliyunNlb.Nlb> listEntities(String namespace,
                                               ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunNlbRepo.listLoadBalancers(namespace, instance.getConfig())
                    .stream()
                    .map(e -> toNlb(namespace, e))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AliyunNlb.Nlb toNlb(String endpoint,
                                ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers loadBalancers) {
        return AliyunNlb.Nlb.builder()
                .regionId(AliyunRegionUtils.toRegionId(endpoint))
                .loadBalancers(loadBalancers)
                .build();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                         AliyunNlb.Nlb entity) {
        // https://help.aliyun.com/zh/slb/application-load-balancer/developer-reference/api-alb-2020-06-16-listloadbalancers?spm=a2c4g.11186623.0.i4
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getLoadBalancers()
                        .getLoadBalancerId())
                .nameOf(entity.getLoadBalancers()
                        .getLoadBalancerName())
                .assetKeyOf(entity.getLoadBalancers()
                        .getDNSName())
                .statusOf(entity.getLoadBalancers()
                        .getLoadBalancerStatus())
                .regionOf(entity)
                .build();
    }

}