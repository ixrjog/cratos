package com.baiyi.cratos.eds.business.converter.impl.cert;

import com.aliyun.cas20200407.models.ListCertificatesResponseBody;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.converter.impl.BaseAssetToBusinessConverter;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/3/13 13:45
 * @Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CERT)
public class AliyunCertAssetToBusinessConverter extends BaseAssetToBusinessConverter<Certificate, ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> {

    public AliyunCertAssetToBusinessConverter(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

    @Override
    protected Certificate toTarget(EdsAssetVO.Asset asset) {
        ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList model = getAssetModel(asset);
        return Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getName())
                .certificateType(getAssetType())
                .keyAlgorithm(model.getAlgorithm())
                .valid(asset.getValid())
                .notBefore(new Date(model.getNotBefore()))
                .notAfter(new Date(model.getNotAfter()))
                .build();
    }

}