package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.domain.view.datacenter.DatacenterVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:11
 * &#064;Version 1.0
 */
public interface DatacenterFacade {

    DataTable<DatacenterVO.Network> queryNetworkPage(DatacenterNetworkParam.NetworkPageQuery pageQuery);

    void addNetwork(DatacenterNetworkParam.AddNetwork addNetwork);

    void updateNetwork(DatacenterNetworkParam.UpdateNetwork updateNetwork);

    DataTable<DatacenterVO.Allocation> queryAllocationPage(DatacenterNetworkParam.AllocationPageQuery pageQuery);

    void addAllocation(DatacenterNetworkParam.AddAllocation addAllocation);

    void updateAllocation(DatacenterNetworkParam.UpdateAllocation updateAllocation);

    void deleteAllocationById(int id);

    DatacenterVO.CidrConflictResult checkCidrConflict(DatacenterNetworkParam.CheckCidrConflict checkCidrConflict);

    DatacenterVO.AvailableCidrResult findAvailableCidrs(DatacenterNetworkParam.FindAvailableCidr findAvailableCidr);

    DatacenterVO.SubnetMap getSubnetMap(String parentCidr, int prefixLength);

    List<DatacenterVO.Allocation> queryAllocationsByCidr(String cidr);

    void scanNetworkAllocation(int networkId);

}
