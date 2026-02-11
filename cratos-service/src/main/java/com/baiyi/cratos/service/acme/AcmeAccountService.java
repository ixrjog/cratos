package com.baiyi.cratos.service.acme;

import com.baiyi.cratos.domain.generator.AcmeAccount;
import com.baiyi.cratos.mapper.AcmeAccountMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 18:09
 * &#064;Version 1.0
 */
public interface AcmeAccountService extends BaseValidService<AcmeAccount, AcmeAccountMapper>, BaseUniqueKeyService<AcmeAccount, AcmeAccountMapper> {
}
