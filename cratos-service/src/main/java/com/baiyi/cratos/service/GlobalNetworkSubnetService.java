package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkSubnetParam;
import com.baiyi.cratos.mapper.GlobalNetworkSubnetMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:30
 * &#064;Version 1.0
 */
public interface GlobalNetworkSubnetService extends BaseValidService<GlobalNetworkSubnet, GlobalNetworkSubnetMapper>, BaseUniqueKeyService<GlobalNetworkSubnet, GlobalNetworkSubnetMapper>, SupportBusinessService {

    DataTable<GlobalNetworkSubnet> queryGlobalNetworkSubnetPage(GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQueryParam param);

    List<GlobalNetworkSubnet> queryByValid();

}