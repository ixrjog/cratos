package com.baiyi.cratos.service.datacenter;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.mapper.DatacenterNetworkMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:04
 * &#064;Version 1.0
 */
public interface DatacenterNetworkService extends BaseUniqueKeyService<DatacenterNetwork, DatacenterNetworkMapper>, BaseValidService<DatacenterNetwork, DatacenterNetworkMapper>, SupportBusinessService {

    DataTable<DatacenterNetwork> queryNetworkPage(DatacenterNetworkParam.NetworkPageQuery pageQuery);

}
