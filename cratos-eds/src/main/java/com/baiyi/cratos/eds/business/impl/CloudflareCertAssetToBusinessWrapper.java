package com.baiyi.cratos.eds.business.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.impl.base.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/13 13:34
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.CLOUDFLARE, assetType = EdsAssetTypeEnum.CLOUDFLARE_CERT)
public class CloudflareCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, CloudflareCert.Certificate> {

    @Override
    protected Certificate getTarget(EdsAssetVO.Asset asset) {
        CloudflareCert.Certificate model = getAssetModel(asset);
        return Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getDescription())
                .certificateType(getAssetType())
                .keyAlgorithm(model.getSignature())
                .valid(asset.getValid())
                .notBefore(model.getUploadedOn())
                .notAfter(model.getExpiresOn())
                .build();
    }

}