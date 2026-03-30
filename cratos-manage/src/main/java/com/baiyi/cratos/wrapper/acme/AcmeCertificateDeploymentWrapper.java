package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeCertificateDeployment;
import com.baiyi.cratos.domain.view.acme.AcmeCertificateVO;
import com.baiyi.cratos.service.acme.AcmeCertificateDeploymentService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/30 13:46
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ACME_CERTIFICATE_DEPLOYMENT)
public class AcmeCertificateDeploymentWrapper extends BaseDataTableConverter<AcmeCertificateVO.Deployment, AcmeCertificateDeployment> implements BaseBusinessDecorator<AcmeCertificateVO.HasAcmeCertificateDeployments, AcmeCertificateVO.Deployment> {

    private final AcmeCertificateDeploymentService acmeCertificateDeploymentService;

    @Override
    public void wrap(AcmeCertificateVO.Deployment vo) {
    }

    @Override
    public void decorateBusiness(AcmeCertificateVO.HasAcmeCertificateDeployments hasBusiness) {
        if (!IdentityUtils.hasIdentity(hasBusiness.getCertificateId())) {
            return;
        }
        hasBusiness.setCertificateDeployments(acmeCertificateDeploymentService.queryByCertificateId(
                        hasBusiness.getCertificateId())
                                                      .stream()
                                                      .map(e -> {
                                                          AcmeCertificateVO.Deployment deploymentVO = convert(e);
                                                          delegateWrap(deploymentVO);
                                                          return deploymentVO;
                                                      })
                                                      .toList());
    }

}