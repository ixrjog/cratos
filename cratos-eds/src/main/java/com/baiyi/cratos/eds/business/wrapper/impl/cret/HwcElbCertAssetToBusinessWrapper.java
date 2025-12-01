package com.baiyi.cratos.eds.business.wrapper.impl.cret;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.huaweicloud.cloud.model.HwcElb;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 13:39
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ELB_CERT)
public class HwcElbCertAssetToBusinessWrapper extends BaseAssetToBusinessWrapper<Certificate, HwcElb.Cert> {

    public HwcElbCertAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

    @Override
    protected Certificate toTarget(EdsAssetVO.Asset asset) {
        HwcElb.Cert model = getAssetModel(asset);
        String domainName = StringUtils.hasText(model.getDomain()) ? model.getDomain() : Joiner.on(",")
                .join(model.getSubjectAlternativeNames());
        return Certificate.builder()
                .certificateId(asset.getAssetId())
                .name(asset.getName())
                .domainName(domainName)
                .certificateType(getAssetType())
                .keyAlgorithm(Global.NONE)
                .valid(asset.getValid())
                .notBefore(asset.getCreatedTime())
                .notAfter(asset.getExpiredTime())
                .build();
    }

}