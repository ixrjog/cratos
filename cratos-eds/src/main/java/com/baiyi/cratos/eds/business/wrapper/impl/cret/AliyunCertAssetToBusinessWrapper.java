package com.baiyi.cratos.eds.business.wrapper.impl.cret;

import com.aliyun.cas20200407.models.ListUserCertificateOrderResponseBody;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
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
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CERT)
public class AliyunCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> {

    @Override
    protected Certificate getTarget(EdsAssetVO.Asset asset) {
        ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList model = getAssetModel(asset);
        return Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getName())
                .certificateType(getAssetType())
                .keyAlgorithm(model.getAlgorithm())
                .valid(asset.getValid())
                .notBefore(asset.getCreatedTime())
                .notAfter(asset.getExpiredTime())
                .build();
    }

}