package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 11:32
 * &#064;Version 1.0
 */
public interface GlobalNetworkFacade extends HasSetValid {

    void addGlobalNetwork(GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork);

    void updateGlobalNetwork(GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork);

    DataTable<GlobalNetworkVO.Network> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQuery pageQuery);

    void deleteById(int id);

    GlobalNetworkVO.NetworkDetails queryGlobalNetworkDetails(
            GlobalNetworkParam.QueryGlobalNetworkDetails queryGlobalNetworkDetails);

    List<GlobalNetworkVO.NetworkDetails> getGlobalNetworkAllDetails();

    List<GlobalNetworkVO.Network> checkGlobalNetworkById(int id);

    List<GlobalNetworkVO.Network> checkGlobalNetworkByCidrBlock(String cidrBlock);

}
