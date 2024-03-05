package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareCertRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 18:03
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.CLOUDFLARE, assetType = EdsAssetTypeEnum.CLOUDFLARE_CERT)
public class CloudflareCertProvider extends BaseEdsInstanceProvider<EdsCloudflareConfigModel.Cloudflare, CloudflareCert.Certificate> {

    private final CloudflareZoneRepo cloudflareZoneRepo;

    private final CloudflareCertRepo cloudflareCertRepo;

    @Override
    protected List<CloudflareCert.Certificate> listEntities(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
        List<CloudflareCert.Certificate> results = Lists.newArrayList();
        try {
            // 查询所有的zone
            List<CloudflareZone.Result> zoneResults = cloudflareZoneRepo.listZones(instance.getEdsConfigModel());
            if (CollectionUtils.isEmpty(zoneResults)) {
                return results;
            }
            zoneResults.forEach(e -> {
                List<CloudflareCert.Result> cRt = cloudflareCertRepo.listCertificatePacks(instance.getEdsConfigModel(), e.getId());
                if (!CollectionUtils.isEmpty(cRt)) {
                    cRt.forEach(c -> results.addAll(c.getCertificates()));
                }
            });
            return results;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance, CloudflareCert.Certificate entity) {
        final String hosts = Joiner.on(",")
                .join(entity.getHosts());
        final String name = entity.getHosts()
                .stream()
                .filter(e -> e.startsWith("*."))
                .findAny()
                .orElseGet(() -> entity.getHosts()
                        .get(0));
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(name)
                .zoneOf(entity.getZoneId())
                .kindOf(entity.getIssuer())
                .statusOf(entity.getStatus())
                .createdTimeOf(entity.getUploadedOn())
                .expiredTimeOf(entity.getExpiresOn())
                .descriptionOf(hosts)
                .build();
    }

}
