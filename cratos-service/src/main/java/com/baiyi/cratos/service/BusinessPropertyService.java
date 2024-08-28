package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.BusinessProperty;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.base.HasUniqueKey;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:05
 * @Version 1.0
 */
public interface BusinessPropertyService extends BaseBusinessService<BusinessProperty>, HasUniqueKey<BusinessProperty> {
}
