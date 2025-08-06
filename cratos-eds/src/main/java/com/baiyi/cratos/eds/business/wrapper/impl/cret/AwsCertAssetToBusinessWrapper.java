package com.baiyi.cratos.eds.business.wrapper.impl.cret;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.BusinessAssetBindService;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/13 13:49
 * @Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_CERT)
public class AwsCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, CertificateSummary> {

    public AwsCertAssetToBusinessWrapper(BusinessAssetBindService businessAssetBindService) {
        super(businessAssetBindService);
    }

    @Override
    protected Certificate toTarget(EdsAssetVO.Asset asset) {
        CertificateSummary model = getAssetModel(asset);
        return Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(asset.getName())
                .certificateType(getAssetType())
                .keyAlgorithm(model.getKeyAlgorithm())
                .valid(asset.getValid())
                .notBefore(asset.getCreatedTime())
                .notAfter(asset.getExpiredTime())
                .build();
    }

}