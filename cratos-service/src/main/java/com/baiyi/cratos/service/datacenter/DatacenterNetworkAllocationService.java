package com.baiyi.cratos.service.datacenter;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.mapper.DatacenterNetworkAllocationMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:55
 * &#064;Version 1.0
 */
public interface DatacenterNetworkAllocationService extends BaseUniqueKeyService<DatacenterNetworkAllocation, DatacenterNetworkAllocationMapper>, BaseValidService<DatacenterNetworkAllocation, DatacenterNetworkAllocationMapper>, SupportBusinessService {

    DataTable<DatacenterNetworkAllocation> queryAllocationPage(DatacenterNetworkParam.AllocationPageQuery pageQuery);

    List<DatacenterNetworkAllocation> queryOverlappingAllocations(long ipStart, long ipEnd, Integer excludeId);

    List<DatacenterNetworkAllocation> queryAllocationsInRange(long ipStart, long ipEnd);

    List<DatacenterNetworkAllocation> queryByNetworkId(int networkId);

}
