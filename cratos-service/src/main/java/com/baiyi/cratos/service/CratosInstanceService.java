package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import com.baiyi.cratos.mapper.CratosInstanceMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 11:05
 * &#064;Version 1.0
 */
public interface CratosInstanceService extends BaseValidService<CratosInstance, CratosInstanceMapper>, BaseUniqueKeyService<CratosInstance, CratosInstanceMapper>, SupportBusinessService {

    DataTable<CratosInstance> queryCratosInstancePage(CratosInstanceParam.RegisteredInstancePageQueryParam param);

    CratosInstance getByHostIp(String hostIp);
}
