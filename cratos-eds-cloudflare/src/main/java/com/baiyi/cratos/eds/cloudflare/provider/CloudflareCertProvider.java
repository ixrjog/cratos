package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.cloudflare.model.Cert;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 18:03
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.CLOUDFLARE, assetType = EdsAssetTypeEnum.CLOUDFLARE_CERT)
public class CloudflareCertProvider extends BaseEdsInstanceProvider<EdsCloudflareConfigModel.Cloudflare, Cert.Result> {

    @Override
    protected List<Cert.Result> listEntities(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
        try {
            // TODO
            return null;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance, Cert.Result entity) throws ParseException {
        // https://developers.cloudflare.com/api/operations/certificate-packs-order-advanced-certificate-manager-certificate-pack
        // TODO
        return newEdsAssetBuilder(instance, entity)
                // ARN
//                .assetIdOf(entity.getCertificateArn())
//                .nameOf(entity.getDomainName())
//                .kindOf(entity.getType())
//                .statusOf(entity.getStatus())
//                .createdTimeOf(entity.getNotBefore())
//                .expiredTimeOf(entity.getNotAfter())
                .build();
    }

}
