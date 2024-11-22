package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkParam;
import com.baiyi.cratos.mapper.GlobalNetworkMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 17:27
 * &#064;Version 1.0
 */
public interface GlobalNetworkService extends BaseValidService<GlobalNetwork, GlobalNetworkMapper>, BaseUniqueKeyService<GlobalNetwork, GlobalNetworkMapper>, SupportBusinessService {

    DataTable<GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQueryParam param);

    List<GlobalNetwork> queryByValid();

}
