package com.baiyi.cratos.eds.huaweicloud.cloud.provider;

import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.huaweicloud.cloud.model.HwcElb;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcElbRepo;
import com.huaweicloud.sdk.elb.v3.model.LoadBalancer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.ISO8601;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/1 17:04
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ELB)
public class EdsHwcElbAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Hwc, HwcElb.LoadBalancer> {

    public EdsHwcElbAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<HwcElb.LoadBalancer> listEntities(String regionId,
                                                     EdsConfigs.Hwc configModel) throws EdsQueryEntitiesException {
        List<LoadBalancer> loadBalancers = HwcElbRepo.listLoadBalancers(regionId, configModel);
        if (CollectionUtils.isEmpty(loadBalancers)) {
            return List.of();
        }
        return toLoadBalancers(regionId, loadBalancers);
    }

    private List<HwcElb.LoadBalancer> toLoadBalancers(String regionId, List<LoadBalancer> loadBalancers) {
        return loadBalancers.stream()
                .map(e -> {
                    HwcElb.LoadBalancer loadBalancer = HwcElb.LoadBalancer.of(e);
                    loadBalancer.setRegionId(regionId);
                    return loadBalancer;
                })
                .toList();
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.Hwc> instance, HwcElb.LoadBalancer entity) {
        //  https://support.huaweicloud.com/intl/zh-cn/api-elb/ListLoadBalancers.html
        try {
            return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                    .regionOf(entity)
                    .nameOf(entity.getName())
                    .createdTimeOf(TimeUtils.strToDate(entity.getCreatedAt(), ISO8601))
                    .descriptionOf(entity.getDescription())
                    .build();
        } catch (ParseException parseException) {
            throw new EdsQueryEntitiesException(parseException.getMessage());
        }
    }

}