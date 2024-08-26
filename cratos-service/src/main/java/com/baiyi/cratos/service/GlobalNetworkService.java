package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.mapper.GlobalNetworkMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:30
 * &#064;Version 1.0
 */
public interface GlobalNetworkService extends BaseValidService<GlobalNetwork, GlobalNetworkMapper>, BaseUniqueKeyService<GlobalNetwork>, SupportBusinessService {

    DataTable<GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQueryParam param);

}