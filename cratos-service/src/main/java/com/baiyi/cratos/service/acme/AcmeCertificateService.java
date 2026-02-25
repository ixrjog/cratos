package com.baiyi.cratos.service.acme;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.mapper.AcmeCertificateMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/13 11:36
 * &#064;Version 1.0
 */
public interface AcmeCertificateService extends BaseValidService<AcmeCertificate, AcmeCertificateMapper>, BaseUniqueKeyService<AcmeCertificate, AcmeCertificateMapper> {
}
