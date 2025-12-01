package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PostImportProcessor;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.facade.CertificateFacade;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.CertificateDeploymentWrapper;
import com.baiyi.cratos.wrapper.CertificateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:27
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateFacadeImpl implements CertificateFacade {

    private final CertificateService certificateService;
    private final CertificateWrapper certificateWrapper;
    private final CertificateDeploymentWrapper certificateDeploymentWrapper;

    @Override
    @PostImportProcessor(ofType = BusinessTypeEnum.CERTIFICATE)
    public Certificate addCertificate(CertificateParam.AddCertificate addCertificate) {
        Certificate certificate = addCertificate.toTarget();
        certificateService.add(certificate);
        return certificate;
    }

    @Override
    public void updateCertificate(CertificateParam.UpdateCertificate updateCertificate) {
        certificateService.updateByPrimaryKey(updateCertificate.toTarget());
    }

    @Override
    public DataTable<CertificateVO.Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery) {
        DataTable<Certificate> table = certificateService.queryCertificatePage(pageQuery);
        return certificateWrapper.wrapToTarget(table);
    }

    @Override
    public void deleteByCertificateId(String certificateId) {
        certificateService.deleteByCertificateId(certificateId);
    }

    @Override
    public void deleteById(int id) {
        certificateService.deleteById(id);
    }

    @Override
    public List<String> getCertificateNameOptions(
            CertificateParam.GetCertificateNameOptions getCertificateNameOptions) {
        return certificateService.getCertificateNameOptions(getCertificateNameOptions);
    }

    @Override
    public List<CertificateVO.CertificateDeployment> getCertificateDeploymentDetails(
            CertificateParam.GetCertificateDeploymentDetails getCertificateDeploymentDetails) {
        List<Certificate> certificates = certificateService.queryByName(getCertificateDeploymentDetails.getName());
        if (CollectionUtils.isEmpty(certificates)) {
            return List.of();
        }
        return certificates.stream()
                .map(certificate -> {
                    CertificateVO.CertificateDeployment vo = CertificateVO.CertificateDeployment.builder()
                            .certificate(certificateWrapper.wrapToTarget(certificate))
                            .build();
                    certificateDeploymentWrapper.wrap(vo);
                    return vo;
                })
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return certificateService;
    }

}