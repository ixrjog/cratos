package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:45
 * &#064;Version 1.0
 */
public interface GlobalNetworkFacade {

    GlobalNetwork addGlobalNetwork(GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork);

    void updateGlobalNetwork(GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork);

    DataTable<GlobalNetworkVO.GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQuery pageQuery);

    void setGlobalNetworkValidById(int id);

    void deleteById(int id);

}
