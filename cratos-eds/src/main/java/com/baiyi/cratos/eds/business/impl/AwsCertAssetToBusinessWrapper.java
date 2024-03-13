package com.baiyi.cratos.eds.business.impl;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.impl.base.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/13 13:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_CERT)
public class AwsCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, CertificateSummary> {

    @Override
    public EdsAssetVO.AssetToBusiness<Certificate> getToBusinessTarget(EdsAssetVO.Asset asset) {
        CertificateSummary model = getAssetModel(asset);
        Certificate cert = Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getName())
                .certificateType(getAssetType())
                .keyAlgorithm(model.getKeyAlgorithm())
                .valid(asset.getValid())
                .notBefore(asset.getCreatedTime())
                .notAfter(asset.getExpiredTime())
                .build();
        return EdsAssetVO.AssetToBusiness.<Certificate>builder()
                .target(cert)
                .toBusiness(getToBusiness(asset.getId()))
                .build();
    }

}