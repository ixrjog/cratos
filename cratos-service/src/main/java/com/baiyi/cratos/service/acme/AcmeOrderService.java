package com.baiyi.cratos.service.acme;

import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.mapper.AcmeOrderMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 17:39
 * &#064;Version 1.0
 */
public interface AcmeOrderService extends BaseUniqueKeyService<AcmeOrder, AcmeOrderMapper> {

    AcmeOrder getByOrderUrl(String orderUrl);

}
