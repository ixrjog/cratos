package com.baiyi.cratos.service.acme;

import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.mapper.AcmeDomainMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 17:07
 * &#064;Version 1.0
 */
public interface AcmeDomainService extends BaseValidService<AcmeDomain, AcmeDomainMapper>, BaseUniqueKeyService<AcmeDomain, AcmeDomainMapper> {
}
