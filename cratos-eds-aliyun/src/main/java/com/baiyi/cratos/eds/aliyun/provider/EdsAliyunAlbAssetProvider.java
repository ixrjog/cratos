package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aliyun.model.AliyunAlb;
import com.baiyi.cratos.eds.aliyun.repo.AliyunLoadBalancersRepo;
import com.baiyi.cratos.eds.aliyun.util.AliyunRegionUtil;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ALB_INSTANCE_URL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/29 14:58
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ALB)
public class EdsAliyunAlbAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunAlb.Alb> {

    private final AliyunLoadBalancersRepo aliyunAlbRepo;

    private static final String ALB_URL = "https://slb.console.aliyun.com/alb/{}/albs/{}";
    private static final String DEFAULT_ENDPOINT = "alb.cn-hangzhou.aliyuncs.com";

    public EdsAliyunAlbAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder,
                                     AliyunLoadBalancersRepo aliyunAlbRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.aliyunAlbRepo = aliyunAlbRepo;
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        List<String> endpoints = Optional.of(instance.getEdsConfigModel())
                .map(EdsAliyunConfigModel.Aliyun::getAlb)
                .map(EdsAliyunConfigModel.ALB::getEndpoints)
                .orElse(Lists.newArrayList(DEFAULT_ENDPOINT));
        return new HashSet<>(endpoints);
    }

    @Override
    protected List<AliyunAlb.Alb> listEntities(String namespace,
                                               ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunAlbRepo.listLoadBalancers(namespace, instance.getEdsConfigModel())
                    .stream()
                    .map(e -> toAlb(namespace, e))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private AliyunAlb.Alb toAlb(String endpoint,
                                ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers loadBalancers) {
        return AliyunAlb.Alb.builder()
                .regionId(AliyunRegionUtil.toRegionId(endpoint))
                .loadBalancers(loadBalancers)
                .build();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunAlb.Alb entity) {
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

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                                      EdsAsset edsAsset, AliyunAlb.Alb entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        try {
            final String url = StringFormatter.arrayFormat(ALB_URL, entity.getRegionId(), entity.getLoadBalancers()
                    .getLoadBalancerId());
            indices.add(toEdsAssetIndex(edsAsset, ALIYUN_ALB_INSTANCE_URL, url));
        } catch (Exception ignored) {
        }
        return indices;
    }

}