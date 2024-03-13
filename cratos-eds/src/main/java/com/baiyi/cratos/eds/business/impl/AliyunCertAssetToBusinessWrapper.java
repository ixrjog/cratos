package com.baiyi.cratos.eds.business.impl;

import com.aliyun.cas20200407.models.ListUserCertificateOrderResponseBody;
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
 * @Date 2024/3/13 13:45
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_CERT)
public class AliyunCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> {

    @Override
    public EdsAssetVO.AssetToBusiness<Certificate> getToBusinessTarget(EdsAssetVO.Asset asset) {
        ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList aliyunCert = getAssetModel(asset);
        Certificate cert = Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getDescription())
                .certificateType(getAssetType())
                .keyAlgorithm(aliyunCert.getAlgorithm())
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