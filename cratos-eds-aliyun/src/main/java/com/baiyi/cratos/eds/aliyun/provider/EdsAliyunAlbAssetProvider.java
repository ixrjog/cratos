package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunAlbRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/29 14:58
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_ALB)
public class EdsAliyunAlbAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> {

    private final AliyunAlbRepo aliyunAlbRepo;

    public EdsAliyunAlbAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade, AliyunAlbRepo aliyunAlbRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.aliyunAlbRepo = aliyunAlbRepo;
    }

    private static final String DEFAULT_ENDPOINT = "alb.cn-hangzhou.aliyuncs.com";

    @Override
    protected List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> entities = Lists.newArrayList();
        try {
            List<String> endpoints = Optional.of(instance.getEdsConfigModel())
                    .map(EdsAliyunConfigModel.Aliyun::getAlb)
                    .map(EdsAliyunConfigModel.ALB::getEndpoints)
                    .orElse(Lists.newArrayList(DEFAULT_ENDPOINT));
            for (String endpoint : endpoints) {
                entities.addAll(aliyunAlbRepo.listAlb(endpoint, instance.getEdsConfigModel()));
            }
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers entity) {
        // https://help.aliyun.com/zh/slb/application-load-balancer/developer-reference/api-alb-2020-06-16-listloadbalancers?spm=a2c4g.11186623.0.i4
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getLoadBalancerId())
                .nameOf(entity.getLoadBalancerName())
                .assetKeyOf(entity.getDNSName())
                .statusOf(entity.getLoadBalancerStatus())
                .build();
    }

}