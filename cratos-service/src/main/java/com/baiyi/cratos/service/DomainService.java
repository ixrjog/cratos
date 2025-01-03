package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.http.domain.DomainParam;
import com.baiyi.cratos.mapper.DomainMapper;
import com.baiyi.cratos.service.base.BaseQueryByExpiryService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午9:58
 * @Version 1.0
 */
public interface DomainService extends BaseValidService<Domain, DomainMapper>, BaseUniqueKeyService<Domain, DomainMapper>, SupportBusinessService, BaseQueryByExpiryService<Domain> {

    DataTable<Domain> queryDomainPage(DomainParam.DomainPageQueryParam param);

}