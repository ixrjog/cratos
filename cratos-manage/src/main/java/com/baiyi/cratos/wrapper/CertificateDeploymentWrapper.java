package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 15:03
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateDeploymentWrapper extends BaseDataTableConverter<CertificateVO.CertificateDeployment, Certificate> implements BaseWrapper<CertificateVO.CertificateDeployment> {

    private final EdsInstanceService instanceService;
    private final EdsAssetService assetService;
    private final BusinessAssetBoundService boundService;
    private final EdsAssetWrapper edsAssetWrapper;
    private final EdsInstanceWrapper edsInstanceWrapper;

    @Override
    public void wrap(CertificateVO.CertificateDeployment vo) {
        SimpleBusiness hasBusiness = SimpleBusiness.builder()
                .businessId(vo.getCertificate()
                                    .getBusinessId())
                .businessType(BusinessTypeEnum.CERTIFICATE.name())
                .build();
        List<BusinessAssetBound> bounds = boundService.queryByBusiness(
                hasBusiness, vo.getCertificate()
                        .getCertificateType()
        );
        if (CollectionUtils.isEmpty(bounds)) {
            return;
        }
        EdsAsset asset = assetService.getById(bounds.getFirst()
                                                      .getAssetId());
        if (asset == null) {
            return;
        }
        vo.setAsset(edsAssetWrapper.wrapToTarget(asset));
        EdsInstance instance = instanceService.getById(asset.getInstanceId());
        if (instance == null) {
            return;
        }
        vo.setEdsInstance(edsInstanceWrapper.wrapToTarget(instance));
    }

}