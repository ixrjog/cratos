package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.network.GlobalNetworkSubnetParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:45
 * &#064;Version 1.0
 */
public interface GlobalNetworkSubnetFacade {

    GlobalNetworkSubnet addGlobalNetworkSubnet(GlobalNetworkSubnetParam.AddGlobalNetworkSubnet addGlobalNetworkSubnet);

    void updateGlobalNetworkSubnet(GlobalNetworkSubnetParam.UpdateGlobalNetworkSubnet updateGlobalNetworkSubnet);

    DataTable<GlobalNetworkVO.Subnet> queryGlobalNetworkSubnetPage(
            GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQuery pageQuery);

    void setGlobalNetworkSubnetValidById(int id);

    void deleteById(int id);

}
